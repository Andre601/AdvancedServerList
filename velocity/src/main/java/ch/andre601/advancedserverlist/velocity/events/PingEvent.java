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
        
        Map<String, Object> replacements = plugin.getCore()
            .loadPlaceholders(protocol.getProtocol(), builder.getOnlinePlayers(), builder.getMaximumPlayers(), address);
        replacements.put(Placeholders.PLAYER_VERSION, ProtocolVersion.getProtocolVersion(protocol.getProtocol()));
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(replacements)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            if(motd.size() > 2)
                motd = motd.subList(0, 2);
            
            builder.description(ComponentParser.list(motd)
                .replacements(replacements)
                .toComponent()
            );
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
