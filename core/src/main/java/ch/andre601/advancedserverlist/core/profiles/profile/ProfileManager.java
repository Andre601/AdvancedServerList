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

package ch.andre601.advancedserverlist.core.profiles.profile;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.objects.NullBool;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

public class ProfileManager{
    
    /*
     * Convenience method to get a ProfileEntry instance with values merged.
     * If the random profile's value is not null will it be used instead of the default one.
     */
    public static ProfileEntry merge(ServerListProfile profile){
        ProfileEntry entry = profile.getRandomProfile();
        ProfileEntry defEntry = profile.defaultProfile();
        
        List<String> motd = resolveMOTD(entry, defEntry);
        List<String> players = resolvePlayers(entry, defEntry);
        String playerCountText = resolvePlayerCountText(entry, defEntry);
        String favicon = resolveFavicon(entry, defEntry);
        boolean isHidePlayersEnabled = resolveHidePlayersEnabled(entry, defEntry);
        boolean isExtraPlayersEnabled = resolveExtraPlayersEnabled(entry, defEntry);
        boolean isMaxPlayersEnabled = resolveMaxPlayersEnabled(entry, defEntry);
        Integer extraPlayersCount = resolveExtraPlayersCount(entry, defEntry);
        Integer maxPlayersCount = resolveMaxPlayersCount(entry, defEntry);
        
        return new ProfileEntry(motd, players, playerCountText, favicon, 
            NullBool.resolve(isHidePlayersEnabled), NullBool.resolve(isExtraPlayersEnabled), NullBool.resolve(isMaxPlayersEnabled),
            extraPlayersCount, maxPlayersCount);
    }
    
    public static ServerListProfile resolveProfile(AdvancedServerList<?> core, GenericPlayer player, GenericServer server){
        for(ServerListProfile profile : core.getFileHandler().getProfiles()){
            if(profile.isInvalidProfile())
                continue;
            
            if(profile.evalConditions(core.getExpressionEngine(), core.getPlugin().getPluginLogger(), player, server))
                return profile;
        }
        
        return null;
    }
    
    public static ProfileEntry retrieveProfileEntry(ConfigurationNode node){
        List<String> motd = resolveList(node, "motd");
        List<String> players = resolveList(node, "playerCount", "hover");
        String playerCountText = node.node("playerCount", "text").getString("");
        String favicon = node.node("favicon").getString("");
        NullBool hidePlayers = resolveNullBool(node, "playerCount", "hidePlayers");
        NullBool extraPlayersEnabled = resolveNullBool(node, "playerCount", "extraPlayers", "enabled");
        NullBool maxPlayersEnabled = resolveNullBool(node, "playerCount", "maxPlayers", "enabled");
        Integer extraPlayers = resolveNullableInt(node, "playerCount", "extraPlayers", "amount");
        Integer maxPlayers = resolveNullableInt(node, "playerCount", "maxPlayers", "amount");
        
        return new ProfileEntry.Builder()
            .setMotd(motd)
            .setPlayers(players)
            .setPlayerCountText(playerCountText)
            .setFavicon(favicon)
            .setHidePlayersEnabled(hidePlayers)
            .setExtraPlayersEnabled(extraPlayersEnabled)
            .setMaxPlayersEnabled(maxPlayersEnabled)
            .setExtraPlayersCount(extraPlayers)
            .setMaxPlayersCount(maxPlayers)
            .build();
    }
    
    public static boolean checkOption(Object obj){
        if(obj == null)
            return false;
        
        if(obj instanceof List<?> list){
            return !list.isEmpty(); // Check if list isn't empty
        }else
        if(obj instanceof String str){
            return !str.isEmpty(); // Check if String is not empty
        }else
        if(obj instanceof NullBool nb){
            return nb.getOrDefault(false); // Return NullBool's value
        }
        
        return false;
    }
    
    private static List<String> resolveMOTD(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.motd()))
            return defaultProfile.motd();
        
        return profile.motd();
    }
    
    private static List<String> resolvePlayers(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.players()))
            return defaultProfile.players();
        
        return profile.players();
    }
    
    private static String resolvePlayerCountText(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.playerCountText()))
            return defaultProfile.playerCountText();
        
        return profile.playerCountText();
    }
    
    private static String resolveFavicon(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.favicon()))
            return defaultProfile.favicon();
        
        return profile.favicon();
    }
    
    private static boolean resolveHidePlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.hidePlayersEnabled()))
            return defaultProfile.hidePlayersEnabled().getOrDefault(false);
        
        return profile.hidePlayersEnabled().getOrDefault(false);
    }
    
    private static boolean resolveExtraPlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.extraPlayersEnabled()))
            return defaultProfile.extraPlayersEnabled().getOrDefault(false);
        
        return profile.extraPlayersEnabled().getOrDefault(false);
    }
    
    private static boolean resolveMaxPlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || !checkOption(profile.maxPlayersEnabled()))
            return defaultProfile.maxPlayersEnabled().getOrDefault(false);
        
        return profile.maxPlayersEnabled().getOrDefault(false);
    }
    
    private static Integer resolveExtraPlayersCount(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.extraPlayersCount() == null)
            return defaultProfile.extraPlayersCount() == null ? 0 : defaultProfile.extraPlayersCount();
        
        return profile.extraPlayersCount();
    }
    
    private static Integer resolveMaxPlayersCount(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.maxPlayersCount() == null)
            return defaultProfile.maxPlayersCount() == null ? 0 : defaultProfile.maxPlayersCount();
        
        return profile.maxPlayersCount();
    }
    
    private static List<String> resolveList(ConfigurationNode node, Object... path){
        try{
            return node.node(path).getList(String.class);
        }catch(SerializationException ex){
            return Collections.emptyList();
        }
    }
    
    private static NullBool resolveNullBool(ConfigurationNode node, Object... path){
        if(node.node(path).virtual())
            return NullBool.NOT_SET;
        
        return NullBool.resolve(node.node(path).getBoolean());
    }
    
    private static Integer resolveNullableInt(ConfigurationNode node, Object... path){
        if(node.node(path).virtual())
            return null;
        
        return node.node(path).getInt();
    }
}
