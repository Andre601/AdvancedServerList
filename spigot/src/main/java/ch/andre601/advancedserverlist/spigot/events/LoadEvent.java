package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.spigot.SpigotCore;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginManager;

public class LoadEvent implements Listener{
    
    private final SpigotCore plugin;
    private final ProtocolManager protocolManager;
    
    public LoadEvent(SpigotCore plugin, ProtocolManager protocolManager){
        this.plugin = plugin;
        this.protocolManager = protocolManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onServerLoad(ServerLoadEvent event){
        if(!plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            plugin.getCore().getPluginLogger().warn("ProtocolLib not found! AdvancedServerList requires it to work on Spigot!");
            
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
    
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(new JoinEvent(plugin), plugin);
        manager.registerEvents(new PingEvent(plugin, protocolManager), plugin);
    }
}
