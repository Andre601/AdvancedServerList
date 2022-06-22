package ch.andre601.advancedserverlist.bungeecord.logging;

import ch.andre601.advancedserverlist.core.interfaces.ProxyLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BungeeLogger implements ProxyLogger{
    
    private final Logger logger;
    
    public BungeeLogger(Logger logger){
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
