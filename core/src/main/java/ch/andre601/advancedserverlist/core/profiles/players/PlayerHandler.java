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

package ch.andre601.advancedserverlist.core.profiles.players;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.profiles.replacer.EntryList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class PlayerHandler{
    
    private final AdvancedServerList core;
    private final Path cache;
    private final EntryList<String, String> players = new EntryList<>();
    
    public PlayerHandler(AdvancedServerList core){
        this.core = core;
        this.cache = core.getPath().resolve("cache.data");
    }
    
    public void load(){
        if(!cache.toFile().exists()){
            core.getPluginLogger().info("No cache.data present. Skipping...");
            return;
        }
        List<String> lines;
        try{
            lines = Files.readAllLines(cache);
        }catch(IOException ex){
            core.getPluginLogger().warn("Encountered IOException while trying to read cache.data", ex);
            return;
        }
        
        if(lines.isEmpty()){
            core.getPluginLogger().info("cache.data is empty. Skipping...");
            return;
        }
        
        for(String line : lines){
            String[] parts = line.split("=", 2);
            if(parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty())
                continue;
            
            if(players.containsKey(parts[0]))
                continue;
            
            players.add(parts[0], parts[1]);
        }
        
        core.getPluginLogger().info("Loaded " + players.size() + " players into cache!");
    }
    
    public void save(){
        if(players.isEmpty()){
            core.getPluginLogger().info("No data to save. Skipping...");
            return;
        }
        
        StringJoiner joiner = new StringJoiner("\n");
        for(Map.Entry<String, String> entry : players){
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        
        try{
            FileWriter file = new FileWriter(cache.toFile(), StandardCharsets.UTF_8, false);
            BufferedWriter writer = new BufferedWriter(file, joiner.toString().length());
            
            writer.write(joiner.toString());
            writer.close();
            
            core.getPluginLogger().info("Successfully saved cache.data file.");
        }catch(IOException ex){
            core.getPluginLogger().warn("Cannot save player data to cache.data file!", ex);
        }
    }
    
    public void addPlayer(String name, String ip){
        if(players.containsKey(name))
            return;
        
        players.add(name, ip);
    }
    
    public String getPlayerByIp(String ip){
        for(Map.Entry<String, String> entry : players){
            if(entry.getValue().equals(ip))
                return entry.getKey();
        }
        
        return core.getFileHandler().getString("Anonymous", "unknown_player");
    }
}
