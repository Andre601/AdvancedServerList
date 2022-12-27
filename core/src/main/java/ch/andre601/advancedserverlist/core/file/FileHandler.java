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

package ch.andre601.advancedserverlist.core.file;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileSerializer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileHandler{
    
    private final AdvancedServerList plugin;
    private final PluginLogger logger;
    
    private final Path config;
    private final Path profilesFolder;
    
    private final List<ServerListProfile> profiles = new ArrayList<>();
    
    private ConfigurationNode node = null;
    
    public FileHandler(AdvancedServerList core){
        this.plugin = core;
        this.logger = core.getPlugin().getPluginLogger();
        
        this.config = core.getPlugin().getFolderPath().resolve("config.yml");
        this.profilesFolder = core.getPlugin().getFolderPath().resolve("profiles");
    }
    
    public List<ServerListProfile> getProfiles(){
        return profiles;
    }
    
    public boolean loadConfig(){
        logger.info("Loading config.yml...");
        File folder = config.toFile().getParentFile();
        if(!folder.exists() && !folder.mkdirs()){
            logger.warn("Couldn't create folder for plugin. Is it missing Write permissions?");
            return false;
        }
        
        if(!config.toFile().exists()){
            try(InputStream stream = plugin.getClass().getResourceAsStream("/config.yml")){
                if(stream == null){
                    logger.warn("Cannot retrieve config.yml from plugin.");
                    return false;
                }
                
                Files.copy(stream, config);
            }catch(IOException ex){
                logger.warn("Cannot create config.yml for plugin.", ex);
                return false;
            }
        }
        
        return reloadConfig();
    }
    
    public boolean loadProfiles(){
        logger.info("Loading profiles...");
        if(!profilesFolder.toFile().exists() && profilesFolder.toFile().mkdirs()){
            logger.info("Successfully created profiles folder.");
            
            try(InputStream stream = plugin.getClass().getResourceAsStream("/profiles/default.yml")){
                if(stream == null){
                    logger.warn("Cannot retrieve default.yml from Plugin.");
                    return false;
                }
                
                Files.copy(stream, profilesFolder.resolve("default.yml"));
            }catch(IOException ex){
                logger.warn("Cannot create default.yml for plugin.", ex);
                return false;
            }
        }
        
        return reloadProfiles();
    }
    
    public boolean reloadConfig(){
        return (node = getConfigurationNode(config)) != null;
    }
    
    public boolean reloadProfiles(){
        profiles.clear();
        
        File[] files = profilesFolder.toFile().listFiles(((dir, name) -> name.endsWith(".yml")));
        if(files == null || files.length == 0){
            logger.warn("Cannot load files from profiles folder! No valid YAML files present.");
            return false;
        }
        
        for(File file : files){
            ConfigurationNode tmp = getConfigurationNode(file.toPath());
            if(tmp == null)
                continue;
            
            profiles.add(ServerListProfile.Builder.resolve(tmp, logger).build());
            logger.info("Loaded " + file.getName());
        }
        
        if(profiles.isEmpty()){
            logger.warn("Couldn't load any profile from profiles folder!");
            return false;
        }
        
        profiles.sort(Comparator.comparing(ServerListProfile::getPriority).reversed());
        return !profiles.isEmpty();
    }
    
    public ConfigurationNode getConfigurationNode(Path path){
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .defaultOptions(opts -> opts.serializers(build -> build.register(ProfileEntry.class, ProfileSerializer.INSTANCE)))
            .path(path)
            .build();
        
        try{
            return loader.load();
        }catch(IOException ex){
            logger.warn("Cannot load " + path.toFile().getName() + " due to an IOException!", ex);
            return null;
        }
    }
    
    public String getString(String def, Object... path){
        return node.node(path).getString(def);
    }
    
    public boolean getBoolean(Object... path){
        return node.node(path).getBoolean();
    }
}
