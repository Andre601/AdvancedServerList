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

package ch.andre601.advancedserverlist.core.profiles;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public class ServerListProfile{
    
    private final ConditionsHolder conditions;
    private final int priority;
    
    private final List<String> motd;
    private final List<String> players;
    private final String playerCount;
    
    public ServerListProfile(ConfigurationNode node){
        this.conditions = new ConditionsHolder(getList(node, "conditions", false));
        this.priority = node.node("priority").getInt();
        
        this.motd = getList(node, "motd", true);
        this.players = getList(node, "players", false);
        this.playerCount = node.node("playerCount").getString("");
    }
    
    public ConditionsHolder getConditions(){
        return conditions;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public List<String> getMotd(){
        return motd;
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public String getPlayerCount(){
        return playerCount;
    }
    
    private List<String> getList(ConfigurationNode node, String key, boolean trim){
        List<String> list;
        try{
            list = node.node(key).getList(String.class);
        }catch(SerializationException ex){
            return null;
        }
        
        if(list == null)
            return null;
        
        if(trim && list.size() > 2)
            return list.subList(0, 2);
        
        return list;
    }
}
