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

package ch.andre601.advancedserverlist.velocity.events;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.replacer.Placeholders;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class PingEvent{
    
    private final VelocityCore plugin;
    
    public PingEvent(VelocityCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }
    
    @Subscribe(order = PostOrder.EARLY)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getPing();
        ServerPing.Version protocol = ping.getVersion();
        if(protocol == null)
            return;
    
        ServerPing.Builder builder = ping.asBuilder();
        InetSocketAddress address = event.getConnection().getRemoteAddress();
        InetSocketAddress host = event.getConnection().getVirtualHost().orElse(null);
        
        Map<String, Object> replacements = Placeholders.get(plugin.getCore())
            .withProtocol(protocol.getProtocol())
            .withPlayersOnline(builder.getOnlinePlayers())
            .withPlayersMax(builder.getMaximumPlayers())
            .withPlayerName(address)
            .withHostAddress(host)
            .withPlayerVersion(ProtocolVersion.getProtocolVersion(protocol.getProtocol()).toString())
            .getReplacements();
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(replacements)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            builder.description(ComponentParser.list(motd)
                .replacements(replacements)
                .toComponent()
            );
        }
        
        if(profile.shouldHidePlayers()){
            builder.nullPlayers();
            
            event.setPing(builder.build());
            return;
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            builder.version(new ServerPing.Version(
                -1, 
                ComponentParser.text(profile.getPlayerCount())
                    .replacements(replacements)
                    .toString()
            ));
        }
        
        if(!profile.getPlayers().isEmpty()){
            String players = ComponentParser.list(profile.getPlayers())
                .replacements(replacements)
                .toString();
            
            ServerPing.SamplePlayer[] playerSamples = AdvancedServerList.getPlayers(ServerPing.SamplePlayer.class, players)
                .toArray(new ServerPing.SamplePlayer[0]);
            
            if(playerSamples.length > 0)
                builder.clearSamplePlayers().samplePlayers(playerSamples);
        }
        
        event.setPing(builder.build());
    }
}
