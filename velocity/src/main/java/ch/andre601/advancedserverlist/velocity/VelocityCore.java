package ch.andre601.advancedserverlist.velocity;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.velocity.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.velocity.events.JoinEvent;
import ch.andre601.advancedserverlist.velocity.events.PingEvent;
import ch.andre601.advancedserverlist.velocity.logging.VelocityLogger;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bstats.velocity.Metrics;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class VelocityCore implements PluginCore{
    
    private final PluginLogger logger;
    private final ProxyServer proxy;
    private final Path path;
    private final Metrics.Factory metrics;
    
    private AdvancedServerList core;
    
    @Inject
    public VelocityCore(ProxyServer proxy, @DataDirectory Path path, Metrics.Factory metrics){
        this.logger = new VelocityLogger(LoggerFactory.getLogger("AdvancedServerList"));
        
        this.proxy = proxy;
        this.path = path;
        this.metrics = metrics;
    }
    
    @Subscribe
    public void init(ProxyInitializeEvent event){
        this.core = new AdvancedServerList(this);
    }
    
    @Subscribe
    public void pluginDisable(ProxyShutdownEvent event){
        core.disable();
    }
    
    @Override
    public void loadCommands(){
        CommandMeta command = getProxy().getCommandManager()
            .metaBuilder("advancedserverlist")
            .aliases("asl")
            .build();
        
        getProxy().getCommandManager().register(command, new CmdAdvancedServerList(this));
    }
    
    @Override
    public void loadEvents(){
        new JoinEvent(this);
        new PingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        metrics.make(this, 15587);
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getPath(){
        return path;
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public String getPlatformName(){
        return getProxy().getVersion().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getProxy().getVersion().getVersion();
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
}
