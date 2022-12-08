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

package ch.andre601.advancedserverlist.velocity.events;

import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.profiles.players.GenericPlayer;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.PlayerPlaceholders;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.ServerPlaceholders;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import ch.andre601.advancedserverlist.velocity.VelocityPlayer;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.Component;

import java.awt.image.BufferedImage;
import java.util.List;

public class VelocityEventWrapper implements GenericEventWrapper<Player, Favicon>{
    
    private final VelocityCore plugin;
    private final ProxyPingEvent event;
    private final ServerPing.Builder builder;
    private final ServerPing.Version protocol;
    
    public VelocityEventWrapper(VelocityCore plugin, ProxyPingEvent event){
        this.plugin = plugin;
        this.event = event;
        this.builder = event.getPing().asBuilder();
        this.protocol = event.getPing().getVersion();
    }
    
    @Override
    public void setMaxPlayers(int maxPlayers){
        builder.maximumPlayers(maxPlayers);
    }
    
    @Override
    public void setMotd(Component component){
        builder.description(component);
    }
    
    @Override
    public void hidePlayers(){
        builder.nullPlayers();
    }
    
    @Override
    public void setPlayerCount(String name){
        builder.version(new ServerPing.Version(-1, name));
    }
    
    @Override
    public void setPlayers(List<String> players, GenericPlayer<Player> player, PlayerPlaceholders playerPlaceholders, ServerPlaceholders serverPlaceholders){
        ServerPing.SamplePlayer[] playerSamples = plugin.createPlayers(players, playerPlaceholders, serverPlaceholders)
            .toArray(new ServerPing.SamplePlayer[0]);
        
        if(playerSamples.length > 0)
            builder.clearSamplePlayers().samplePlayers(playerSamples);
    }
    
    @Override
    public void setFavicon(Favicon favicon){
        builder.favicon(favicon);
    }
    
    // Not used in Velocity
    @Override
    public void setDefaultFavicon(){}
    
    @Override
    public void updateEvent(){
        event.setPing(builder.build());
    }
    
    @Override
    public boolean isInvalidProtocol(){
        return protocol == null;
    }
    
    @Override
    public int getProtocolVersion(){
        return protocol.getProtocol();
    }
    
    @Override
    public int getOnlinePlayers(){
        return builder.getOnlinePlayers();
    }
    
    @Override
    public int getMaxPlayers(){
        return builder.getMaximumPlayers();
    }
    
    @Override
    public String getPlayerIP(){
        return event.getConnection().getRemoteAddress().getHostString();
    }
    
    @Override
    public String parsePAPIPlaceholders(String text, GenericPlayer<Player> player){
        return text;
    }
    
    @Override
    public String getVirtualHost(){
        return this.resolveHost(event.getConnection().getVirtualHost().orElse(null));
    }
    
    @Override
    public PluginCore<Favicon> getPlugin(){
        return plugin;
    }
    
    @Override
    public GenericPlayer<Player> createPlayer(String name, int protocol){
        return new VelocityPlayer(name, protocol);
    }
    
    @Override
    public Favicon createFavicon(BufferedImage image){
        return Favicon.create(image);
    }
}
