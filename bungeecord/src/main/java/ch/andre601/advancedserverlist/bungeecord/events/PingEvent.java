package ch.andre601.advancedserverlist.bungeecord.events;

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;

public class PingEvent implements Listener{
    
    private final BungeeCordCore plugin;
    
    public PingEvent(BungeeCordCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Protocol protocol = ping.getVersion();
        if(protocol == null)
            return;
    
        ServerListProfile profile = plugin.getCore().getServerListProfile(protocol.getProtocol());
        if(!profile.getMotd().isEmpty()){
            List<String> motd = profile.getMotd();
            if(motd.size() > 2)
                motd = motd.subList(0, 2);
    
            TextComponent component = new TextComponent(BungeeComponentSerializer.get().serialize(
                ComponentParser.toComponent(motd)
            ));
            
            ping.setDescriptionComponent(component);
        }
        
        if(!profile.getPlayerCount().isEmpty()){
            protocol.setName(ComponentParser.toString(profile.getPlayerCount()));
            protocol.setProtocol(-1);
        }
        
        if(!profile.getPlayers().isEmpty()){
            ServerPing.PlayerInfo[] players = AdvancedServerList.getPlayers(ServerPing.PlayerInfo.class, profile.getPlayers())
                .toArray(new ServerPing.PlayerInfo[0]);
            if(players.length > 0)
                ping.getPlayers().setSample(players);
            
        }
        
        ping.setFavicon(ping.getFaviconObject());
        ping.setVersion(protocol);
        
        event.setResponse(ping);
    }
}
