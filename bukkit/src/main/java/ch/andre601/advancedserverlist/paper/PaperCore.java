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

package ch.andre601.advancedserverlist.paper;

import ch.andre601.advancedserverlist.bukkit.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.bukkit.events.JoinEvent;
import ch.andre601.advancedserverlist.bukkit.logging.BukkitLogger;
import ch.andre601.advancedserverlist.bukkit.objects.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.nio.file.Path;

public class PaperCore extends JavaPlugin implements PluginCore<CachedServerIcon>{
    
    private final PluginLogger logger = new BukkitLogger(getLogger());
    
    private AdvancedServerList core;
    private FaviconHandler<CachedServerIcon> faviconHandler;
    
    @Override
    public void onEnable(){
        this.core = new AdvancedServerList(this, new PlayerPlaceholders());
    }
    
    @Override
    public void onDisable(){
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
    }
    
    @Override
    public void loadMetrics(){
        
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
        
        faviconHandler.clearCache();
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getFolderPath(){
        return getDataFolder().toPath();
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public FaviconHandler<CachedServerIcon> getFaviconHandler(){
        if(faviconHandler == null)
            faviconHandler = new FaviconHandler<>(core);
        
        return faviconHandler;
    }
    
    @Override
    public String getPlatformName(){
        return getServer().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getServer().getVersion();
    }
    
    @Override
    public String getLoader(){
        return "paper";
    }
}
