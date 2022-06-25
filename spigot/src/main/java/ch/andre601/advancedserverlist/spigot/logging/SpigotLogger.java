package ch.andre601.advancedserverlist.spigot.logging;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpigotLogger implements PluginLogger{
    
    private final Logger logger;
    
    public SpigotLogger(Logger logger){
        this.logger = logger;
    }
    
    @Override
    public void info(String msg){
        logger.info(msg);
    }
    
    @Override
    public void warn(String msg){
        logger.warning(msg);
    }
    
    @Override
    public void warn(String msg, Throwable throwable){
        logger.log(Level.WARNING, msg, throwable);
    }
}
