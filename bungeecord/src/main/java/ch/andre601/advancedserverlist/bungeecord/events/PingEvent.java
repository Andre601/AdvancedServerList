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

package ch.andre601.advancedserverlist.bungeecord.events;

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.replacer.Placeholders;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.net.InetSocketAddress;
import java.util.Map;

public class PingEvent implements Listener{
    
    private final BungeeCordCore plugin;
    
    public PingEvent(BungeeCordCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Protocol protocol = ping.getVersion();
        if(protocol == null)
            return;
    
        InetSocketAddress address = (InetSocketAddress)event.getConnection().getSocketAddress();
        InetSocketAddress host = event.getConnection().getVirtualHost();
        Map<String, Object> replacements = Placeholders.get(plugin.getCore())
            .withProtocol(protocol.getProtocol())
            .withPlayersOnline(ping.getPlayers().getOnline())
            .withPlayersMax(ping.getPlayers().getMax())
            .withPlayerName(address)
            .withHostAddress(host)
            .getReplacements();
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(replacements)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(!profile.getMotd().isEmpty()){
            TextComponent component = new TextComponent(BungeeComponentSerializer.get().serialize(
                ComponentParser.list(profile.getMotd())
                    .replacements(replacements)
                    .toComponent()
            ));
            
            ping.setDescriptionComponent(component);
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            protocol.setName(ComponentParser.text(profile.getPlayerCount())
                .replacements(replacements)
                .toString()
            );
            protocol.setProtocol(-1);
        }
        
        if(!profile.getPlayers().isEmpty()){
            String players = ComponentParser.list(profile.getPlayers())
                .replacements(replacements)
                .toString();
            
            ServerPing.PlayerInfo[] playerInfos = AdvancedServerList.getPlayers(ServerPing.PlayerInfo.class, players)
                .toArray(new ServerPing.PlayerInfo[0]);
            
            if(playerInfos.length > 0)
                ping.getPlayers().setSample(playerInfos);
            
        }
        
        ping.setFavicon(ping.getFaviconObject());
        ping.setVersion(protocol);
        
        event.setResponse(ping);
    }
}
