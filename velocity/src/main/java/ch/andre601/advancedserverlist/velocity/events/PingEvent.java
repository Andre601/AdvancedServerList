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
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import ch.andre601.advancedserverlist.velocity.VelocityPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;

import java.net.InetSocketAddress;
import java.util.List;

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
        String playerName = plugin.getCore().getPlayerHandler().getPlayerByIp(event.getConnection().getRemoteAddress().getHostString());
        InetSocketAddress host = event.getConnection().getVirtualHost().orElse(null);
        
        int online = builder.getOnlinePlayers();
        int max = builder.getMaximumPlayers();
        
        PlayerPlaceholders playerPlaceholders = new PlayerPlaceholders(new VelocityPlayer(playerName, protocol.getProtocol()));
        ServerPlaceholders serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(playerPlaceholders)
            .replacements(serverPlaceholders)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(profile.getXMore() >= 0){
            max = online + profile.getXMore();
            builder.maximumPlayers(max);
        }
        
        serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
        
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            builder.description(ComponentParser.list(motd)
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
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
                    .replacements(playerPlaceholders)
                    .replacements(serverPlaceholders)
                    .toString()
            ));
        }
        
        if(!profile.getPlayers().isEmpty()){
            String players = ComponentParser.list(profile.getPlayers())
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
                .toString();
            
            ServerPing.SamplePlayer[] playerSamples = AdvancedServerList.getPlayers(ServerPing.SamplePlayer.class, players)
                .toArray(new ServerPing.SamplePlayer[0]);
            
            if(playerSamples.length > 0)
                builder.clearSamplePlayers().samplePlayers(playerSamples);
        }
        
        if(!profile.getFavicon().isEmpty()){
            String favName = StringReplacer.replace(profile.getFavicon(), playerPlaceholders.getReplacements());
            
            Favicon favicon = plugin.getFaviconHandler().getFavicon(favName, image -> {
                try{
                    return Favicon.create(image);
                }catch(Exception ex){
                    return null;
                }
            });
            
            if(favicon == null){
                plugin.getPluginLogger().warn("Could not obtain valid Favicon to use.");
            }else{
                builder.favicon(favicon);
            }
        }
        
        event.setPing(builder.build());
    }
}
