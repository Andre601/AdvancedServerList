package ch.andre601.advancedserverlist.velocity.events;

import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import java.net.InetSocketAddress;

public class JoinEvent{
    
    private final VelocityCore plugin;
    
    public JoinEvent(VelocityCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }
    
    @Subscribe
    public void onJoin(PostLoginEvent event){
        InetSocketAddress address = event.getPlayer().getRemoteAddress();
        plugin.getCore().getPlayerHandler().addPlayer(event.getPlayer().getUsername(), address.getHostString());
    }
}
