package ch.andre601.advancedserverlist.paper.events;

import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.paper.PaperCore;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PingEvent implements Listener{
    
    private final PaperCore plugin;
    
    public PingEvent(PaperCore plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onServerPing(PaperServerListPingEvent event){
        Map<String, Object> replacements = plugin.getCore()
            .loadPlaceholders(event.getProtocolVersion(), event.getNumPlayers(), event.getMaxPlayers(), event.getClient().getAddress());
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replace("{playerVersion}", event.getClient().getProtocolVersion())
            .getProfile();
        
        if(profile == null)
            return;
        
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            if(motd.size() > 2)
                motd = motd.subList(0, 2);
            
            event.motd(ComponentParser.list(motd).toComponent());
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            event.setVersion(ComponentParser.text(profile.getPlayerCount()).toString());
            event.setProtocolVersion(-1);
        }
        
        if(!profile.getPlayers().isEmpty()){
            event.getPlayerSample().clear();
            event.getPlayerSample().addAll(getPlayers(profile.getPlayers()));
        }
        
        event.setServerIcon(event.getServerIcon());
    }
    
    private List<PlayerProfile> getPlayers(List<String> lines){
        List<PlayerProfile> players = new ArrayList<>();
        lines.forEach(line -> players.add(Bukkit.createProfile(UUID.randomUUID(), ComponentParser.text(line).toString())));
        
        return players;
    }
}
