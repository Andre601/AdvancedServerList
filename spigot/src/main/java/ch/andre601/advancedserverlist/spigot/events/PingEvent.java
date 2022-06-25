package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.spigot.SpigotCore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.event.Listener;

import java.net.InetSocketAddress;
import java.util.*;

public class PingEvent implements Listener{
    private final ProtocolManager protocolManager;
    
    public PingEvent(SpigotCore plugin, ProtocolManager protocolManager){
        this.protocolManager = protocolManager;
        
        loadPacketListener(plugin);
    }
    
    private void loadPacketListener(SpigotCore spigotPlugin){
        protocolManager.addPacketListener(new PacketAdapter(spigotPlugin, ListenerPriority.LOW, PacketType.Status.Server.SERVER_INFO){
            @Override
            public void onPacketSending(PacketEvent event){
                WrappedServerPing ping = event.getPacket().getServerPings().read(0);
                InetSocketAddress address = event.getPlayer().getAddress();
                if(address == null)
                    return;
                
                Map<String, Object> replacements = spigotPlugin.getCore()
                    .loadPlaceholders(ping.getVersionProtocol(), ping.getPlayersOnline(), ping.getPlayersMaximum(), address);
                
                ServerListProfile profile = ProfileManager.get(spigotPlugin.getCore())
                    .replacements(replacements)
                    .getProfile();
                
                if(profile == null)
                    return;
                
                if(!profile.getMotd().isEmpty()){
                    ping.setMotD(ComponentParser.list(profile.getMotd())
                        .replacements(replacements)
                        .toString()
                    );
                }
                
                if(!profile.getPlayerCount().isEmpty()){
                    ping.setVersionName(ComponentParser.text(profile.getPlayerCount())
                        .replacements(replacements)
                        .toString()
                    );
                    ping.setVersionProtocol(-1);
                }
                
                if(!profile.getPlayers().isEmpty()){
                    ping.setPlayers(getFakePlayers(
                        ComponentParser.list(profile.getPlayers())
                            .replacements(replacements)
                            .toString()
                    ));
                }
            }
        });
    }
    
    private List<WrappedGameProfile> getFakePlayers(String text){
        String[] lines = text.split("\n");
        List<WrappedGameProfile> profiles = new ArrayList<>();
        
        for(String line : lines){
            profiles.add(new WrappedGameProfile(UUID.randomUUID(), line));
        }
        
        return profiles;
    }
}
