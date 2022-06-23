package ch.andre601.advancedserverlist.velocity.events;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;

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
        ServerListProfile profile = plugin.getCore().getServerListProfile(protocol.getProtocol());
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            if(motd.size() > 2)
                motd = motd.subList(0, 2);
            
            builder.description(ComponentParser.toComponent(motd));
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            builder.version(new ServerPing.Version(-1, ComponentParser.toString(profile.getPlayerCount())));
        }
        
        if(!profile.getPlayers().isEmpty()){
            ServerPing.SamplePlayer[] players = AdvancedServerList.getPlayers(ServerPing.SamplePlayer.class, profile.getPlayers())
                .toArray(new ServerPing.SamplePlayer[0]);
            
            if(players.length > 0)
                builder.clearSamplePlayers().samplePlayers(players);
        }
        
        event.setPing(builder.build());
    }
}
