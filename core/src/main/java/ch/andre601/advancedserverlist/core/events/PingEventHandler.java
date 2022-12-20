/*
 * MIT License
 *
 * Copyright (c) 2022 Andre_601
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

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

public class PingEventHandler{
    
    public static <F, PL, P extends GenericPlayer<?>> void handleEvent(GenericEventWrapper<F, PL, P> event){
        if(event.isInvalidProtocol())
            return;
        
        PluginCore<F, PL, P> plugin = event.getPlugin();
        String host = event.getVirtualHost();
        
        int online = event.getOnlinePlayers();
        int max = event.getMaxPlayers();
        
        P player = event.createPlayer(
            plugin.getCore().getPlayerHandler().getPlayerByIp(event.getPlayerIP()),
            event.getProtocolVersion()
        );
        GenericServer server = new GenericServer(online, max, host);
    
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .applyReplacements(player, server)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(profile.isExtraPlayersEnabled()){
            max = online + profile.getExtraPlayers();
            event.setMaxPlayers(max);
        }
        
        server = new GenericServer(online, max, host);
        
        if(!profile.getMOTDs().isEmpty()){
            ServerListProfile.Motd motd = profile.getRandomMOTD();
            
            if(motd != null){
                event.setMotd(
                    ComponentParser.text(motd.getText())
                        .applyReplacements(player, server)
                        .modifyText(text -> event.parsePAPIPlaceholders(text, player))
                        .toComponent()
                );
            }
        }
        
        if(profile.isHidePlayersEnabled()){
            event.hidePlayers();
        }
        
        if(!profile.getPlayerCountText().isEmpty() && !profile.isHidePlayersEnabled()){
            event.setPlayerCount(
                ComponentParser.text(profile.getPlayerCountText())
                    .applyReplacements(player, server)
                    .modifyText(text -> event.parsePAPIPlaceholders(text, player))
                    .toString()
            );
        }
        
        if(!profile.getPlayers().isEmpty() && !profile.isHidePlayersEnabled()){
            event.setPlayers(profile.getPlayers(), player, server);
        }
        
        if(!profile.getFavicon().isEmpty()){
            String favicon = StringReplacer.replace(profile.getFavicon(), player, server);
            
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
