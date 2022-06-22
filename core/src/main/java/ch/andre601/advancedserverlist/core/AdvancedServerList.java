package ch.andre601.advancedserverlist.core;

import ch.andre601.advancedserverlist.core.commands.CommandHandler;
import ch.andre601.advancedserverlist.core.file.FileHandler;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.ProxyLogger;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.Condition;
import ch.andre601.advancedserverlist.core.profiles.ProxyPlayer;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;

public class AdvancedServerList{
    
    private final PluginCore core;
    private final FileHandler fileHandler;
    private final CommandHandler commandHandler;
    
    public AdvancedServerList(PluginCore core){
        this.core = core;
        this.fileHandler = new FileHandler(this);
        this.commandHandler = new CommandHandler(this);
        
        load();
    }
    
    public static <T> List<T> getPlayers(Class<T> clazz, List<String> lines){
        try{
            final List<T> players = new ArrayList<>(lines.size());
            final Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, UUID.class);
            
            constructor.setAccessible(true);
            
            for(String line : lines){
                players.add(constructor.newInstance(
                    ComponentParser.toString(line), UUID.randomUUID()
                ));
            }
            
            return players;
        }catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            return Collections.emptyList();
        }
    }
    
    public ProxyLogger getProxyLogger(){
        return core.getProxyLogger();
    }
    
    public Path getPath(){
        return core.getPath();
    }
    
    public FileHandler getFileHandler(){
        return fileHandler;
    }
    
    public CommandHandler getCommandHandler(){
        return commandHandler;
    }
    
    public ServerListProfile getServerListProfile(ProxyPlayer player){
        for(ServerListProfile profile : getFileHandler().getProfiles()){
            if(profile.getMotd().isEmpty() && profile.getPlayers().isEmpty() && profile.getPlayerCount().isEmpty())
                continue;
            
            Condition condition = profile.getCondition();
            if(condition.eval(player, getProxyLogger()))
                return profile;
        }
        
        return null;
    }
    
    private void load(){
        getProxyLogger().info("Starting AdvancedServerList...");
        
        if(getFileHandler().loadConfig()){
            getProxyLogger().info("Successfully loaded config.yml!");
        }else{
            getProxyLogger().warn("Unable to load config.yml! Check previous lines for errors.");
            return;
        }
        
        if(getFileHandler().loadProfiles()){
            getProxyLogger().info("Successfully loaded " + getFileHandler().getProfiles().size() + "profiles!");
        }else{
            getProxyLogger().warn("Unable to load profiles! Check previous lines for errors.");
            return;
        }
        
        getProxyLogger().info("Loading Commands...");
        core.loadCommands();
        getProxyLogger().info("Commands loaded!");
        
        getProxyLogger().info("Loading events...");
        core.loadEvents();
        getProxyLogger().info("Events loaded!");
        
        getProxyLogger().info("AdvancedServerList is ready!");
    }
}
