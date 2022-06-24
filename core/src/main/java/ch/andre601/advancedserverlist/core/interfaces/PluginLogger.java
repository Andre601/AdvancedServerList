package ch.andre601.advancedserverlist.core.interfaces;

public interface PluginLogger{
    
    void info(String msg);
    
    void warn(String msg);
    
    void warn(String msg, Throwable throwable);
}
