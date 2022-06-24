package ch.andre601.advancedserverlist.velocity.logging;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import org.slf4j.Logger;

public class VelocityLogger implements PluginLogger{
    
    private final Logger logger;
    
    public VelocityLogger(Logger logger){
        this.logger = logger;
    }
    
    @Override
    public void info(String msg){
        logger.info(msg);
    }
    
    @Override
    public void warn(String msg){
        logger.warn(msg);
    }
    
    @Override
    public void warn(String msg, Throwable throwable){
        logger.warn(msg, throwable);
    }
}
