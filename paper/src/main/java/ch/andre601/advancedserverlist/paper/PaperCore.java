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

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.paper.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.paper.listeners.LoadEvent;
import ch.andre601.advancedserverlist.paper.logging.PaperLogger;
import ch.andre601.advancedserverlist.paper.objects.WorldCache;
import ch.andre601.advancedserverlist.paper.objects.placeholders.PAPIPlaceholders;
import ch.andre601.advancedserverlist.paper.objects.placeholders.PaperPlayerPlaceholders;
import ch.andre601.advancedserverlist.paper.objects.placeholders.PaperServerPlaceholders;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PaperCore extends JavaPlugin implements PluginCore<CachedServerIcon>{
    
    private final PluginLogger logger = new PaperLogger(this);
    
    private AdvancedServerList<CachedServerIcon> core;
    private FaviconHandler<CachedServerIcon> faviconHandler = null;
    private PAPIPlaceholders papiPlaceholders = null;
    private WorldCache worldCache = null;
    
    @Override
    public void onEnable(){
        try{
            Class.forName("io.papermc.paper.configuration.ConfigurationLoaders");
        }catch(ClassNotFoundException ignored){
            try{
                // Above class only exists since 1.19+. This is a fallback to see if an older version is used.
                Class.forName("com.destroystokyo.paper.PaperConfig");
            }catch(ClassNotFoundException ex){
                printNoPaperWarning();
                return;
            }
        }
        
        this.core = AdvancedServerList.init(this, new PaperPlayerPlaceholders(), new PaperServerPlaceholders(this));
        
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            papiPlaceholders = new PAPIPlaceholders(this);
    }
    
    @Override
    public void onDisable(){
        if(papiPlaceholders != null){
            papiPlaceholders.unregister();
            papiPlaceholders = null;
        }
        
        if(worldCache != null)
            worldCache = null;
        
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
        new LoadEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15584).addCustomChart(new SimplePie("profiles",
            () -> String.valueOf(core.getFileHandler().getProfiles().size())
        ));
    }
    
    @Override
    public void loadFaviconHandler(AdvancedServerList<CachedServerIcon> core){
        faviconHandler = new FaviconHandler<>(core);
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
        
        faviconHandler.cleanCache();
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
    
    @Override
    public CachedServerIcon createFavicon(BufferedImage image) throws Exception{
        return Bukkit.loadServerIcon(image);
    }
    
    public WorldCache getWorldCache(){
        if(worldCache != null)
            return worldCache;
        
        return (worldCache = new WorldCache());
    }
    
    public int getPlayersOnline(World world){
        List<? extends Player> players = new ArrayList<>(world == null ? getServer().getOnlinePlayers() : world.getPlayers());
        
        players.removeIf(player -> {
            for(MetadataValue metadata : player.getMetadata("vanished")){
                if(metadata.asBoolean())
                    return true;
            }
            
            return false;
        });
        
        return players.size();
    }
    
    private void printNoPaperWarning(){
        getPluginLogger().warn("======================================================================================");
        getPluginLogger().warn("THIS SERVER DOES NOT USE PAPER OR A COMPATIBLE FORK!");
        getPluginLogger().warn("");
        getPluginLogger().warn("AdvancedServerList 3.6.0 has dropped support for Spigot-based servers.");
        getPluginLogger().warn("This means that only servers using Paper or a Paper-compatible fork may work.");
        getPluginLogger().warn("");
        getPluginLogger().warn("Reasons for this removal:");
        getPluginLogger().warn("  - Reliance on a different plugin for core functionalities.");
        getPluginLogger().warn("  - Bad dependency loading limited to MavenCentral only.");
        getPluginLogger().warn("");
        getPluginLogger().warn("Please consider switching to Paper or one of the Paper-compatible forks to benefit");
        getPluginLogger().warn("from overall performance improvements amongst other things.");
        getPluginLogger().warn("");
        getPluginLogger().warn("Disabling AdvancedServerList. If you believe this is an error, report it on");
        getPluginLogger().warn("Codeberg: https://codeberg.org/Andre601/AdvancedServerList");
        getPluginLogger().warn("======================================================================================");
        
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
