package ch.andre601.advancedserverlist.bungeecord;

import ch.andre601.advancedserverlist.bungeecord.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.bungeecord.events.JoinEvent;
import ch.andre601.advancedserverlist.bungeecord.events.PingEvent;
import ch.andre601.advancedserverlist.bungeecord.logging.BungeeLogger;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.nio.file.Path;

public class BungeeCordCore extends Plugin implements PluginCore{
    
    private AdvancedServerList core;
    private final PluginLogger logger = new BungeeLogger(getLogger());
    
    @Override
    public void onEnable(){
        this.core = new AdvancedServerList(this);
    }
    
    @Override
    public void onDisable(){
        core.disable();
    }
    
    @Override
    public void loadCommands(){
        getProxy().getPluginManager().registerCommand(this, new CmdAdvancedServerList(this));
    }
    
    @Override
    public void loadEvents(){
        new JoinEvent(this);
        new PingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15585);
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
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public String getPlatformName(){
        return getProxy().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getProxy().getVersion();
    }
}
