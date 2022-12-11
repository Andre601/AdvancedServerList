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

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.conditions.Expression;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerListProfile{
    
    private final int priority;
    private final List<Expression> expressions;
    
    private final List<String> motd;
    private final List<String> players;
    private final String playerCount;
    private final String favicon;
    private final boolean hidePlayers;
    private final boolean extraPlayersEnabled;
    private final int extraPlayers;
    
    public ServerListProfile(ConfigurationNode node, PluginLogger logger){
        this.priority = node.node("priority").getInt();
        this.expressions = createExpressions(getList(node, false, "conditions"), logger);
        
        this.motd = getList(node, true, "motd");
        this.players = getList(node, false, "playerCount", "hover");
        this.playerCount = node.node("playerCount", "text").getString("");
        this.favicon = node.node("favicon").getString("");
        this.hidePlayers = node.node("playerCount", "hidePlayers").getBoolean();
        this.extraPlayersEnabled = node.node("playerCount", "extraPlayers", "enabled").getBoolean();
        this.extraPlayers = node.node("playerCount", "extraPlayers", "amount").getInt();
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
    
    public <P extends GenericPlayer<?>> boolean evalConditions(P player, GenericServer server){
        if(expressions.isEmpty())
            return true;
        
        for(Expression expression : expressions){
            if(!expression.evaluate(player, server))
                return false;
        }
        
        return true;
    }
    
    public boolean isInvalid(){
        return getMotd().isEmpty() &&
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
    
    private List<String> getList(ConfigurationNode node, boolean trim, Object... path){
        List<String> list;
        try{
            list = node.node(path).getList(String.class);
        }catch(SerializationException ex){
            return Collections.emptyList();
        }
        
        if(list == null)
            return Collections.emptyList();
        
        if(trim && list.size() > 2)
            return list.subList(0, 2);
        
        return list;
    }
}
