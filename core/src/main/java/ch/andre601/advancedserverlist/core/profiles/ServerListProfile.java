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

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.conditions.Expression;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

public class ServerListProfile{
    
    private final int priority;
    private final List<Expression> expressions;
    
    private final List<Motd> motds = new ArrayList<>();
    private final List<String> players;
    private final String playerCount;
    private final String favicon;
    private final boolean hidePlayers;
    private final boolean extraPlayersEnabled;
    private final int extraPlayers;
    
    private final Random motdRandom;
    
    public ServerListProfile(ConfigurationNode node, PluginLogger logger){
        this.priority = node.node("priority").getInt();
        this.expressions = createExpressions(getList(node, "conditions"), logger);
        
        loadMotds(node);
        this.players = getList(node, "playerCount", "hover");
        this.playerCount = node.node("playerCount", "text").getString("");
        this.favicon = node.node("favicon").getString("");
        this.hidePlayers = node.node("playerCount", "hidePlayers").getBoolean();
        this.extraPlayersEnabled = node.node("playerCount", "extraPlayers", "enabled").getBoolean();
        this.extraPlayers = node.node("playerCount", "extraPlayers", "amount").getInt();
        
        this.motdRandom = motds.size() <= 1 ? null : new Random();
    }
    
    public int getPriority(){
        return priority;
    }
    
    public Motd getMotd(){
        if(motdRandom == null){
            if(motds.isEmpty())
                return null;
            
            return motds.get(0);
        }
        
        return motds.get(motdRandom.nextInt(motds.size()));
    }
    
    public List<Motd> getMotds(){
        return motds;
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public String getPlayerCount(){
        return playerCount;
    }
    
    public String getFavicon(){
        return favicon;
    }
    
    public boolean shouldHidePlayers(){
        return hidePlayers;
    }
    
    public boolean isExtraPlayersEnabled(){
        return extraPlayersEnabled;
    }
    
    public int getExtraPlayers(){
        return extraPlayers;
    }
    
    public boolean evalConditions(Map<String, Object> replacements){
        if(expressions.isEmpty())
            return true;
        
        for(Expression expression : expressions){
            if(!expression.evaluate(replacements)){
                return false;
            }
        }
        
        return true;
    }
    
    public boolean isInvalid(){
        return getMotds().isEmpty() &&
            getPlayers().isEmpty() &&
            (getPlayerCount().isEmpty() && !shouldHidePlayers()) &&
            getFavicon().isEmpty();
    }
    
    private List<Expression> createExpressions(List<String> list, PluginLogger logger) {
        if(list.isEmpty())
            return Collections.emptyList();
        
        List<Expression> expressions = new ArrayList<>();
        for(String str : list){
            Expression expression = new Expression(str);
            
            if(expression.getResult() != Expression.ExpressionResult.VALID){
                logger.warn("Detected Invalid condition '%s'! Cause: %s", str, expression.getResult().getMessage());
                continue;
            }
            
            expressions.add(expression);
        }
        
        return expressions;
    }
    
    private void loadMotds(ConfigurationNode node){
        List<String> temp = getList(node, "motds");
        
        if(!temp.isEmpty()){
            for(String lines : temp){
                Motd motd = Motd.resolve(lines);
                if(motd == null)
                    continue;
                
                motds.add(motd);
            }
            return;
        }
        
        temp = getList(node, "motd");
        if(temp.isEmpty())
            return;
        
        Motd motd = Motd.resolve(temp);
        if(motd == null)
            return;
        
        motds.add(motd);
    }
    
    private List<String> getList(ConfigurationNode node, Object... path){
        List<String> list;
        try{
            list = node.node(path).getList(String.class);
        }catch(SerializationException ex){
            return Collections.emptyList();
        }
        
        if(list == null)
            return Collections.emptyList();
        
        return list;
    }
    
    public static class Motd{
        String text;
        
        private Motd(String[] lines){
            text = resolveLines(lines);
        }
        
        public static Motd resolve(String text){
            String[] split = text.split("\n");
            if(split.length == 0)
                return null;
            
            return new Motd(split);
        }
        
        public static Motd resolve(List<String> lines){
            if(lines.size() == 0)
                return null;
            
            return new Motd(lines.toArray(new String[0]));
        }
    
        public String getText(){
            return text;
        }
    
        private String resolveLines(String[] lines){
            if(lines.length > 2)
                return String.join("\n", lines[0], lines[1]);
            
            return String.join("\n", lines);
        }
    }
}
