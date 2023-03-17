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

package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.spigot.SpigotCore;
import ch.andre601.advancedserverlist.spigot.objects.PAPIPlaceholders;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginManager;

public class LoadEvent implements Listener{
    
    private final SpigotCore plugin;
    
    public LoadEvent(SpigotCore plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onServerLoad(ServerLoadEvent event){
        PluginManager manager = plugin.getServer().getPluginManager();
        
        if(!manager.isPluginEnabled("ProtocolLib")){
            plugin.getCore().getPlugin().getPluginLogger().warn("ProtocolLib not found! AdvancedServerList requires it to work on Spigot!");
    
            manager.disablePlugin(plugin);
            return;
        }
        
        manager.registerEvents(new JoinEvent(plugin), plugin);
        manager.registerEvents(new PingEvent(plugin, ProtocolLibrary.getProtocolManager()), plugin);
        
        if(manager.isPluginEnabled("PlaceholderAPI"))
            new PAPIPlaceholders(plugin);
    }
}
