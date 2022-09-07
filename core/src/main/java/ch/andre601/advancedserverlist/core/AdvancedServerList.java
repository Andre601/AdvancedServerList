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

package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.core.commands.CommandHandler;
import ch.andre601.advancedserverlist.core.file.FileHandler;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.core.ProxyCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.players.PlayerHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

public class AdvancedServerList{
    
    private final PluginCore<?> plugin;
    private final FileHandler fileHandler;
    private final CommandHandler commandHandler;
    private final PlayerHandler playerHandler;
    
    private String version;
    
    public AdvancedServerList(PluginCore<?> plugin){
        this.plugin = plugin;
        this.fileHandler = new FileHandler(this);
        this.commandHandler = new CommandHandler(this);
        this.playerHandler = new PlayerHandler(this);
        
        load();
    }
    
    public void disable(){
        getPluginLogger().info("Saving cache.data file...");
        getPlayerHandler().save();
        getPluginLogger().info("AdvancedServerList disabled!");
    }
    
    public void clearFaviconCache(){
        plugin.clearFaviconCache();
    }
    
    public PluginLogger getPluginLogger(){
        return plugin.getPluginLogger();
    }
    
    public Path getPath(){
        return plugin.getFolderPath();
    }
    
    public FileHandler getFileHandler(){
        return fileHandler;
    }
    
    public CommandHandler getCommandHandler(){
        return commandHandler;
    }
    
    public PlayerHandler getPlayerHandler(){
        return playerHandler;
    }
    
    public String getVersion(){
        return version;
    }
    
    private void load(){
        printBanner();
        resolveVersion();
        
        getPluginLogger().info("Starting AdvancedServerList v%s...", version);
        
        getPluginLogger().info("Platform: " + plugin.getPlatformName() + " " + plugin.getPlatformVersion());
        
        if(getFileHandler().loadConfig()){
            getPluginLogger().info("Successfully loaded config.yml!");
        }else{
            getPluginLogger().warn("Unable to load config.yml! Check previous lines for errors.");
            return;
        }
        
        if(getFileHandler().loadProfiles()){
            getPluginLogger().info("Successfully loaded " + getFileHandler().getProfiles().size() + " profiles!");
        }else{
            getPluginLogger().warn("Unable to load profiles! Check previous lines for errors.");
            return;
        }
        
        if(!getPath().resolve("favicons").toFile().exists() && getPath().resolve("favicons").toFile().mkdirs())
            getPluginLogger().info("Successfully created favicons folder.");
        
        getPluginLogger().info("Loading Commands...");
        plugin.loadCommands();
        getPluginLogger().info("Commands loaded!");
        
        getPluginLogger().info("Loading events...");
        plugin.loadEvents();
        getPluginLogger().info("Events loaded!");
        
        getPluginLogger().info("Loading cache.data...");
        getPlayerHandler().load();
        
        getPluginLogger().info("Loading bStats metrics. Disable it in the global config under /plugins/bstats/");
        plugin.loadMetrics();
        getPluginLogger().info("Metrics loaded!");
        
        getPluginLogger().info("AdvancedServerList is ready!");
    }
    
    private void printBanner(){
        getPluginLogger().info("");
        getPluginLogger().info("           _____ _");
        getPluginLogger().info("    /\\    / ____| |");
        getPluginLogger().info("   /  \\  | (___ | |");
        getPluginLogger().info("  / /\\ \\  \\___ \\| |");
        getPluginLogger().info(" / ____ \\ ____) | |____");
        getPluginLogger().info("/_/    \\_\\_____/|______|");
        getPluginLogger().info("");
    }
    
    private void resolveVersion(){
        try(InputStream is = getClass().getResourceAsStream("/version.properties")){
            Properties properties = new Properties();
            
            properties.load(is);
            
            version = properties.getProperty("version");
        }catch(IOException ex){
            version = "UNKNOWN";
        }
    }
}
