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

import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.paper.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.paper.events.JoinEvent;
import ch.andre601.advancedserverlist.paper.events.PingEvent;
import ch.andre601.advancedserverlist.paper.logging.PaperLogger;
import ch.andre601.advancedserverlist.paper.objects.PaperPlayer;
import ch.andre601.advancedserverlist.paper.objects.PlayerPlaceholders;
import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaperCore extends JavaPlugin implements PluginCore<CachedServerIcon, PlayerProfile, PaperPlayer>{
    
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
                printSpigotWarning();
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
    
    @Override
    public List<PlayerProfile> createPlayers(List<String> lines, PaperPlayer player, GenericServer server){
        List<PlayerProfile> players = new ArrayList<>(lines.size());
        
        for(String line : lines){
            String parsed = ComponentParser.text(line)
                .applyReplacements(player, server)
                .modifyText(text -> {
                    if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
                    
                    return text;
                })
                .toString();
            
            players.add(Bukkit.createProfile(UUID.randomUUID(), parsed));
        }
        
        return players;
    }
    
    private void enable(){
        this.core = new AdvancedServerList(this, new PlayerPlaceholders());
    }
    
    private void printSpigotWarning(){
        getPluginLogger().warn("======================================== WARNING ========================================");
        getPluginLogger().warn("");
        getPluginLogger().warn("You are using the Paper version of AdvancedServerList on a SpigotMC server.");
        getPluginLogger().warn("The Paper version is ONLY compatible with Paper itself due to it using exclusive");
        getPluginLogger().warn("methods and events not available within SpigotMC.");
        getPluginLogger().warn("");
        getPluginLogger().warn("To avoid any exceptions and errors will AdvancedServerList disable itself now.");
        getPluginLogger().warn("Please stop your server and either switch to Paper or use the Spigot version");
        getPluginLogger().warn("of AdvancedServerList.");
        getPluginLogger().warn("");
        getPluginLogger().warn("Paper can be downloaded here:");
        getPluginLogger().warn("https://papermc.io/downloads");
        getPluginLogger().warn("");
        getPluginLogger().warn("======================================== WARNING ========================================");
    }
}
