/*
 * MIT License
 *
 * Copyright (c) 2022 Andre_601
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

package ch.andre601.advancedserverlist.paper;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.paper.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.paper.events.JoinEvent;
import ch.andre601.advancedserverlist.paper.events.PingEvent;
import ch.andre601.advancedserverlist.paper.logging.PaperLogger;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.nio.file.Path;

public class PaperCore extends JavaPlugin implements PluginCore{
    
    private final PluginLogger logger = new PaperLogger(getLogger());
    
    private AdvancedServerList core;
    private FaviconHandler<CachedServerIcon> faviconHandler;
    
    @Override
    public void onEnable(){
        try{
            Class.forName("io.papermc.paper.configuration.ConfigurationLoaders");
            enable();
        }catch(ClassNotFoundException ex){
            try{
                Class.forName("com.destroystokyo.paper.PaperConfig");
                enable();
            }catch(ClassNotFoundException ex1){
                getPluginLogger().warn("======================================== WARNING ========================================");
                getPluginLogger().warn("");
                getPluginLogger().warn("You are using the PaperMC version of AdvancedServerList on a SpigotMC server.");
                getPluginLogger().warn("The PaperMC version is ONLY compatible with PaperMC itself due to it using exclusive");
                getPluginLogger().warn("methods and events not available within SpigotMC.");
                getPluginLogger().warn("");
                getPluginLogger().warn("To avoid any exceptions and errors will AdvancedServerList disable itself now.");
                getPluginLogger().warn("Please stop your server and either switch to PaperMC or use the Spigot version");
                getPluginLogger().warn("of AdvancedServerList.");
                getPluginLogger().warn("");
                getPluginLogger().warn("======================================== WARNING ========================================");
                
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }
    
    @Override
    public void onDisable(){
        if(getCore() == null)
            return; // This is always the case when the plugin is used on a Spigot server (See above try-catch blocks).
        
        getCore().disable();
    }
    
    @Override
    public void loadCommands(){
        PluginCommand cmd = getServer().getPluginCommand("advancedserverlist");
        if(cmd == null){
            getPluginLogger().warn("Could not register command /advancedserverlist");
            return;
        }
        cmd.setExecutor(new CmdAdvancedServerList(this));
    }
    
    @Override
    public void loadEvents(){
        new JoinEvent(this);
        new PingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15584).addCustomChart(new SimplePie("profiles",
            () -> String.valueOf(core.getFileHandler().getProfiles().size())
        ));
    }
    
    @Override
    public void clearFaviconCache(){
        faviconHandler.clearCache();
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getPath(){
        return getDataFolder().toPath();
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public String getPlatformName(){
        return getServer().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getServer().getVersion();
    }
    
    public FaviconHandler<CachedServerIcon> getFaviconHandler(){
        return faviconHandler;
    }
    
    private void enable(){
        this.core = new AdvancedServerList(this);
        this.faviconHandler = new FaviconHandler<>(core);
    }
}
