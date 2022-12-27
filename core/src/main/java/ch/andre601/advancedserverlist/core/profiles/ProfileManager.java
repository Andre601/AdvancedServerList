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

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.Placeholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileManager{
    
    private final AdvancedServerList core;
    
    private final Map<String, Object> replacements = new HashMap<>();
    
    private ProfileManager(AdvancedServerList core){
        this.core = core;
    }
    
    public static ProfileManager get(AdvancedServerList core){
        return new ProfileManager(core);
    }
    
    public ProfileManager replacements(Placeholders placeholders){
        this.replacements.putAll(placeholders.getReplacements());
        return this;
    }
    
    public ServerListProfile getProfile(){
        for(ServerListProfile profile : core.getFileHandler().getProfiles()){
            if(profile.isInvalidProfile())
                continue;
            
            if(profile.evaluateConditions(replacements))
                return profile;
        }
        
        return null;
    }
    
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
        Boolean isHidePlayersEnabled = resolveHidePlayersEnabled(entry, defEntry);
        Boolean isExtraPlayersEnabled = resolveExtraPlayersEnabled(entry, defEntry);
        Integer extraPlayersCount = resolveExtraPlayersCount(entry, defEntry);
        
        return new ProfileEntry(motd, players, playerCountText, favicon, isHidePlayersEnabled, isExtraPlayersEnabled,
            extraPlayersCount);
    }
    
    private static List<String> resolveMOTD(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getMOTD().isEmpty())
            return defaultProfile.getMOTD();
        
        return profile.getMOTD();
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
    
    private static Boolean resolveHidePlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.isHidePlayersEnabled() == null)
            return defaultProfile.isHidePlayersEnabled();
        
        return profile.isHidePlayersEnabled();
    }
    
    private static Boolean resolveExtraPlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.isExtraPlayersEnabled() == null)
            return defaultProfile.isExtraPlayersEnabled();
        
        return profile.isExtraPlayersEnabled();
    }
    
    private static Integer resolveExtraPlayersCount(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getExtraPlayersCount() == null)
            return defaultProfile.getExtraPlayersCount();
        
        return profile.getExtraPlayersCount();
    }
    
}
