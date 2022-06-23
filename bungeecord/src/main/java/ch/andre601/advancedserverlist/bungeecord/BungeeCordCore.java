package ch.andre601.advancedserverlist.bungeecord;

import ch.andre601.advancedserverlist.bungeecord.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.bungeecord.events.PingEvent;
import ch.andre601.advancedserverlist.bungeecord.logging.BungeeLogger;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.ProxyLogger;
import net.md_5.bungee.api.plugin.Plugin;

import java.nio.file.Path;

public class BungeeCordCore extends Plugin implements PluginCore{
    
    private AdvancedServerList core;
    private final ProxyLogger logger = new BungeeLogger(getLogger());
    
    @Override
    public void onEnable(){
        this.core = new AdvancedServerList(this);
    }
    
    @Override
    public void loadCommands(){
        getProxy().getPluginManager().registerCommand(this, new CmdAdvancedServerList(this));
    }
    
    @Override
    public void loadEvents(){
        new PingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getPath(){
        return getDataFolder().toPath();
    }
    
    @Override
    public ProxyLogger getProxyLogger(){
        return logger;
    }
    
    @Override
    public String getProxyName(){
        return getProxy().getName();
    }
    
    @Override
    public String getProxyVersion(){
        return getProxy().getVersion();
    }
}
