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

package ch.andre601.advancedserverlist.bungeecord.listeners;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.bungeecord.objects.impl.BungeePlayerImpl;
import ch.andre601.advancedserverlist.bungeecord.objects.impl.BungeeProxyImpl;
import ch.andre601.advancedserverlist.core.events.PingEventHandler;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BungeeEventWrapper implements GenericEventWrapper<Favicon, BungeePlayerImpl>{
    
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
    public GenericServerListEvent callEvent(ProfileEntry entry){
        PreServerListSetEventImpl event = new PreServerListSetEventImpl(entry);
        plugin.getProxy().getPluginManager().callEvent(event);
        
        return event;
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
    public void setPlayers(List<String> lines, BungeePlayerImpl player, GenericServer server){
        ServerPing.PlayerInfo[] players = new ServerPing.PlayerInfo[lines.size()];
        
        for(int i = 0; i < players.length; i++){
            String parsed = ComponentParser.text(lines.get(i))
                .modifyText(text -> StringReplacer.replace(text, player, server))
                .toString();
    
            ServerPing.PlayerInfo pi = new ServerPing.PlayerInfo(parsed, UUID.randomUUID());
            players[i] = pi;
        }
        
        if(players.length > 0)
            ping.getPlayers().setSample(players);
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
    public boolean isMaintenanceModeActive(){
        if(plugin.getProxy().getPluginManager().getPlugin("Maintenance") == null)
            return false;
        
        return PingEventHandler.getMaintenanceUtil().isMaintenanceEnabled();
    }
    
    @Override
    public int getProtocolVersion(){
        return this.protocol.getProtocol();
    }
    
    @Override
    public int getOnlinePlayers(){
        return plugin.getOnlinePlayers(null);
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
    public String parsePAPIPlaceholders(String text, BungeePlayerImpl player){
        if(plugin.getProxy().getPluginManager().getPlugin("PAPIProxyBridge") == null)
            return text;
        
        if(!PingEventHandler.getPAPIUtil().isCompatible())
            return text;
        
        String server = PingEventHandler.getPAPIUtil().getServer();
        if(server == null || server.isEmpty())
            return text;
        
        ServerInfo serverInfo = plugin.getProxy().getServerInfo(server);
        if(serverInfo == null || serverInfo.getPlayers().isEmpty())
            return text;
        
        ProxiedPlayer carrier = PingEventHandler.getPAPIUtil().getPlayer(serverInfo.getPlayers());
        if(carrier == null)
            return text;
        
        return PingEventHandler.getPAPIUtil().parse(text, carrier.getUniqueId(), player.getUUID());
    }
    
    @Override
    public String getVirtualHost(){
        return this.resolveHost(event.getConnection().getVirtualHost());
    }
    
    @Override
    public PluginCore<Favicon> getPlugin(){
        return plugin;
    }
    
    @Override
    public BungeePlayerImpl createPlayer(CachedPlayer player, int protocol){
        return new BungeePlayerImpl(player, protocol);
    }
    
    @Override
    public GenericServer createGenericServer(int playersOnline, int playersMax, String host){
        Map<String, ServerInfo> servers = plugin.getProxy().getServers();
        
        return new BungeeProxyImpl(servers, playersOnline, playersMax, host);
    }
}
