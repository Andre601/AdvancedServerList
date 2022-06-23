package ch.andre601.advancedserverlist.core.interfaces;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.commands.CommandHandler;

import java.nio.file.Path;

public interface PluginCore{
    
    void loadCommands();
    
    void loadEvents();
    
    void loadMetrics();
    
    AdvancedServerList getCore();
    
    Path getPath();
    
    ProxyLogger getProxyLogger();
    
    String getProxyName();
    
    String getProxyVersion();
}
