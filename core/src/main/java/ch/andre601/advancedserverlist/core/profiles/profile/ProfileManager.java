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
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.objects.NullBool;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;

import java.util.List;

public class ProfileManager{
    
    private final AdvancedServerList core;
    
    private GenericPlayer<?> player;
    private GenericServer server;
    
    private ProfileManager(AdvancedServerList core){
        this.core = core;
    }
    
    public static ProfileManager get(AdvancedServerList core){
        return new ProfileManager(core);
    }
    
    public <P extends GenericPlayer<?>> ProfileManager applyReplacements(P player, GenericServer server){
        this.player = player;
        this.server = server;
        
        return this;
    }
    
    public ServerListProfile getProfile(){
        for(ServerListProfile profile : core.getFileHandler().getProfiles()){
            if(profile.isInvalidProfile())
                continue;
            
            if(profile.evalConditions(player, server))
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
        boolean isHidePlayersEnabled = resolveHidePlayersEnabled(entry, defEntry);
        boolean isExtraPlayersEnabled = resolveExtraPlayersEnabled(entry, defEntry);
        Integer extraPlayersCount = resolveExtraPlayersCount(entry, defEntry);
        
        return new ProfileEntry(motd, players, playerCountText, favicon, 
            new NullBool(isHidePlayersEnabled), new NullBool(isExtraPlayersEnabled), extraPlayersCount);
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
    
    private static boolean resolveHidePlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.isHidePlayersEnabled().isNull())
            return defaultProfile.isHidePlayersEnabled().getValue(false);
        
        return profile.isHidePlayersEnabled().getValue(false);
    }
    
    private static boolean resolveExtraPlayersEnabled(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.isExtraPlayersEnabled().isNull())
            return defaultProfile.isExtraPlayersEnabled().getValue(false);
        
        return profile.isExtraPlayersEnabled().getValue(false);
    }
    
    private static Integer resolveExtraPlayersCount(ProfileEntry profile, ProfileEntry defaultProfile){
        if(profile == null || profile.getExtraPlayersCount() == null)
            return defaultProfile.getExtraPlayersCount() == null ? 0 : defaultProfile.getExtraPlayersCount();
        
        return profile.getExtraPlayersCount();
    }
    
}
