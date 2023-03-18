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

package ch.andre601.advancedserverlist.paper.objects;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.objects.GenericServerImpl;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.paper.PaperCore;
import com.viaversion.viaversion.api.Via;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        return String.join(", ", plugin.getDescription().getAuthors());
    }
    
    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public String onPlaceholderRequest(Player pl, @NotNull String identifier){
        String host = pl.getVirtualHost() == null ? null : pl.getVirtualHost().getHostString();
        CachedPlayer cached = plugin.getCore().getPlayerHandler().getCachedPlayer(pl.getUniqueId());
        
        // Check if ViaVersion is enabled to resolve the player's protocol version.
        // If not can we assume the client's version to be the same as the server's.
        int protocol;
        if(Bukkit.getPluginManager().isPluginEnabled("ViaVersion")){
            protocol = Via.getAPI().getPlayerVersion(pl.getUniqueId());
        }else{
            protocol = Bukkit.getUnsafe().getProtocolVersion();
        }
        
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();
        GenericServer server = new GenericServerImpl(online, max, host);
        GenericPlayer player = new PaperPlayerImpl(pl, cached, protocol);
        
        ServerListProfile profile = ProfileManager.resolveProfile(plugin.getCore(), player, server);
        if(profile == null)
            return null;
    
        ProfileEntry entry = ProfileManager.merge(profile);
        
        if(ProfileManager.checkOption(entry.isExtraPlayersEnabled()))
            max = online + (entry.getExtraPlayersCount() == null ? 0 : entry.getExtraPlayersCount());
        
        GenericServer finalServer = new GenericServerImpl(online, max, host);
        
        return switch(identifier.toLowerCase(Locale.ROOT)){
            case "motd" -> ComponentParser.list(entry.getMotd())
                .modifyText(text -> PlaceholderAPI.setPlaceholders(pl, text))
                .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                .toString();
            case "favicon" -> ComponentParser.text(entry.getFavicon())
                .modifyText(text -> PlaceholderAPI.setPlaceholders(pl, text))
                .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                .toString();
            case "playercount_hover" -> ComponentParser.list(entry.getPlayers())
                .modifyText(text -> PlaceholderAPI.setPlaceholders(pl, text))
                .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                .toString();
            case "playercount_text" -> ComponentParser.text(entry.getPlayerCountText())
                .modifyText(text -> PlaceholderAPI.setPlaceholders(pl, text))
                .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                .toString();
            case "extra_players_max" -> String.valueOf(max);
            default -> null;
        };
    }
}
