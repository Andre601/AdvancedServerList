package ch.andre601.advancedserverlist.core.profiles;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

import java.util.HashMap;
import java.util.Map;

public class ProfileManager{
    
    private final AdvancedServerList core;
    
    private Map<String, Object> replacements = new HashMap<>();
    
    private ProfileManager(AdvancedServerList core){
        this.core = core;
    }
    
    public static ProfileManager get(AdvancedServerList core){
        return new ProfileManager(core);
    }
    
    public ProfileManager replace(String from, Object to){
        replacements.put(from, to);
        return this;
    }
    
    public ProfileManager replacements(Map<String, Object> replacements){
        this.replacements = replacements;
        return this;
    }
    
    public ServerListProfile getProfile(){
        for(ServerListProfile profile : core.getFileHandler().getProfiles()){
            if(profile.getMotd().isEmpty() && profile.getPlayers().isEmpty() && profile.getPlayerCount().isEmpty())
                continue;
    
            ConditionsHolder conditions = profile.getConditions();
            replacements.forEach(conditions::replace);
            if(conditions.eval(core.getPluginLogger()))
                return profile;
        }
        
        return null;
    }
}
