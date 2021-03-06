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
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

import java.net.InetSocketAddress;
import java.util.*;

public class PingEvent implements Listener{
    
    private final SpigotCore plugin;
    private final ProtocolManager protocolManager;
    
    public PingEvent(SpigotCore plugin, ProtocolManager protocolManager){
        this.plugin = plugin;
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
                
                OfflinePlayer player = resolvePlayer(address);
                
                if(!profile.getMotd().isEmpty()){
                    ping.setMotD(ComponentParser.list(profile.getMotd())
                        .replacements(replacements)
                        .function(text -> {
                            if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                                return PlaceholderAPI.setPlaceholders(player, text);
    
                            return text;
                        })
                        .toString()
                    );
                }
                
                if(!profile.getPlayerCount().isEmpty()){
                    ping.setVersionName(ComponentParser.text(profile.getPlayerCount())
                        .replacements(replacements)
                        .function(text -> {
                            if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                                return PlaceholderAPI.setPlaceholders(player, text);
    
                            return text;
                        })
                        .toString()
                    );
                    ping.setVersionProtocol(-1);
                }
                
                if(!profile.getPlayers().isEmpty()){
                    ping.setPlayers(getFakePlayers(
                        ComponentParser.list(profile.getPlayers())
                            .replacements(replacements)
                            .function(text -> {
                                if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                                    return PlaceholderAPI.setPlaceholders(player, text);
    
                                return text;
                            })
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
    
    private OfflinePlayer resolvePlayer(InetSocketAddress address){
        String playerName = plugin.getCore().getPlayerHandler().getPlayerByIp(address.getHostString());
        OfflinePlayer player = Bukkit.getPlayerExact(playerName);
        
        if(player == null){
            //noinspection deprecation
            player = Bukkit.getOfflinePlayer(playerName);
            
            return player.hasPlayedBefore() ? player : null;
        }
        
        return player;
    }
}
