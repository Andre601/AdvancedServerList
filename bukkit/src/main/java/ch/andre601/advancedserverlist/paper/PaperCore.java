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

import ch.andre601.advancedserverlist.bukkit.BukkitCore;
import ch.andre601.advancedserverlist.bukkit.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.bukkit.events.JoinEvent;
import ch.andre601.advancedserverlist.bukkit.logging.BukkitLogger;
import ch.andre601.advancedserverlist.bukkit.objects.BukkitPlayerPlaceholders;
import ch.andre601.advancedserverlist.bukkit.objects.PAPIPlaceholders;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.paper.events.PaperPingEvent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.util.CachedServerIcon;

import java.nio.file.Path;

public class PaperCore extends BukkitCore<CachedServerIcon>{
    
    private final PluginLogger logger = new BukkitLogger(getLogger());
    
    private AdvancedServerList<CachedServerIcon> core;
    private FaviconHandler<CachedServerIcon> faviconHandler;
    private PAPIPlaceholders<CachedServerIcon> papiPlaceholders = null;
    
    @Override
    public void onEnable(){
        this.core = AdvancedServerList.init(this, BukkitPlayerPlaceholders.init());
        
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            papiPlaceholders = PAPIPlaceholders.init(this);
    }
    
    @Override
    public void onDisable(){
        if(papiPlaceholders != null){
            papiPlaceholders.unregister();
            papiPlaceholders = null;
        }
        
        getCore().disable();
    }
    
    @Override
    public void loadCommands(){
        if(getServer().getCommandMap().register("asl", new CmdAdvancedServerList(this))){
            getPluginLogger().info("Registered /advancedserverlist:advancedserverlist");
        }else{
            getPluginLogger().info("Registered /asl:advancedserverlist");
        }
    }
    
    @Override
    public void loadEvents(){
        new JoinEvent(this);
        new PaperPingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15584).addCustomChart(new SimplePie("profiles",
            () -> String.valueOf(core.getFileHandler().getProfiles().size())
        ));
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
        
        faviconHandler.clearCache();
    }
    
    @Override
    public AdvancedServerList<CachedServerIcon> getCore(){
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
