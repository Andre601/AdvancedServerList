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
import ch.andre601.advancedserverlist.core.profiles.conditions.ProfileConditionParser;
import ch.andre601.expressionparser.ParseWarnCollector;
import ch.andre601.expressionparser.templates.ExpressionTemplate;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record ServerListProfile(int priority, String condition, ProfileEntry defaultProfile, List<ProfileEntry> profiles, String file){
    private static final Random random = new Random();
    
    public boolean evalConditions(ProfileConditionParser parser, PluginLogger logger, GenericPlayer player, GenericServer server){
        if(condition == null || condition.isEmpty())
            return true;
        
        ParseWarnCollector collector = new ParseWarnCollector(condition);
        ExpressionTemplate template = parser.compile(condition, player, server, collector);
        
        if(collector.hasWarnings()){
            logger.warn("Encountered %d Error(s) while parsing condition for file '%s':", collector.getWarnings().size(), file);
            
            for(ParseWarnCollector.Context context : collector.getWarnings()){
                if(context.position() <= -1){
                    logger.warn("  - %s", condition);
                    logger.warn("    -> %s", context.message());
                }else{
                    logger.warn("  - At position %d:", context.position());
                    logger.warn("    %s", condition);
                    logger.warn(" ".repeat(context.position() + 5) + "^");
                    logger.warn("    -> %s", context.message());
                }
            }
        }
        
        if(template == null)
            return false;
        
        return template.returnBooleanExpression().evaluate();
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
    
    /*
     * Returns true if the Profile...
     * ...doesn't have any valid MOTD set AND
     * ...doesn't have any player hover set AND
     * ...doesn't have a player count text set and hidePlayers is false AND
     * ...doesn't have a favicon set.
     */
    public boolean isInvalidProfile(){
        if(profiles().isEmpty())
            return defaultProfile().isInvalid();
        
        boolean profilesValid = false;
        
        for(ProfileEntry profile : profiles()){
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
            return new ServerListProfile(this.priority, this.condition, this.defaultProfile, this.profiles, this.fileName);
        }
    }
}
