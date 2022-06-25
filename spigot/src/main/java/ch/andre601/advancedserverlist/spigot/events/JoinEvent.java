package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.spigot.SpigotCore;
import org.bukkit.event.Listener;

public class JoinEvent implements Listener{
    
    private final SpigotCore plugin;
    
    public JoinEvent(SpigotCore plugin){
        this.plugin = plugin;
    }
}
