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

package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.spigot.SpigotCore;
import ch.andre601.advancedserverlist.spigot.objects.SpigotPlayer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.AdventureComponentConverter;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProtocolLibEventWrapper implements GenericEventWrapper<WrappedServerPing.CompressedImage, SpigotPlayer>{
    
    private final SpigotCore plugin;
    private final PacketEvent event;
    private final WrappedServerPing ping;
    
    private final Map<String, String> hostAddresses;
    
    public ProtocolLibEventWrapper(SpigotCore plugin, PacketEvent event, Map<String, String> hostAddresses){
        this.plugin = plugin;
        this.event = event;
        this.ping = event.getPacket().getServerPings().read(0);
        
        this.hostAddresses = hostAddresses;
    }
    
    @Override
    public GenericServerListEvent callEvent(ProfileEntry entry){
        PreServerListSetEvent event = new PreServerListSetEvent(entry);
        plugin.getServer().getPluginManager().callEvent(event);
        
        return event;
    }
    
    @Override
    public void setMaxPlayers(int maxPlayers){
        ping.setPlayersMaximum(maxPlayers);
    }
    
    @Override
    public void setMotd(Component component){
        ping.setMotD(AdventureComponentConverter.fromComponent(component));
    }
    
    @Override
    public void hidePlayers(){
        ping.setPlayersVisible(false);
    }
    
    @Override
    public void setPlayerCount(String name){
        ping.setVersionName(name);
        ping.setVersionProtocol(-1);
    }
    
    @Override
    public void setPlayers(List<String> lines, SpigotPlayer player, GenericServer server){
        List<WrappedGameProfile> players = new ArrayList<>(lines.size());
        
        for(String line : lines){
            String parsed = ComponentParser.text(line)
                .modifyText(text -> StringReplacer.replace(text, player, server))
                .modifyText(text -> {
                    if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
                        return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
                    
                    return text;
                })
                .toString();
            
            players.add(new WrappedGameProfile(UUID.randomUUID(), parsed));
        }
        
        ping.setPlayers(players);
    }
    
    @Override
    public void setFavicon(WrappedServerPing.CompressedImage favicon){
        ping.setFavicon(favicon);
    }
    
    @Override
    public void setDefaultFavicon(){
        ping.setFavicon(ping.getFavicon());
    }
    
    // Not used in ProtocolLib
    @Override
    public void updateEvent(){}
    
    @Override
    public boolean isInvalidProtocol(){
        return event.getPlayer().getAddress() == null;
    }
    
    @Override
    public int getProtocolVersion(){
        return ping.getVersionProtocol();
    }
    
    @Override
    public int getOnlinePlayers(){
        return ping.getPlayersOnline();
    }
    
    @Override
    public int getMaxPlayers(){
        return ping.getPlayersMaximum();
    }
    
    @Override
    public String getPlayerIP(){
        return event.getPlayer().getAddress() == null ? "UNKNOWN" : event.getPlayer().getAddress().getHostString();
    }
    
    @Override
    public String parsePAPIPlaceholders(String text, SpigotPlayer player){
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            return PlaceholderAPI.setPlaceholders(player.getPlayer(), text);
        
        return text;
    }
    
    @Override
    public String getVirtualHost(){
        String host = this.resolveHost(event.getPlayer().getAddress());
        if(host == null)
            return null;
        
        return hostAddresses.get(host);
    }
    
    @Override
    public PluginCore<WrappedServerPing.CompressedImage> getPlugin(){
        return plugin;
    }
    
    @Override
    public SpigotPlayer createPlayer(CachedPlayer player, int protocol){
        OfflinePlayer pl = Bukkit.getOfflinePlayer(player.getUuid());
        
        return new SpigotPlayer(pl.hasPlayedBefore() ? pl : null, player, protocol);
    }
    
    @Override
    public WrappedServerPing.CompressedImage createFavicon(BufferedImage image) throws Exception{
        return WrappedServerPing.CompressedImage.fromPng(image);
    }
}
