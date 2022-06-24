package ch.andre601.advancedserverlist.paper.events;

import ch.andre601.advancedserverlist.paper.PaperCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetSocketAddress;

public class JoinEvent implements Listener{
    
    private final PaperCore plugin;
    
    public JoinEvent(PaperCore plugin){
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
