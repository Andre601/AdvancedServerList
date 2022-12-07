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

import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.players.GenericPlayer;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;
import ch.andre601.advancedserverlist.paper.PaperCore;
import ch.andre601.advancedserverlist.paper.PaperPlayer;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.CachedServerIcon;

import java.util.List;

public class PaperEventWrapper implements GenericEventWrapper<OfflinePlayer, CachedServerIcon>{
    
    private final PaperCore plugin;
    private final PaperServerListPingEvent event;
    
    public PaperEventWrapper(PaperCore plugin, PaperServerListPingEvent event){
        this.plugin = plugin;
        this.event = event;
    }
    
    @Override
    public void setMaxPlayers(int maxPlayers){
        event.setMaxPlayers(maxPlayers);
    }
    
    @Override
    public void setMotd(Component component){
        event.motd(component);
    }
    
    @Override
    public void hidePlayers(){
        event.setHidePlayers(true);
    }
    
    @Override
    public void setPlayerCount(String name){
        event.setVersion(name);
        event.setProtocolVersion(-1);
    }
    
    @Override
    public void setPlayers(List<String> players, GenericPlayer<OfflinePlayer> player, PlayerPlaceholders playerPlaceholders, ServerPlaceholders serverPlaceholders){
        event.getPlayerSample().clear();
        
        event.getPlayerSample().addAll(
            plugin.createPlayers(players, player.getPlayer(), playerPlaceholders, serverPlaceholders)
        );
    }
    
    @Override
    public void setFavicon(String favicon){
        CachedServerIcon fav = plugin.getFaviconHandler().getFavicon(favicon, image -> {
            try{
                return Bukkit.loadServerIcon(image);
            }catch(Exception ex){
                return null;
            }
        });
        
        if(fav == null){
            plugin.getPluginLogger().warn("Cannot apply valid favicon. See previous messages for reasons");
            event.setServerIcon(event.getServerIcon());
        }else{
            event.setServerIcon(fav);
        }
    }
    
    // Not used in Paper
    @Override
    public void updateEvent(){}
    
    // Not used in Paper
    @Override
    public boolean isInvalidProtocol(){
        return false;
    }
    
    @Override
    public int getProtocolVersion(){
        return event.getClient().getProtocolVersion();
    }
    
    @Override
    public int getOnlinePlayers(){
        return event.getNumPlayers();
    }
    
    @Override
    public int getMaxPlayers(){
        return event.getMaxPlayers();
    }
    
    @Override
    public String getPlayerIP(){
        return event.getClient().getAddress().getHostString();
    }
    
    @Override
    public String parsePAPIPlaceholders(String text, GenericPlayer<OfflinePlayer> player){
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
        
        return text;
    }
    
    @Override
    public String getVirtualHost(){
        return this.resolveHost(event.getClient().getVirtualHost());
    }
    
    @Override
    public PluginCore<CachedServerIcon> getPlugin(){
        return plugin;
    }
    
    @Override
    public GenericPlayer<OfflinePlayer> createPlayer(String name, int protocol){
        OfflinePlayer player = Bukkit.getPlayerExact(name);
        
        if(player == null){
            player = Bukkit.getOfflinePlayer(name);
            
            return new PaperPlayer(player.hasPlayedBefore() ? player : null, name, protocol);
        }
        
        return new PaperPlayer(player, name, protocol);
    }
}
