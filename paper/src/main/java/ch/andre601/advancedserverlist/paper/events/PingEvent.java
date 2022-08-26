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
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.Placeholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;
import ch.andre601.advancedserverlist.paper.PaperCore;
import ch.andre601.advancedserverlist.paper.PaperPlayer;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
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
        InetSocketAddress host = event.getClient().getVirtualHost();
    
        PaperPlayer player = resolvePlayer(address, event.getClient().getProtocolVersion());
        
        int online = event.getNumPlayers();
        int max = event.getMaxPlayers();
        
        PlayerPlaceholders playerPlaceholders = new PlayerPlaceholders(player);
        ServerPlaceholders serverPlaceholders = new ServerPlaceholders(online, max,  host == null ? null : host.getHostString());
        
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(playerPlaceholders)
            .replacements(serverPlaceholders)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(profile.getXMore() >= 0){
            max = online + profile.getXMore();
            event.setMaxPlayers(max);
        }
        
        serverPlaceholders = new ServerPlaceholders(online, max,  host == null ? null : host.getHostString());
        
        if(!profile.getMotd().isEmpty()){
            event.motd(ComponentParser.list(profile.getMotd())
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
                .modifyText(text -> {
                    if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
                    
                    return text;
                })
                .toComponent());
        }
        
        if(profile.shouldHidePlayers()){
            event.setHidePlayers(true);
            return;
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            event.setVersion(ComponentParser.text(profile.getPlayerCount())
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
                .modifyText(text -> {
                    if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
    
                    return text;
                })
                .toString());
            event.setProtocolVersion(-1);
        }
        
        if(!profile.getPlayers().isEmpty()){
            event.getPlayerSample().clear();
            
            event.getPlayerSample().addAll(getPlayers(profile.getPlayers(), playerPlaceholders, serverPlaceholders, player.getPlayer()));
        }
        
        if(!profile.getFavicon().isEmpty()){
            String favName = StringReplacer.replace(profile.getFavicon(), playerPlaceholders.getReplacements());
            
            CachedServerIcon favicon = plugin.getFaviconHandler().getFavicon(favName, image -> {
                try{
                    return Bukkit.loadServerIcon(image);
                }catch(Exception ex){
                    return null;
                }
            });
            
            if(favicon == null){
                plugin.getPluginLogger().warn("Could not obtain valid Favicon to use.");
                event.setServerIcon(event.getServerIcon());
            }else{
                event.setServerIcon(favicon);
            }
        }
    }
    
    private List<PlayerProfile> getPlayers(List<String> lines, Placeholders playerPlaceholders, Placeholders serverPlaceholders, OfflinePlayer player){
        List<PlayerProfile> players = new ArrayList<>();
        lines.forEach(line -> players.add(Bukkit.createProfile(UUID.randomUUID(), ComponentParser.text(line)
            .replacements(playerPlaceholders)
            .replacements(serverPlaceholders)
            .modifyText(text -> {
                if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                    return PlaceholderAPI.setPlaceholders(player, text);
    
                return text;
            })
            .toString())));
        
        return players;
    }
    
    private PaperPlayer resolvePlayer(InetSocketAddress address, int protocol){
        String playerName = plugin.getCore().getPlayerHandler().getPlayerByIp(address.getHostString());
        OfflinePlayer player = Bukkit.getPlayerExact(playerName);
        
        if(player == null){
            player = Bukkit.getOfflinePlayer(playerName);
            
            return new PaperPlayer(player.hasPlayedBefore() ? player : null, playerName, protocol);
        }
        
        return new PaperPlayer(player, playerName, protocol);
    }
}
