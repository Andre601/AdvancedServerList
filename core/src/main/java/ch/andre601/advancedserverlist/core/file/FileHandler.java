package ch.andre601.advancedserverlist.core.file;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.ProxyLogger;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FileHandler{
    
    private final FilenameFilter profileFilter = (dir, name) -> name.endsWith(".yml");
    
    private final AdvancedServerList plugin;
    private final ProxyLogger logger;
    
    private final Path config;
    private final Path path;
    
    private ConfigurationNode node = null;
    
    private final List<ServerListProfile> profiles = new ArrayList<>();
    
    public FileHandler(AdvancedServerList plugin){
        this.plugin = plugin;
        this.logger = plugin.getProxyLogger();
        
        this.config = plugin.getPath().resolve("config.yml");
        this.path = plugin.getPath();
    }
    
    public List<ServerListProfile> getProfiles(){
        return profiles;
    }
    
    public boolean loadProfiles(){
        logger.info("Loading profiles...");
        Path profileFolder = path.resolve("profiles");
        if(!profileFolder.toFile().exists() && profileFolder.toFile().mkdirs()){
            logger.info("Successfully created profiles folder.");
            
            try(InputStream stream = plugin.getClass().getResourceAsStream("/profiles/default.yml")){
                if(stream == null){
                    logger.warn("Cannot retrieve default.yml from Plugin.");
                    return false;
                }
                
                Files.copy(stream, profileFolder.resolve("default.yml"));
            }catch(IOException ex){
                logger.warn("Cannot create default.yml for plugin.", ex);
                return false;
            }
        }
        return reloadProfiles();
    }
    
    public ConfigurationNode getConfigurationNode(Path path){
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .path(path)
            .build();
        
        try{
            return loader.load();
        }catch(IOException ex){
            logger.warn("Cannot load " + path.toFile().getName() + " due to an IOException!", ex);
            return null;
        }
    }
    
    public boolean reloadProfiles(){
        profiles.clear();
        
        File[] files = path.resolve("profiles").toFile().listFiles(profileFilter);
        if(files == null || files.length == 0){
            logger.warn("Cannot load files from profiles folder! No valid YAML files present.");
            return false;
        }
    
        for(File file : files){
            ConfigurationNode tmp = getConfigurationNode(file.toPath());
            if(tmp == null)
                continue;
        
            profiles.add(new ServerListProfile(tmp));
            logger.info("Loaded " + file.getName());
        }
    
        if(profiles.isEmpty()){
            logger.warn("Couldn't load any profile from profiles folder!");
            return false;
        }
    
        profiles.sort(Comparator.comparing(ServerListProfile::getPriority));
        return !profiles.isEmpty();
    }
    
    public boolean getBoolean(Object... path){
        return node.node(path).getBoolean(false);
    }
}
