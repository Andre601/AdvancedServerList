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

package ch.andre601.advancedserverlist.paper.events;

import ch.andre601.advancedserverlist.api.events.GenericProfileEntryEvent;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.paper.PaperCore;
import ch.andre601.advancedserverlist.paper.objects.PaperPlayer;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaperEventWrapper implements GenericEventWrapper<CachedServerIcon, PlayerProfile, PaperPlayer>{
    
    private final PaperCore plugin;
    private final PaperServerListPingEvent event;
    
    public PaperEventWrapper(PaperCore plugin, PaperServerListPingEvent event){
        this.plugin = plugin;
        this.event = event;
    }
    
    @Override
    public GenericProfileEntryEvent callEvent(ProfileEntry entry){
        PreServerListSetEvent event = new PreServerListSetEvent(entry);
        plugin.getServer().getPluginManager().callEvent(event);
        
        return event;
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
    public void setPlayers(List<String> lines, PaperPlayer player, GenericServer server){
        event.getPlayerSample().clear();
        List<PlayerProfile> players = new ArrayList<>(lines.size());
        
        for(String line : lines){
            String parsed = ComponentParser.text(line)
                .modifyText(text -> StringReplacer.replace(text, player, server))
                .modifyText(text -> {
                    if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
                    
                    return text;
                })
                .toString();
            
            players.add(Bukkit.createProfile(UUID.randomUUID(), parsed));
        }
        
        event.getPlayerSample().addAll(players);
    }
    
    @Override
    public void setFavicon(CachedServerIcon favicon){
        event.setServerIcon(favicon);
    }
    
    @Override
    public void setDefaultFavicon(){
        event.setServerIcon(event.getServerIcon());
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
    public String parsePAPIPlaceholders(String text, PaperPlayer player){
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
    public PaperPlayer createPlayer(CachedPlayer player, int protocol){
        OfflinePlayer pl = Bukkit.getOfflinePlayer(player.getUuid());
        
        return new PaperPlayer(pl.hasPlayedBefore() ? pl : null, player, protocol);
    }
    
    @Override
    public CachedServerIcon createFavicon(BufferedImage image) throws Exception{
        return Bukkit.loadServerIcon(image);
    }
}
