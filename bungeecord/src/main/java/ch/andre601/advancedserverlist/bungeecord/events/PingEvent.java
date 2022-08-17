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
import ch.andre601.advancedserverlist.bungeecord.BungeePlayer;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.net.InetSocketAddress;

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
        
        int online = ping.getPlayers().getOnline();
        int max = ping.getPlayers().getMax();
    
        PlayerPlaceholders playerPlaceholders = new PlayerPlaceholders(
            new BungeePlayer(plugin.getCore().getPlayerHandler().getPlayerByIp(address.getHostString()), protocol.getProtocol())
        );
        ServerPlaceholders serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(playerPlaceholders)
            .replacements(serverPlaceholders)
            .getProfile();
        
        if(profile == null)
            return;
        
        int xMore = profile.getxMore();
        max = online + xMore;
        ping.getPlayers().setMax(max);
        
        serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
        
        if(!profile.getMotd().isEmpty()){
            TextComponent component = new TextComponent(BungeeComponentSerializer.get().serialize(
                ComponentParser.list(profile.getMotd())
                    .replacements(playerPlaceholders)
                    .replacements(serverPlaceholders)
                    .toComponent()
            ));
            
            ping.setDescriptionComponent(component);
        }
        
        if(profile.shouldHidePlayers()){
            ping.setPlayers(null);
            
            ping.setFavicon(ping.getFaviconObject());
            ping.setVersion(protocol);
            
            event.setResponse(ping);
            return;
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            protocol.setName(ComponentParser.text(profile.getPlayerCount())
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
                .toString()
            );
            protocol.setProtocol(-1);
        }
        
        if(!profile.getPlayers().isEmpty()){
            String players = ComponentParser.list(profile.getPlayers())
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
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
