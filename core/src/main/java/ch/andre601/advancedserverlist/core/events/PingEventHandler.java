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

import ch.andre601.advancedserverlist.core.interfaces.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;

import java.net.InetSocketAddress;

public class PingEventHandler{
    
    public static void handleEvent(PluginCore<?> plugin, GenericEventWrapper<?, ?> event){
        if(event.getProtocol() == null)
            return;
        
        String name = plugin.getCore().getPlayerHandler().getPlayerByIp(event.getHostString());
        InetSocketAddress host = event.getVirtualHost();
        
        int online = event.getOnlinePlayers();
        int max = event.getMaxPlayers();
    
        PlayerPlaceholders playerPlaceholders = new PlayerPlaceholders(event.createPlayer(name, event.getProtocolVersion()));
        ServerPlaceholders serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
    
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(playerPlaceholders)
            .replacements(serverPlaceholders)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(profile.isExtraPlayersEnabled()){
            max = online + profile.getExtraPlayers();
            event.setMaxPlayers(max);
        }
        
        serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
        
        if(!profile.getMotd().isEmpty()){
            event.setMotd(
                ComponentParser.list(profile.getMotd())
                    .replacements(playerPlaceholders)
                    .replacements(serverPlaceholders)
                    .toComponent()
            );
        }
        
        if(profile.shouldHidePlayers()){
            event.hidePlayers();
        }
        
        if(!profile.getPlayerCount().isEmpty() && !profile.shouldHidePlayers()){
            event.setPlayerCount(
                ComponentParser.text(profile.getPlayerCount())
                    .replacements(playerPlaceholders)
                    .replacements(serverPlaceholders)
                    .toString()
            );
        }
        
        if(!profile.getPlayers().isEmpty() && !profile.shouldHidePlayers()){
            event.setPlayers(profile.getPlayers(), playerPlaceholders, serverPlaceholders);
        }
        
        if(!profile.getFavicon().isEmpty()){
            String favicon = StringReplacer.replace(profile.getFavicon(), playerPlaceholders.getReplacements());
            
            event.setFavicon(favicon);
        }
        
        event.updateEvent();
    }
}
