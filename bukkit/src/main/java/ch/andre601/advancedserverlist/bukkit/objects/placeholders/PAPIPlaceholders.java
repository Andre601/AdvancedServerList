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

package ch.andre601.advancedserverlist.bukkit.objects.placeholders;

import ch.andre601.advancedserverlist.api.bukkit.objects.BukkitServer;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.bukkit.BukkitCore;
import ch.andre601.advancedserverlist.bukkit.objects.impl.BukkitPlayerImpl;
import ch.andre601.advancedserverlist.bukkit.objects.impl.BukkitServerImpl;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.spigot.SpigotCore;
import ch.andre601.advancedserverlist.spigot.listeners.ProtocolLibEvents;
import com.comphenix.protocol.ProtocolLibrary;
import com.viaversion.viaversion.api.Via;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.*;

public class PAPIPlaceholders<F> extends PlaceholderExpansion{
    
    private final BukkitCore<F> plugin;
    
    private PAPIPlaceholders(BukkitCore<F> plugin){
        this.plugin = plugin;
        this.register();
    }
    
    public static <F> PAPIPlaceholders<F> init(BukkitCore<F> plugin){
        return new PAPIPlaceholders<>(plugin);
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
        InetSocketAddress address = pl.getAddress();
        
        String host = resolveVirtualHost(pl, address);
        CachedPlayer cached = plugin.getCore().getPlayerHandler().getCachedPlayer(pl.getUniqueId());
        
        int protocol = resolveProtocol(pl);
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();
        BukkitServer server = new BukkitServerImpl(plugin.getWorldCache().worlds(), online, max, host);
        GenericPlayer player = new BukkitPlayerImpl(pl, cached, protocol);
        
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
        
        BukkitServer finalServer = new BukkitServerImpl(plugin.getWorldCache().worlds(), online, max, host);
        
        return switch(identifier.toLowerCase(Locale.ROOT)){
            case "motd" -> getOption(entry.motd(), pl, player, finalServer);
            case "favicon" -> getOption(entry.favicon(), pl, player, finalServer);
            case "playercount_hover" -> getOption(entry.players(), pl, player, finalServer);
            case "playercount_text" -> getOption(entry.playerCountText(), pl, player, finalServer);
            case "extra_players_max" -> extraPlayers == null ? "null" : String.valueOf(extraPlayers);
            case "players_max" -> maxPlayers == null ? "null" : String.valueOf(max);
            case "players_max_modified" -> String.valueOf(max);
            default -> null;
        };
    }
    
    private String getOption(String str, Player pl, GenericPlayer player, GenericServer server){
        return getOption(Collections.singletonList(str), pl, player, server);
    }
    
    private String getOption(List<String> list, Player pl, GenericPlayer player, GenericServer server){
        return ComponentParser.list(list)
            .modifyText(text -> PlaceholderAPI.setPlaceholders(pl, text))
            .modifyText(text -> StringReplacer.replace(text, player, server))
            .toString();
    }
    
    private int resolveProtocol(Player player){
        if(Bukkit.getPluginManager().isPluginEnabled("ViaVersion"))
            return Via.getAPI().getPlayerVersion(player.getUniqueId());
        
        // Since Spigot version requires ProtocolLib can we use it here safely if the plugin instance is SpigotCore.
        if(plugin instanceof SpigotCore)
            return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
        
        // getProtocolVersion is only in Paper, so this is only called when main class isn't SpigotCore.
        // Also, ViaVersion is not present, so the player protocol *should* be the same as the server.
        return Bukkit.getUnsafe().getProtocolVersion();
    }
    
    private String resolveVirtualHost(Player player, InetSocketAddress address){
        try {
            // Server is a Paper Server with Player#getVirtualHost()
            Class.forName("com.destroystokyo.paper.network.NetworkClient");
            return player.getVirtualHost() == null ? null : player.getVirtualHost().getHostString();
        }catch(ClassNotFoundException ignored){}
        
        if(address == null)
            return null;
        
        return ProtocolLibEvents.getHostAddresses().get(address.getHostString());
    }
}
