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

import ch.andre601.advancedserverlist.core.check.UpdateChecker;
import ch.andre601.advancedserverlist.core.commands.CommandHandler;
import ch.andre601.advancedserverlist.core.file.FileHandler;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
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
    private final UpdateChecker updateChecker;
    
    private String version;
    
    public AdvancedServerList(PluginCore<?> plugin){
        this.plugin = plugin;
        this.fileHandler = new FileHandler(this);
        this.commandHandler = new CommandHandler(this);
        this.playerHandler = new PlayerHandler(this);
        this.updateChecker = new UpdateChecker(this);
        
        load();
    }
    
    public PluginCore<?> getPlugin(){
        return plugin;
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
    
    public void disable(){
        getPlugin().getPluginLogger().info("Saving cache.data file...");
        getPlayerHandler().save();
        getPlugin().getPluginLogger().info("AdvancedServerList disabled!");
    }
    
    public void clearFaviconCache(){
        plugin.clearFaviconCache();
    }
    
    public void checkForUpdates(String platform){
        PluginLogger logger = plugin.getPluginLogger();
    
        logger.info("Checking for updates. Please wait...");
        if(!getFileHandler().getBoolean("check_updates")){
            logger.info("'check_updates' set to false. Skipping update checks.");
            return;
        }
        
        updateChecker.checkUpdate(platform).whenComplete((version, throwable) -> {
            if(version == null || throwable != null){
                logger.warn("Update check failed! See previous messages for explanations and causes.");
                return;
            }
            
            int result = version.compare(getVersion());
            switch(result){
                case -2 -> {
                    logger.warn("Encountered an exception while comparing versions. Are they valid?");
                    logger.warn("Own version: %s; New version: %s", getVersion(), version.getVersionNumber());
                }
                case -1 -> logger.info("You seem to run a newer version compared to Modrinth. Are you running a dev build?");
                case 0 -> logger.info("No new update found. You're running the latest version!");
                case 1 -> printUpdateBanner(version.getVersionNumber(), version.getId());
            }
        });
    }
    
    private void load(){
        printBanner();
        resolveVersion();
    
        getPlugin().getPluginLogger().info("Starting AdvancedServerList v%s...", version);
        getPlugin().getPluginLogger().info("Platform: " + plugin.getPlatformName() + " " + plugin.getPlatformVersion());
        
        if(getFileHandler().loadConfig()){
            getPlugin().getPluginLogger().info("Successfully loaded config.yml!");
        }else{
            getPlugin().getPluginLogger().warn("Unable to load config.yml! Check previous lines for errors.");
            return;
        }
        
        if(getFileHandler().loadProfiles()){
            getPlugin().getPluginLogger().info("Successfully loaded " + getFileHandler().getProfiles().size() + " profiles!");
        }else{
            getPlugin().getPluginLogger().warn("Unable to load profiles! Check previous lines for errors.");
            return;
        }
        
        Path folder = getPlugin().getFolderPath().resolve("favicons");
        if(!folder.toFile().exists() && folder.toFile().mkdirs())
            getPlugin().getPluginLogger().info("Successfully created favicons folder.");
    
        getPlugin().getPluginLogger().info("Loading Commands...");
        plugin.loadCommands();
        getPlugin().getPluginLogger().info("Commands loaded!");
    
        getPlugin().getPluginLogger().info("Loading events...");
        plugin.loadEvents();
        getPlugin().getPluginLogger().info("Events loaded!");
    
        getPlugin().getPluginLogger().info("Loading cache.data...");
        getPlayerHandler().load();
    
        getPlugin().getPluginLogger().info("Loading bStats metrics. Disable it in the global config under /plugins/bstats/");
        plugin.loadMetrics();
        getPlugin().getPluginLogger().info("Metrics loaded!");
    
        getPlugin().getPluginLogger().info("AdvancedServerList is ready!");
    }
    
    private void printBanner(){
        getPlugin().getPluginLogger().info("");
        getPlugin().getPluginLogger().info("           _____ _");
        getPlugin().getPluginLogger().info("    /\\    / ____| |");
        getPlugin().getPluginLogger().info("   /  \\  | (___ | |");
        getPlugin().getPluginLogger().info("  / /\\ \\  \\___ \\| |");
        getPlugin().getPluginLogger().info(" / ____ \\ ____) | |____");
        getPlugin().getPluginLogger().info("/_/    \\_\\_____/|______|");
        getPlugin().getPluginLogger().info("");
    }
    
    private void printUpdateBanner(String version, String versionId){
        getPlugin().getPluginLogger().info("==================================================================");
        getPlugin().getPluginLogger().info("You are running an outdated version of AdvancedServerList!");
        getPlugin().getPluginLogger().info("");
        getPlugin().getPluginLogger().info("Your version: %s", getVersion());
        getPlugin().getPluginLogger().info("Modrinth version: %s", version);
        getPlugin().getPluginLogger().info("");
        getPlugin().getPluginLogger().info("You can download the latest release from here:");
        getPlugin().getPluginLogger().info("https://modrinth.com/plugin/advancedserverlist/version/%s", versionId);
        getPlugin().getPluginLogger().info("==================================================================");
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
