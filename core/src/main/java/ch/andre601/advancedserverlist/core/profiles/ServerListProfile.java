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
    
    private final List<Motd> motds;
    private final List<String> players;
    private final String playerCount;
    private final String favicon;
    private final boolean hidePlayers;
    private final boolean extraPlayersEnabled;
    private final int extraPlayers;
    
    private final Random random = new Random();
    
    public ServerListProfile(int priority, List<Expression> expressions, List<Motd> motds, List<String> players,
                             String playerCount, String favicon, boolean hidePlayers, boolean extraPlayersEnabled,
                             int extraPlayers){
        this.priority = priority;
        this.expressions = expressions;
        this.motds = motds;
        this.players = players;
        this.playerCount = playerCount;
        this.favicon = favicon;
        this.hidePlayers = hidePlayers;
        this.extraPlayersEnabled = extraPlayersEnabled;
        this.extraPlayers = extraPlayers;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public List<Motd> getMOTDs(){
        return motds;
    }
    
    public Motd getRandomMOTD(){
        if(motds.isEmpty())
            return null;
        
        if(motds.size() == 1)
            return motds.get(0);
        
        synchronized(random){
            return motds.get(random.nextInt(motds.size()));
        }
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public String getPlayerCountText(){
        return playerCount;
    }
    
    public String getFavicon(){
        return favicon;
    }
    
    public boolean isHidePlayersEnabled(){
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
    
    /*
     * Returns true if the Profile...
     * ...doesn't have any valid MOTD set AND
     * ...doesn't have any player hover set AND
     * ...doesn't have a player count text set and hidePlayers is false AND
     * ...doesn't have a favicon set.
     */
    public boolean isInvalidProfile(){
        return getMOTDs().isEmpty() &&
            getPlayers().isEmpty() &&
            (getPlayerCountText().isEmpty() && !isHidePlayersEnabled()) &&
            getFavicon().isEmpty();
    }
    
    public static class Builder{
        
        private final ConfigurationNode node;
        
        private final int priority;
        private final PluginLogger logger;
        
        private final List<Expression> expressions = new ArrayList<>();
        private final List<Motd> motds = new ArrayList<>();
        private List<String> players = new ArrayList<>();
        private String playerCount = null;
        private String favicon = null;
        private boolean hidePlayers = false;
        private boolean extraPlayersEnabled = false;
        private int extraPlayers = 0;
        
        private Builder(ConfigurationNode node, PluginLogger logger){
            this.node = node;
            this.priority = node.node("priority").getInt();
            this.logger = logger;
        }
        
        public static Builder resolve(ConfigurationNode node, PluginLogger logger){
            return new Builder(node, logger)
                .resolveExpressions()
                .resolveMOTDs()
                .resolvePlayers()
                .resolvePlayerCount()
                .resolveFavicon()
                .resolveHidePlayers()
                .resolveExtraPlayers()
                .resolveExtraPlayersCount();
        }
    
        private Builder resolveExpressions(){
            List<String> list = getList("conditions");
            if(list.isEmpty())
                return this;
            
            for(String str : list){
                Expression expression = new Expression(str);
                
                if(expression.getResult() != Expression.ExpressionResult.VALID){
                    logger.warn("Detected invalid expression in condition '%s'! Reason: %s", str, expression.getResult().getMessage());
                    continue;
                }
                
                this.expressions.add(expression);
            }
            
            return this;
        }
    
        private Builder resolveMOTDs(){
            List<String> list = getList("motds");
            if(!list.isEmpty()){
                for(String lines : list){
                    Motd motd = Motd.resolve(lines);
                    if(motd == null)
                        continue;
                    
                    this.motds.add(motd);
                }
                return this;
            }
            
            list = getList("motd");
            if(list.isEmpty())
                return this;
            
            Motd motd = Motd.resolve(list);
            if(motd == null)
                return this;
            
            this.motds.add(motd);
            return this;
        }
    
        private Builder resolvePlayers(){
            this.players = getList("playerCount", "hover");
            return this;
        }
    
        private Builder resolvePlayerCount(){
            this.playerCount = node.node("playerCount", "text").getString("");
            return this;
        }
    
        private Builder resolveFavicon(){
            this.favicon = node.node("favicon").getString("");
            return this;
        }
    
        private Builder resolveHidePlayers(){
            this.hidePlayers = node.node("playerCount", "hidePlayers").getBoolean();
            return this;
        }
    
        private Builder resolveExtraPlayers(){
            this.extraPlayersEnabled = node.node("playerCount", "extraPlayers", "enabled").getBoolean();
            return this;
        }
    
        private Builder resolveExtraPlayersCount(){
            this.extraPlayers = node.node("playerCount", "extraPlayers", "amount").getInt();
            return this;
        }
    
        public ServerListProfile build(){
            return new ServerListProfile(this.priority, this.expressions, this.motds, this.players, this.playerCount,
                this.favicon, this.hidePlayers, this.extraPlayersEnabled, this.extraPlayers);
        }
        
        private List<String> getList(Object... path){
            List<String> temp;
            try{
                temp = node.node(path).getList(String.class);
            }catch(SerializationException ex){
                temp = null;
            }
            
            if(temp == null)
                return Collections.emptyList();
            
            return temp;
        }
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
