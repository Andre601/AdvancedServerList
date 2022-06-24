package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.core.commands.CommandHandler;
import ch.andre601.advancedserverlist.core.file.FileHandler;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.players.PlayerHandler;
import ch.andre601.advancedserverlist.core.profiles.replacer.Placeholders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.*;

public class AdvancedServerList{
    
    private final PluginCore plugin;
    private final FileHandler fileHandler;
    private final CommandHandler commandHandler;
    private final PlayerHandler playerHandler;
    
    public AdvancedServerList(PluginCore plugin){
        this.plugin = plugin;
        this.fileHandler = new FileHandler(this);
        this.commandHandler = new CommandHandler(this);
        this.playerHandler = new PlayerHandler(this);
        
        load();
    }
    
    public static <T> List<T> getPlayers(Class<T> clazz, String text){
        try{
            String[] lines = text.split("\n");
            
            final List<T> players = new ArrayList<>(lines.length);
            final Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, UUID.class);
            
            constructor.setAccessible(true);
            
            for(String line : lines){
                players.add(constructor.newInstance(
                    ComponentParser.text(line).toString(), UUID.randomUUID()
                ));
            }
            
            return players;
        }catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            return Collections.emptyList();
        }
    }
    
    public Map<String, Object> loadPlaceholders(int protocol, int online, int max, InetSocketAddress ip){
        Map<String, Object> replacements = new HashMap<>();
        
        replacements.put(Placeholders.PLAYER_PROTOCOL, protocol);
        replacements.put(Placeholders.PLAYERS_ONLINE, online);
        replacements.put(Placeholders.PLAYERS_MAX, max);
        replacements.put(Placeholders.PLAYER_NAME, getPlayerHandler().getPlayerByIp(ip.getHostString()));
        
        return replacements;
    }
    
    public void disable(){
        getPluginLogger().info("Saving cache.data file...");
        getPlayerHandler().save();
        getPluginLogger().info("AdvancedServerList disabled!");
    }
    
    public PluginLogger getPluginLogger(){
        return plugin.getPluginLogger();
    }
    
    public Path getPath(){
        return plugin.getPath();
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
    
    private void load(){
        printBanner();
        getPluginLogger().info("Starting AdvancedServerList...");
        
        getPluginLogger().info("Proxy: " + plugin.getPlatformName() + " " + plugin.getPlatformVersion());
        
        if(getFileHandler().loadProfiles()){
            getPluginLogger().info("Successfully loaded " + getFileHandler().getProfiles().size() + " profiles!");
        }else{
            getPluginLogger().warn("Unable to load profiles! Check previous lines for errors.");
            return;
        }
        
        getPluginLogger().info("Loading Commands...");
        plugin.loadCommands();
        getPluginLogger().info("Commands loaded!");
        
        getPluginLogger().info("Loading events...");
        plugin.loadEvents();
        getPluginLogger().info("Events loaded!");
        
        getPluginLogger().info("Loading cache.data...");
        getPlayerHandler().load();
        
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
}
