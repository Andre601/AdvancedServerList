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
    private final ProfileEntry defaultProfile;
    
    private List<ProfileEntry> profiles;
    
    private final Random random = new Random();
    
    public ServerListProfile(int priority, List<Expression> expressions, List<ProfileEntry> profiles, ProfileEntry defaultProfile){
        this.priority = priority;
        this.expressions = expressions;
        this.profiles = profiles;
        this.defaultProfile = defaultProfile;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public boolean evaluateConditions(Map<String, Object> replacements){
        if(expressions.isEmpty())
            return true;
        
        for(Expression expression : expressions){
            if(!expression.evaluate(replacements))
                return false;
        }
        
        return true;
    }
    
    public ProfileEntry getRandomProfile(){
        if(profiles.isEmpty()){
            if(defaultProfile == null || defaultProfile.isInvalidProfile())
                return null;
            
            return defaultProfile;
        }
        
        synchronized(random){
            return profiles.get(random.nextInt(profiles.size()));
        }
    }
    
    public List<String> getMOTD(){
        ProfileEntry profile = getRandomProfile();
        if(profile == null)
            return Collections.emptyList();
        
        if(profile.getMOTD().size() > 2)
            return profile.getMOTD().subList(0, 2);
        
        return profile.getMOTD();
    }
    
    public List<String> getPlayers(){
        ProfileEntry profile = getRandomProfile();
        if(profile == null)
            return Collections.emptyList();
        
        return profile.getPlayers();
    }
    
    public String getPlayerCountText(){
        ProfileEntry profile = getRandomProfile();
        if(profile == null)
            return null;
        
        return profile.getPlayerCountText();
    }
    
    public String getFavicon(){
        ProfileEntry profile = getRandomProfile();
        if(profile == null)
            return null;
        
        return profile.getFavicon();
    }
    
    public boolean isHidePlayersEnabled(){
        ProfileEntry profile = getRandomProfile();
        if(profile == null)
            return false;
        
        return profile.isExtraPlayersEnabled();
    }
    
    public boolean isExtraPlayersEnabled(){
        ProfileEntry profile = getRandomProfile();
        if(profile == null)
            return false;
        
        return profile.isExtraPlayersEnabled();
    }
    
    /*
     * Returns true if the Profile...
     * ...doesn't have any valid MOTD set AND
     * ...doesn't have any player hover set AND
     * ...doesn't have a player count text set and hidePlayers is false AND
     * ...doesn't have a favicon set.
     */
    public boolean isInvalidProfile(){
        boolean profilesValid = false;
        
        for(ProfileEntry profile : profiles){
            if(profile.isInvalidProfile())
                continue;
            
            profilesValid = true;
            break;
        }
    
        return !profilesValid && defaultProfile.isInvalidProfile();
    }
    
    public static class Builder{
        
        private final ConfigurationNode node;
        private final int priority;
        private final List<Expression> expressions = new ArrayList<>();
        
        private final PluginLogger logger;
        
        private List<ProfileEntry> profiles = new ArrayList<>();
        private ProfileEntry defaultProfile = null;
        
        private Builder(ConfigurationNode node, PluginLogger logger){
            this.node = node;
            this.priority = node.node("priority").getInt();
            this.logger = logger;
        }
        
        public static Builder resolve(ConfigurationNode node, PluginLogger logger){
            return new Builder(node, logger)
                .resolveExpressions()
                .resolveProfiles()
                .resolveDefaultProfile();
        }
        
        private Builder resolveExpressions(){
            List<String> temp;
            try{
                temp = node.node("conditions").getList(String.class);
            }catch(SerializationException ex){
                return this;
            }
            
            if(temp == null || temp.isEmpty())
                return this;
            
            for(String str : temp){
                Expression expr = new Expression(str);
                
                if(expr.getResult() != Expression.ExpressionResult.VALID){
                    logger.warn("Found invalid expression in condition '%s'! Reason: %s", str, expr.getResult().getMessage());
                    continue;
                }
                
                this.expressions.add(expr);
            }
            
            return this;
        }
        
        private Builder resolveProfiles(){
            try{
                this.profiles = node.node("profiles").getList(ProfileEntry.class);
            }catch(SerializationException ignored){}
            
            return this;
        }
        
        private Builder resolveDefaultProfile(){
            try{
                this.defaultProfile = node.get(ProfileEntry.class);
            }catch(SerializationException ignored){}
            return this;
        }
        
        public ServerListProfile build(){
            return new ServerListProfile(this.priority, this.expressions, this.profiles, this.defaultProfile);
        }
    }
}
