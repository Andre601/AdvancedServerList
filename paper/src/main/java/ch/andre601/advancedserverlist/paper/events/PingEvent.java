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

package ch.andre601.advancedserverlist.paper.events;

import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.paper.PaperCore;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.net.InetSocketAddress;
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
        InetSocketAddress address = event.getClient().getAddress();
        Map<String, Object> replacements = plugin.getCore()
            .loadPlaceholders(event.getClient().getProtocolVersion(), event.getNumPlayers(), event.getMaxPlayers(), address);
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(replacements)
            .getProfile();
        
        if(profile == null)
            return;
        
        OfflinePlayer player = resolvePlayer(address);
        
        if(!profile.getMotd().isEmpty()){
            event.motd(ComponentParser.list(profile.getMotd())
                .replacements(replacements)
                .function(text -> {
                    if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player, text);
                    
                    return text;
                })
                .toComponent());
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            event.setVersion(ComponentParser.text(profile.getPlayerCount())
                .replacements(replacements)
                .function(text -> {
                    if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player, text);
    
                    return text;
                })
                .toString());
            event.setProtocolVersion(-1);
        }
        
        if(!profile.getPlayers().isEmpty()){
            event.getPlayerSample().clear();
            event.getPlayerSample().addAll(getPlayers(profile.getPlayers(), replacements, player));
        }
        
        event.setServerIcon(event.getServerIcon());
    }
    
    private List<PlayerProfile> getPlayers(List<String> lines, Map<String, Object> replacements, OfflinePlayer player){
        List<PlayerProfile> players = new ArrayList<>();
        lines.forEach(line -> players.add(Bukkit.createProfile(UUID.randomUUID(), ComponentParser.text(line)
            .replacements(replacements)
            .function(text -> {
                plugin.getPluginLogger().info("Before: " + text);
                if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
                    String after = PlaceholderAPI.setPlaceholders(player, text);
                    
                    plugin.getPluginLogger().info("After: " + after);
                    return after;
                }
    
                return text;
            })
            .toString())));
        
        return players;
    }
    
    private OfflinePlayer resolvePlayer(InetSocketAddress address){
        String playerName = plugin.getCore().getPlayerHandler().getPlayerByIp(address.getHostString());
        OfflinePlayer player = Bukkit.getPlayerExact(playerName);
        
        if(player == null){
            player = Bukkit.getOfflinePlayer(playerName);
            
            return player.hasPlayedBefore() ? player : null;
        }
        
        return player;
    }
}
