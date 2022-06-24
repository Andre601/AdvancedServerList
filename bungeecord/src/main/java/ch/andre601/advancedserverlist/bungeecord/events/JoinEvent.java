package ch.andre601.advancedserverlist.bungeecord.events;

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class JoinEvent implements Listener{
    
    private final BungeeCordCore plugin;
    
    public JoinEvent(BungeeCordCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    
    @EventHandler
    public void onJoin(PostLoginEvent event){
        InetSocketAddress address = (InetSocketAddress)event.getPlayer().getPendingConnection().getSocketAddress();
        plugin.getCore().getPlayerHandler().addPlayer(event.getPlayer().getName(), address.getHostString());
    }
}
