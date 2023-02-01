/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ch.andre601.advancedserverlist.core.events;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.GenericServerImpl;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

public class PingEventHandler{
    
    public static <F, P extends GenericPlayer> void handleEvent(GenericEventWrapper<F, P> event){
        if(event.isInvalidProtocol())
            return;
        
        PluginCore<F> plugin = event.getPlugin();
        String host = event.getVirtualHost();
        
        int online = event.getOnlinePlayers();
        int max = event.getMaxPlayers();
        
        P player = event.createPlayer(
            plugin.getCore().getPlayerHandler().getCachedPlayer(event.getPlayerIP()),
            event.getProtocolVersion()
        );
        GenericServer server = new GenericServerImpl(online, max, host);
        
        ServerListProfile profile = ProfileManager.resolveProfile(plugin.getCore(), player, server);
        
        if(profile == null)
            return;
        
        GenericServerListEvent e = event.callEvent(ProfileManager.merge(profile));
        if(e == null || e.isCancelled())
            return;
        
        ProfileEntry entry = e.getEntry();
        if(entry.isInvalid())
            return;
        
        if(ProfileManager.checkOption(entry.isExtraPlayersEnabled())){
            max = online + (entry.getExtraPlayersCount() == null ? 0 : entry.getExtraPlayersCount());
            event.setMaxPlayers(max);
        }
        
        GenericServer finalServer = new GenericServerImpl(online, max, host);
        
        if(ProfileManager.checkOption(entry.getMotd())){
            event.setMotd(
                ComponentParser.list(entry.getMotd())
                    .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                    .modifyText(text -> event.parsePAPIPlaceholders(text, player))
                    .toComponent()
            );
        }
        
        boolean hidePlayers = ProfileManager.checkOption(entry.isHidePlayersEnabled());
        
        if(hidePlayers){
            event.hidePlayers();
        }
        
        if(ProfileManager.checkOption(entry.getPlayerCountText()) && !hidePlayers){
            event.setPlayerCount(
                ComponentParser.text(entry.getPlayerCountText())
                    .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                    .modifyText(text -> event.parsePAPIPlaceholders(text, player))
                    .toString()
            );
        }
        
        if(ProfileManager.checkOption(entry.getPlayers()) && !hidePlayers){
            event.setPlayers(entry.getPlayers(), player, server);
        }
        
        if(ProfileManager.checkOption(entry.getFavicon())){
            String favicon = StringReplacer.replace(entry.getFavicon(), player, server);
            
            F fav = plugin.getFaviconHandler().getFavicon(favicon, image -> {
                try{
                    return event.createFavicon(image);
                }catch(Exception ex){
                    plugin.getPluginLogger().warn("Encountered an Exception while creating Favicon!", ex);
                    return null;
                }
            });
            
            if(fav == null){
                event.setDefaultFavicon();
            }else{
                event.setFavicon(fav);
            }
        }
        
        event.updateEvent();
    }
}
