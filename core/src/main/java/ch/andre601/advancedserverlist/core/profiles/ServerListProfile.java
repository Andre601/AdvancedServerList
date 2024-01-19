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

package ch.andre601.advancedserverlist.core.profiles;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.conditions.Expression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ExpressionEngine;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionErrorTemplate;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplate;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class ServerListProfile{
    
    private final int priority;
    private final String condition;
    private final List<Expression> expressions;
    private final ProfileEntry defaultProfile;
    
    private final List<ProfileEntry> profiles;
    
    private final Random random = new Random();
    
    public ServerListProfile(int priority, String condition, List<Expression> expressions, ProfileEntry defaultProfile, List<ProfileEntry> profiles){
        this.priority = priority;
        this.condition = condition;
        this.expressions = expressions;
        this.defaultProfile = defaultProfile;
        
        this.profiles = profiles;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public boolean evalConditions(ExpressionEngine expressionEngine, PluginLogger logger, GenericPlayer player, GenericServer server){
        if(condition != null && !condition.isEmpty()){
            ExpressionTemplate template = expressionEngine.compile(condition, logger, player, server);
            if(template instanceof ExpressionErrorTemplate errorTemplate){
                logger.warn(errorTemplate.instantiateWithStringResult().evaluate());
                return true;
            }
            
            return template.instantiateWithBooleanResult().evaluate();
        }
        
        if(expressions.isEmpty())
            return true;
        
        for(Expression expression : expressions){
            if(!expression.evaluate(player, server))
                return false;
        }
        
        return true;
    }
    
    public ProfileEntry getRandomProfile(){
        if(profiles.isEmpty()){
            return null;
        }
        
        if(profiles.size() == 1)
            return profiles.get(0); // No need to run a random for 1 profile.
        
        synchronized(random){
            return profiles.get(random.nextInt(profiles.size()));
        }
    }
    
    public ProfileEntry getDefaultProfile(){
        return defaultProfile;
    }
    
    /*
     * Returns true if the Profile...
     * ...doesn't have any valid MOTD set AND
     * ...doesn't have any player hover set AND
     * ...doesn't have a player count text set and hidePlayers is false AND
     * ...doesn't have a favicon set.
     */
    public boolean isInvalidProfile(){
        if(profiles.isEmpty())
            return defaultProfile.isInvalid();
        
        boolean profilesValid = false;
        
        for(ProfileEntry profile : profiles){
            if(profile.isInvalid())
                continue;
            
            profilesValid = true;
            break;
        }
    
        return !profilesValid && defaultProfile.isInvalid();
    }
    
    public static class Builder{
        
        private final String fileName;
        private final ConfigurationNode node;
        private final int priority;
        private final List<Expression> expressions = new ArrayList<>();
        
        private final PluginLogger logger;
        
        private String condition = null;
        private List<ProfileEntry> profiles = new ArrayList<>();
        private ProfileEntry defaultProfile = ProfileEntry.empty();
        
        private Builder(String fileName, ConfigurationNode node, PluginLogger logger){
            this.fileName = fileName;
            this.node = node;
            this.priority = node.node("priority").getInt();
            this.logger = logger;
        }
        
        public static Builder resolve(String fileName, ConfigurationNode node, PluginLogger logger){
            return new Builder(fileName, node, logger)
                .resolveCondition()
                .resolveExpressions()
                .resolveProfiles()
                .resolveDefaultProfile();
        }
        
        private Builder resolveCondition(){
            String condition = node.node("condition").getString();
            if(condition == null || condition.isEmpty())
                return this;
            
            this.condition = condition;
            return this;
        }
        
        private Builder resolveExpressions(){
            List<String> temp;
            try{
                temp = node.node("conditions").getList(String.class);
            }catch(SerializationException ex){
                logger.warn("Encountered a SerializationException while resolving conditions for %s", ex, fileName);
                return this;
            }
            
            if(temp == null || temp.isEmpty())
                return this;
            
            StringJoiner joiner = new StringJoiner(" and ");
            
            for(String str : temp){
                Expression expr = Expression.resolve(str);
                
                if(expr.getResult() != Expression.ExpressionResult.VALID){
                    logger.warn("Found invalid expression in condition '%s'! Reason: %s", str, expr.getResult().getMessage());
                    continue;
                }
                
                joiner.add(str);
                this.expressions.add(expr);
            }
            
            if(!this.expressions.isEmpty()){
                logger.warn("'%s' uses 'conditions' which is deprecated in favour of the newer 'condition' option.", fileName);
                logger.warn("The 'conditions' option will be removed in a future version.");
                logger.warn("");
                logger.warn("Please migrate your condition(s) over from the old option to the new option like this:");
                logger.warn("consition: '%s'", joiner.toString());
            }
            
            return this;
        }
        
        private Builder resolveProfiles(){
            try{
                this.profiles = node.node("profiles").getList(ProfileEntry.class);
            }catch(SerializationException ex){
                logger.warn("Encountered a SerializationException while resolving the profiles entry for %s", ex, fileName);
            }
            
            return this;
        }
        
        private Builder resolveDefaultProfile(){
            try{
                this.defaultProfile = node.get(ProfileEntry.class);
            }catch(SerializationException ex){
                logger.warn("Encountered a SerializationException while resolving the global profile for %s", ex, fileName);
            }
            return this;
        }
        
        public ServerListProfile build(){
            return new ServerListProfile(this.priority, this.condition, this.expressions, this.defaultProfile, this.profiles);
        }
    }
}
