/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ch.andre601.advancedserverlist.velocity;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.velocity.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.velocity.events.JoinEvent;
import ch.andre601.advancedserverlist.velocity.events.PingEvent;
import ch.andre601.advancedserverlist.velocity.logging.VelocityLogger;
import ch.andre601.advancedserverlist.velocity.objects.VelocityPlayerPlaceholders;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.Favicon;
import org.bstats.charts.SimplePie;
import org.bstats.velocity.Metrics;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class VelocityCore implements PluginCore<Favicon>{
    
    private final PluginLogger logger;
    private final ProxyServer proxy;
    private final Path path;
    private final Metrics.Factory metrics;
    
    private AdvancedServerList core;
    private FaviconHandler<Favicon> faviconHandler = null;
    
    @Inject
    public VelocityCore(ProxyServer proxy, @DataDirectory Path path, Metrics.Factory metrics){
        this.logger = new VelocityLogger(LoggerFactory.getLogger("AdvancedServerList"));
        
        this.proxy = proxy;
        this.path = path;
        this.metrics = metrics;
    }
    
    @Subscribe
    public void init(ProxyInitializeEvent event){
        this.core = new AdvancedServerList(this, new VelocityPlayerPlaceholders());
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
        metrics.make(this, 15587).addCustomChart(new SimplePie("profiles",
            () -> String.valueOf(core.getFileHandler().getProfiles().size())
        ));
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
    
        faviconHandler.clearCache();
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getFolderPath(){
        return path;
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public FaviconHandler<Favicon> getFaviconHandler(){
        if(faviconHandler == null)
            faviconHandler = new FaviconHandler<>(core);
        
        return faviconHandler;
    }
    
    @Override
    public String getPlatformName(){
        return getProxy().getVersion().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getProxy().getVersion().getVersion();
    }
    
    @Override
    public String getLoader(){
        return "velocity";
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
}
