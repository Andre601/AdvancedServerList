package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.spigot.SpigotCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetSocketAddress;

public class JoinEvent implements Listener{
    
    private final SpigotCore plugin;
    
    public JoinEvent(SpigotCore plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        InetSocketAddress address = event.getPlayer().getAddress();
        if(address == null)
            return;
        
        plugin.getCore().getPlayerHandler().addPlayer(event.getPlayer().getName(), address.getHostString());
    }
}
