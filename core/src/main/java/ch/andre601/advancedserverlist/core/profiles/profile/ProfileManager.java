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
        ProfileEntry defEntry = profile.getDefaultProfile();
        
        List<String> motd = resolveMOTD(entry, defEntry);
        List<String> players = resolvePlayers(entry, defEntry);
        String playerCountText = resolvePlayerCountText(entry, defEntry);
        String favicon = resolveFavicon(entry, defEntry);
        boolean isHidePlayersEnabled = resolveHidePlayersEnabled(entry, defEntry);
        boolean isExtraPlayersEnabled = resolveExtraPlayersEnabled(entry, defEntry);
        Integer extraPlayersCount = resolveExtraPlayersCount(entry, defEntry);
        
        return new ProfileEntry(motd, players, playerCountText, favicon, 
            new NullBool(isHidePlayersEnabled), new NullBool(isExtraPlayersEnabled), extraPlayersCount);
    }
    
    public static ServerListProfile resolveProfile(AdvancedServerList core, GenericPlayer player, GenericServer server){
        for(ServerListProfile profile : core.getFileHandler().getProfiles()){
            if(profile.isInvalidProfile())
                continue;
            
            if(profile.evalConditions(player, server))
                return profile;
        }
        
        return null;
    }
    
    public static ProfileEntry retrieveProfileEntry(ConfigurationNode node){
        List<String> motd = resolveList(node, "motd");
        List<String> players = resolveList(node, "playerCount", "hover");
        String playerCountText = node.node("playerCount", "text").getString("");
        NullBool hidePlayers = resolveNullBool(node, "playerCount", "hidePlayers");
        NullBool extraPlayersEnabled = resolveNullBool(node, "playerCount", "extraPlayers", "enabled");
        Integer extraPlayers = resolveNullableInt(node);
        
        return new ProfileEntry.Builder()
            .setMotd(motd)
            .setPlayers(players)
            .setPlayerCountText(playerCountText)
            .setHidePlayersEnabled(hidePlayers)
            .setExtraPlayersEnabled(extraPlayersEnabled)
            .setExtraPlayerCount(extraPlayers)
            .build();
    }
    
    private static List<String> resolveMOTD(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getMotd().isEmpty())
            return defaultProfile.getMotd();
        
        return profile.getMotd();
    }
    
    private static List<String> resolvePlayers(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getPlayers().isEmpty())
            return defaultProfile.getPlayers();
        
        return profile.getPlayers();
    }
    
    private static String resolvePlayerCountText(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getPlayerCountText().isEmpty())
            return defaultProfile.getPlayerCountText();
        
        return profile.getPlayerCountText();
    }
    
    private static String resolveFavicon(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getFavicon().isEmpty())
            return defaultProfile.getFavicon();
        
        return profile.getFavicon();
    }
    
    private static boolean resolveHidePlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.isHidePlayersEnabled().isNull())
            return defaultProfile.isHidePlayersEnabled().getOrDefault(false);
        
        return profile.isHidePlayersEnabled().getOrDefault(false);
    }
    
    private static boolean resolveExtraPlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.isExtraPlayersEnabled().isNull())
            return defaultProfile.isExtraPlayersEnabled().getOrDefault(false);
        
        return profile.isExtraPlayersEnabled().getOrDefault(false);
    }
    
    private static Integer resolveExtraPlayersCount(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getExtraPlayersCount() == null)
            return defaultProfile.getExtraPlayersCount() == null ? 0 : defaultProfile.getExtraPlayersCount();
        
        return profile.getExtraPlayersCount();
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
            return NullBool.NULL;
        
        return new NullBool(node.node(path).getBoolean());
    }
    
    private static Integer resolveNullableInt(ConfigurationNode node){
        if(node.node("playerCount", "extraPlayers", "amount").virtual())
            return null;
        
        return node.node("playerCount", "extraPlayers", "amount").getInt();
    }
}
