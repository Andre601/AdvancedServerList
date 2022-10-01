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

package ch.andre601.advancedserverlist.sponge.events;

import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;
import ch.andre601.advancedserverlist.sponge.SpongeCore;
import ch.andre601.advancedserverlist.sponge.SpongePlayer;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.network.status.StatusClient;
import org.spongepowered.api.network.status.StatusResponse;
import org.spongepowered.api.profile.GameProfile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class PingEvent implements EventListener<ClientPingServerEvent>{
    
    private final SpongeCore plugin;
    
    public PingEvent(SpongeCore plugin){
        this.plugin = plugin;
        Sponge.eventManager().registerListeners(plugin.getPluginContainer(), this);
    }
    
    @Override
    public void handle(ClientPingServerEvent event){
        if(event.isCancelled())
            return;
    
        ClientPingServerEvent.Response response = event.response();
        MinecraftVersion version = response.version();
        StatusClient client = event.client();
        
        String playerName = plugin.getCore().getPlayerHandler().getPlayerByIp(client.address().getHostString());
        InetSocketAddress host = client.virtualHost().orElse(null);
    
        StatusResponse.Players players = response.players().orElse(null);
        int online = players == null ? 0 : players.online();
        int max = players == null ? 0 : players.max();
    
        PlayerPlaceholders playerPlaceholders = new PlayerPlaceholders(new SpongePlayer(playerName, version.dataVersion().orElse(-1)));
        ServerPlaceholders serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
    
        ServerListProfile profile = ProfileManager.get(plugin.getCore())
            .replacements(playerPlaceholders)
            .replacements(serverPlaceholders)
            .getProfile();
        
        if(profile == null)
            return;
        
        if(profile.isExtraPlayersEnabled()){
            max = online + profile.getExtraPlayers();
        }
        
        serverPlaceholders = new ServerPlaceholders(online, max, host == null ? null : host.getHostString());
        
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            
            response.setDescription(ComponentParser.list(motd)
                .replacements(playerPlaceholders)
                .replacements(serverPlaceholders)
                .toComponent()
            );
        }
        
        if(profile.shouldHidePlayers()){
            response.setHidePlayers(true);
            return;
        }
        
        if(!profile.getPlayers().isEmpty()){
            List<GameProfile> playerSamples = plugin.createPlayers(profile.getPlayers(), null, playerPlaceholders, serverPlaceholders);
            
            if(playerSamples.size() > 0 && players != null){
                players.profiles().clear();
                players.profiles().addAll(playerSamples);
            }
        }
        
        if(!profile.getFavicon().isEmpty()){
            String favName = StringReplacer.replace(profile.getFavicon(), playerPlaceholders.getReplacements());
            
            Favicon favicon = plugin.getFaviconHandler().getFavicon(favName, image -> {
                try{
                    return Favicon.load(image);
                }catch(IOException ex){
                    return null;
                }
            });
            
            if(favicon == null){
                plugin.getPluginLogger().warn("Could not obtain valid Favicon to use.");
            }else{
                response.setFavicon(favicon);
            }
        }
    }
}
