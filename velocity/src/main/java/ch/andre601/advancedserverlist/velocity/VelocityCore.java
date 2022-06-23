package ch.andre601.advancedserverlist.velocity;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.ProxyLogger;
import ch.andre601.advancedserverlist.velocity.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.velocity.events.PingEvent;
import ch.andre601.advancedserverlist.velocity.logging.VelocityLogger;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class VelocityCore implements PluginCore{
    
    private final ProxyLogger logger;
    private final ProxyServer proxy;
    private final Path path;
    
    private AdvancedServerList core;
    
    @Inject
    public VelocityCore(ProxyServer proxy, @DataDirectory Path path){
        this.logger = new VelocityLogger(LoggerFactory.getLogger("AdvancedServerList"));
        
        this.proxy = proxy;
        this.path = path;
    }
    
    @Subscribe
    public void init(ProxyInitializeEvent event){
        this.core = new AdvancedServerList(this);
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
        return path;
    }
    
    @Override
    public ProxyLogger getProxyLogger(){
        return logger;
    }
    
    @Override
    public String getProxyName(){
        return getProxy().getVersion().getName();
    }
    
    @Override
    public String getProxyVersion(){
        return getProxy().getVersion().getVersion();
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
}
