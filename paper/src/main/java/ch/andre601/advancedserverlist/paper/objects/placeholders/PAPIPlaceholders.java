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

package ch.andre601.advancedserverlist.paper.objects.placeholders;

import ch.andre601.advancedserverlist.api.bukkit.objects.BukkitPlayer;
import ch.andre601.advancedserverlist.api.bukkit.objects.BukkitServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.paper.PaperCore;
import ch.andre601.advancedserverlist.paper.objects.impl.PaperPlayerImpl;
import ch.andre601.advancedserverlist.paper.objects.impl.PaperServerImpl;
import com.viaversion.viaversion.api.Via;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PAPIPlaceholders extends PlaceholderExpansion{
    
    private final PaperCore plugin;
    
    public PAPIPlaceholders(PaperCore plugin){
        this.plugin = plugin;
        this.register();
    }
    
    @Override
    public @NotNull String getIdentifier(){
        return "asl";
    }
    
    @Override
    public @NotNull String getAuthor(){
        return "Andre601";
    }
    
    @Override
    public @NotNull String getVersion(){
        return plugin.getCore().getVersion();
    }
    
    @Override
    public boolean persist(){
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player pl, @NotNull String identifier){
        String host = pl.getVirtualHost() == null ? null : pl.getVirtualHost().getHostString();
        CachedPlayer cached = plugin.getCore().getPlayerHandler().getCachedPlayer(pl.getUniqueId());
        
        int protocol = resolveProtocol(pl);
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();
        
        BukkitServer server = new PaperServerImpl(plugin.getWorldCache().worlds(), online, max, host);
        BukkitPlayer player = new PaperPlayerImpl(pl, cached, protocol);
        
        ServerListProfile profile = ProfileManager.resolveProfile(plugin.getCore(), player, server);
        if(profile == null)
            return null;
        
        ProfileEntry entry = ProfileManager.merge(profile);
        
        Integer maxPlayers = null;
        if(ProfileManager.checkOption(entry.maxPlayersEnabled())){
            max = entry.maxPlayersCount() == null ? 0 : entry.maxPlayersCount();
            maxPlayers = entry.maxPlayersCount();
        }
        
        Integer extraPlayers = null;
        if(ProfileManager.checkOption(entry.extraPlayersEnabled())){
            max = online + (entry.extraPlayersCount() == null ? 0 : entry.extraPlayersCount());
            extraPlayers = entry.extraPlayersCount();
        }
        
        BukkitServer finalServer = new PaperServerImpl(plugin.getWorldCache().worlds(), online, max, host);
        return switch(identifier.toLowerCase(Locale.ROOT)){
            case "motd" -> getOption(entry.motd(), pl, player, finalServer);
            case "favicon" -> getOption(entry.favicon(), pl, player, finalServer);
            case "playercount_hover" -> getOption(entry.players(), pl, player, finalServer);
            case "playercount_text" -> getOption(entry.playerCountText(), pl, player, finalServer);
            case "playercount_extraplayers" -> extraPlayers == null ? "null" : String.valueOf(extraPlayers);
            case "playercount_maxplayers" -> maxPlayers == null ? "null" : String.valueOf(maxPlayers);
            case "server_playersmax" -> String.valueOf(max);
            default -> null;
        };
    }
    
    private int resolveProtocol(Player player){
        if(Bukkit.getPluginManager().isPluginEnabled("ViaVersion"))
            return Via.getAPI().getPlayerVersion(player.getUniqueId());
        
        // Since ViaVersion isn't installed is the player's protocol version the same as the server.
        //noinspection deprecation
        return Bukkit.getUnsafe().getProtocolVersion();
    }
    
    private String getOption(String str, Player pl, BukkitPlayer player, BukkitServer server){
        return getOption(Collections.singletonList(str), pl, player, server);
    }
    
    private String getOption(List<String> list, Player pl, BukkitPlayer player, BukkitServer server){
        return ComponentParser.list(list)
            .modifyText(text -> PlaceholderAPI.setPlaceholders(pl, text))
            .modifyText(text -> StringReplacer.replace(text, player, server))
            .toString();
    }
}
