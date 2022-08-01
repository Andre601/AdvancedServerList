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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerListProfile{
    
    private final int priority;
    private final List<Expression> expressions;
    
    private final List<String> motd;
    private final List<String> players;
    private final String playerCount;
    private final boolean hidePlayers;
    
    public ServerListProfile(ConfigurationNode node, PluginLogger logger){
        this.priority = node.node("priority").getInt();
        this.expressions = createExpressions(getList(node, "conditions", false), logger);
        
        this.motd = getList(node, "motd", true);
        this.players = getList(node, "players", false);
        this.playerCount = node.node("playerCount").getString("");
        this.hidePlayers = node.node("hidePlayers").getBoolean();
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
    
    public boolean hidePlayers(){
        return hidePlayers;
    }
    
    public boolean evalConditions(Map<String, Object> replacements){
        if(expressions.isEmpty())
            return true;
        
        for(Expression expression : expressions){
            if(!expression.evaluate(replacements))
                return false;
        }
        
        return true;
    }
    
    private List<Expression> createExpressions(List<String> list, PluginLogger logger) {
        if(list.isEmpty())
            return Collections.emptyList();
        
        List<Expression> expressions = new ArrayList<>();
        for(String str : list){
            Expression expression = new Expression(str);
            
            switch(expression.getResult()){
                case VALID -> expressions.add(expression);
                case INVALID_EMPTY_PARTS -> logInvalid(logger, str, "Either left or right part of condition was empty.");
                case INVALID_NO_EXPRESSION -> logInvalid(logger, str, "Empty conditions are not allowed.");
                case INVALID_DOUBLE_OPERATOR -> logInvalid(logger, str, "Condition had two operands!");
                case INVALID_BROKEN_NOT_EQUAL -> logInvalid(logger, str, "Found '!' without '=' following it.");
                default -> logInvalid(logger, str, "Encountered unknown issue.");
            }
        }
        
        return expressions;
    }
    
    private List<String> getList(ConfigurationNode node, String key, boolean trim){
        List<String> list;
        try{
            list = node.node(key).getList(String.class);
        }catch(SerializationException ex){
            return Collections.emptyList();
        }
        
        if(list == null)
            return Collections.emptyList();
        
        if(trim && list.size() > 2)
            return list.subList(0, 2);
        
        return list;
    }
    
    private void logInvalid(PluginLogger logger, String expression, String reason){
        logger.warn("Invalid Condition '%s'! %s", expression, reason);
    }
}
