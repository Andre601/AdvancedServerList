package ch.andre601.advancedserverlist.core.interfaces;

import ch.andre601.advancedserverlist.core.AdvancedServerList;

import java.nio.file.Path;

public interface PluginCore{
    
    void loadCommands();
    
    void loadEvents();
    
    void loadMetrics();
    
    AdvancedServerList getCore();
    
    Path getPath();
    
    PluginLogger getPluginLogger();
    
    String getPlatformName();
    
    String getPlatformVersion();
}
