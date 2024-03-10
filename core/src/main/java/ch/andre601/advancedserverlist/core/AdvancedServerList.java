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

package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.api.AdvancedServerListAPI;
import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.core.check.UpdateChecker;
import ch.andre601.advancedserverlist.core.commands.CommandHandler;
import ch.andre601.advancedserverlist.core.file.FileHandler;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.conditions.ProfileConditionParser;
import ch.andre601.advancedserverlist.core.profiles.players.PlayerHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AdvancedServerList<F>{
    
    private final PluginCore<F> plugin;
    private final FileHandler fileHandler;
    private final CommandHandler commandHandler;
    private final PlayerHandler playerHandler;
    private static final AdvancedServerListAPI api = AdvancedServerListAPI.get();
    private final ProfileConditionParser parser = ProfileConditionParser.create();
    
    private UpdateChecker updateChecker;
    
    private String version;
    
    private AdvancedServerList(PluginCore<F> plugin, List<PlaceholderProvider> placeholders){
        this.plugin = plugin;
        this.fileHandler = new FileHandler(this);
        this.commandHandler = new CommandHandler(this);
        this.playerHandler = new PlayerHandler(this);
        
        placeholders.forEach(AdvancedServerList.getApi()::addPlaceholderProvider);
        
        load();
    }
    
    public static <F> AdvancedServerList<F> init(PluginCore<F> plugin, PlaceholderProvider... placeholders){
        return new AdvancedServerList<>(plugin, Arrays.asList(placeholders));
    }
    
    public PluginCore<F> getPlugin(){
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
    
    public static AdvancedServerListAPI getApi(){
        return api;
    }
    
    public ProfileConditionParser getParser(){
        return parser;
    }
    
    public void disable(){
        getPlugin().getPluginLogger().info("Saving playercache.json file...");
        getPlayerHandler().save();
        
        if(updateChecker != null){
            getPlugin().getPluginLogger().info("Disabling Update Checker...");
            updateChecker.disable();
        }
        
        getPlugin().getPluginLogger().info("AdvancedServerList disabled!");
    }
    
    public void clearFaviconCache(){
        plugin.clearFaviconCache();
    }
    
    public void clearPlayerCache(){
        getPlayerHandler().clearCache();
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
        
        if(getFileHandler().isOldConfig()){
            getPlugin().getPluginLogger().info("Detected old config.yml. Attempting to migrate...");
            if(getFileHandler().migrateConfig()){
                getPlugin().getPluginLogger().info("Migration completed successfully!");
            }else{
                getPlugin().getPluginLogger().warn("Couldn't migrate config.yml! Check previous lines for errors.");
                return;
            }
        }
        
        if(getFileHandler().loadProfiles()){
            getPlugin().getPluginLogger().info("Successfully loaded %d profile(s)!", getFileHandler().getProfiles().size());
        }else{
            getPlugin().getPluginLogger().warn("Unable to load profiles! Check previous lines for errors.");
            return;
        }
        
        getPlugin().loadFaviconHandler(this);
    
        getPlugin().getPluginLogger().info("Loading Commands...");
        plugin.loadCommands();
        getPlugin().getPluginLogger().info("Commands loaded!");
    
        getPlugin().getPluginLogger().info("Loading events...");
        plugin.loadEvents();
        getPlugin().getPluginLogger().info("Events loaded!");
    
        getPlugin().getPluginLogger().info("Loading playercache.json...");
        getPlayerHandler().load();
    
        getPlugin().getPluginLogger().info("Loading bStats metrics. Disable it in the global config under /plugins/bstats/");
        plugin.loadMetrics();
        getPlugin().getPluginLogger().info("Metrics loaded!");
    
        getPlugin().getPluginLogger().info("AdvancedServerList is ready!");
        
        if(getFileHandler().getBoolean("checkUpdates"))
            this.updateChecker = new UpdateChecker(this);
    }
    
    private void printBanner(){
        getPlugin().getPluginLogger().info("           _____ _");
        getPlugin().getPluginLogger().info("    /\\    / ____| |");
        getPlugin().getPluginLogger().info("   /  \\  | (___ | |");
        getPlugin().getPluginLogger().info("  / /\\ \\  \\___ \\| |");
        getPlugin().getPluginLogger().info(" / ____ \\ ____) | |____");
        getPlugin().getPluginLogger().info("/_/    \\_\\_____/|______|");
        getPlugin().getPluginLogger().info("");
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
