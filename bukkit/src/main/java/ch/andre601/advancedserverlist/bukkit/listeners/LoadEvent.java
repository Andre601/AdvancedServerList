/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
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

package ch.andre601.advancedserverlist.bukkit.listeners;

<<<<<<< HEAD:bukkit/src/main/java/ch/andre601/advancedserverlist/spigot/events/LoadEvent.java
import ch.andre601.advancedserverlist.bukkit.events.JoinEvent;
import ch.andre601.advancedserverlist.bukkit.objects.PAPIPlaceholders;
import ch.andre601.advancedserverlist.spigot.SpigotCore;
=======
import ch.andre601.advancedserverlist.bukkit.BukkitCore;
import ch.andre601.advancedserverlist.bukkit.objects.placeholders.PAPIPlaceholders;
import ch.andre601.advancedserverlist.spigot.SpigotCore;
import ch.andre601.advancedserverlist.spigot.listeners.ProtocolLibEvents;
>>>>>>> master:bukkit/src/main/java/ch/andre601/advancedserverlist/bukkit/listeners/LoadEvent.java
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class LoadEvent implements Listener{
    
    private final BukkitCore<?> plugin;
    
    private LoadEvent(BukkitCore<?> plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static void init(BukkitCore<?> plugin){
        new LoadEvent(plugin);
    }
    
    @EventHandler
    public void onServerLoad(ServerLoadEvent event){
        JoinEvent.init(plugin);
        WorldEvents.init(plugin);
        
        // Populate WorldCache with all loaded worlds
        Bukkit.getWorlds().forEach(world -> plugin.getWorldCache().addWorld(world.getName(), world));
        
        if(!(plugin instanceof SpigotCore spigotCore))
            return;
        
        if(!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")){
            plugin.getCore().getPlugin().getPluginLogger().warn("ProtocolLib not found! AdvancedServerList requires it to work on Spigot!");
            
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        
        ProtocolLibEvents.init(spigotCore, ProtocolLibrary.getProtocolManager());
        
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            spigotCore.setPapiPlaceholders(PAPIPlaceholders.init(spigotCore));
    }
}
