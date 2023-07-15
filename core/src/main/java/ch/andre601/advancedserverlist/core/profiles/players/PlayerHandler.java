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

package ch.andre601.advancedserverlist.core.profiles.players;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import com.google.gson.Gson;
import io.leangen.geantyref.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlayerHandler{
    
    private final AdvancedServerList<?> core;
    private final PluginLogger logger;
    private final Path cache;
    
    private final Type listType = new TypeToken<ArrayList<CachedPlayer>>(){}.getType();
    private final Gson gson = new Gson();
    
    // UUID of MHF_Question
    private final UUID defaultUUID = UUID.fromString("606e2ff0-ed77-4842-9d6c-e1d3321c7838");
    
    private List<CachedPlayer> cachedPlayers = new ArrayList<>();
    private CachedPlayer defaultPlayer = null;
    
    public PlayerHandler(AdvancedServerList<?> core){
        this.core = core;
        this.logger = core.getPlugin().getPluginLogger();
        this.cache = core.getPlugin().getFolderPath().resolve("playercache.json");
    }
    
    public void load(){
        if(!cache.toFile().exists()){
            logger.info("No playercache.json file present. Skipping...");
            return;
        }
        
        if(core.getFileHandler().getBoolean("disableCache")){
            logger.info("'disable_cache' is set to true. Skipping playercache.json loading...");
            return;
        }
    
        try{
            BufferedReader reader = Files.newBufferedReader(cache);
            
            cachedPlayers = gson.fromJson(reader, listType);
            
            reader.close();
        }catch(IOException ex){
            logger.warn("Encountered IOException while reading the playercache.json file!", ex);
            return;
        }
        
        // In case Gson messes up... I guess.
        if(cachedPlayers == null){
            logger.warn("Couldn't load players from playercache.json file. Is the JSON valid?");
            // Create new ArrayList instance to avoid further issues.
            cachedPlayers = new ArrayList<>();
            return;
        }
        
        logger.info("Loaded %d players into cache!", cachedPlayers.size());
    }
    
    public void save(){
        if(cachedPlayers.isEmpty()){
            logger.info("No players in cache to save. Skipping...");
            return;
        }
        
        if(core.getFileHandler().getBoolean("disableCache")){
            logger.info("'disable_cache' is set to true. Skipping saving of cached players...");
            return;
        }
        
        try(BufferedWriter writer = Files.newBufferedWriter(cache, StandardCharsets.UTF_8)){
            gson.toJson(cachedPlayers, listType, writer);
            logger.info("Successfully saved playercache.json file.");
        }catch(IOException ex){
            logger.warn("Encountered IOException while saving cached players to playercache.json!", ex);
        }
        
        
    }
    
    public void addPlayer(String ip, String name, UUID uuid){
        if(contains(ip) || core.getFileHandler().getBoolean("disableCache"))
            return;
        
        cachedPlayers.add(new CachedPlayer(ip, name, uuid));
    }
    
    public CachedPlayer getCachedPlayer(String key){
        if(!contains(key) || core.getFileHandler().getBoolean("disableCache"))
            return getDefaultPlayer();
        
        for(CachedPlayer player : cachedPlayers){
            if(player.getIp().equals(key))
                return player;
        }
        
        return getDefaultPlayer();
    }
    
    public CachedPlayer getCachedPlayer(UUID uuid){
        for(CachedPlayer player : cachedPlayers){
            if(player.getUuid().equals(uuid))
                return player;
        }
        
        return getDefaultPlayer();
    }
    
    public void clearCache(){
        cachedPlayers.clear();
        defaultPlayer = null;
    }
    
    private boolean contains(String ip){
        for(CachedPlayer player : cachedPlayers){
            if(player.getIp().equals(ip))
                return true;
        }
        
        return false;
    }
    
    private CachedPlayer getDefaultPlayer(){
        if(defaultPlayer != null)
            return defaultPlayer;
        
        return (defaultPlayer = new CachedPlayer(
            "0.0.0.0",
            core.getFileHandler().getString("Anonymous", "unknownPlayer", "name"),
            convertToUUID(core.getFileHandler().getString(defaultUUID.toString(), "unknownPlayer", "uuid"))
        ));
    }
    
    private UUID convertToUUID(String uuid){
        try{
            return UUID.fromString(uuid);
        }catch(IllegalArgumentException ex){
            // This is always a valid UUID.
            return defaultUUID;
        }
    }
    
}
