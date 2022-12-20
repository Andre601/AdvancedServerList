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

package ch.andre601.advancedserverlist.bungeecord.events;

import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.bungeecord.objects.BungeePlayer;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.util.List;

public class BungeeEventWrapper implements GenericEventWrapper<Favicon, ServerPing.PlayerInfo, BungeePlayer>{
    
    private final BungeeCordCore plugin;
    private final ProxyPingEvent event;
    private final ServerPing.Protocol protocol;
    private final ServerPing ping;
    
    public BungeeEventWrapper(BungeeCordCore plugin, ProxyPingEvent event){
        this.plugin = plugin;
        this.event = event;
        this.protocol = event.getResponse().getVersion();
        this.ping = event.getResponse();
    }
    
    @Override
    public void setMaxPlayers(int maxPlayers){
        ping.getPlayers().setMax(maxPlayers);
    }
    
    @Override
    public void setMotd(Component component){
        ping.setDescriptionComponent(new TextComponent(BungeeComponentSerializer.get().serialize(component)));
    }
    
    @Override
    public void hidePlayers(){
        ping.setPlayers(null);
    }
    
    @Override
    public void setPlayerCount(String name){
        protocol.setName(name);
        protocol.setProtocol(-1);
    }
    
    @Override
    public void setPlayers(List<String> players, BungeePlayer player, GenericServer server){
        ServerPing.PlayerInfo[] playerInfos = plugin.createPlayers(players, player, server).toArray(new ServerPing.PlayerInfo[0]);
        
        if(playerInfos.length > 0)
            ping.getPlayers().setSample(playerInfos);
    }
    
    @Override
    public void setFavicon(Favicon favicon){
        ping.setFavicon(favicon);
    }
    
    @Override
    public void setDefaultFavicon(){
        ping.setFavicon(ping.getFaviconObject());
    }
    
    @Override
    public void updateEvent(){
        this.ping.setVersion(this.protocol);
        this.event.setResponse(this.ping);
    }
    
    @Override
    public boolean isInvalidProtocol(){
        return protocol == null;
    }
    
    @Override
    public int getProtocolVersion(){
        return this.protocol.getProtocol();
    }
    
    @Override
    public int getOnlinePlayers(){
        return ping.getPlayers().getOnline();
    }
    
    @Override
    public int getMaxPlayers(){
        return ping.getPlayers().getMax();
    }
    
    @Override
    public String getPlayerIP(){
        return ((InetSocketAddress)event.getConnection().getSocketAddress()).getHostString();
    }
    
    @Override
    public String parsePAPIPlaceholders(String text, BungeePlayer player){
        return text;
    }
    
    @Override
    public String getVirtualHost(){
        return this.resolveHost(event.getConnection().getVirtualHost());
    }
    
    @Override
    public PluginCore<Favicon, ServerPing.PlayerInfo, BungeePlayer> getPlugin(){
        return plugin;
    }
    
    @Override
    public BungeePlayer createPlayer(String name, int protocol){
        return new BungeePlayer(name, protocol);
    }
    
    @Override
    public Favicon createFavicon(BufferedImage image) throws IllegalArgumentException{
        return Favicon.create(image);
    }
}