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
import ch.andre601.advancedserverlist.velocity.listeners.JoinEvent;
import ch.andre601.advancedserverlist.velocity.listeners.PingEvent;
import ch.andre601.advancedserverlist.velocity.logging.VelocityLogger;
import ch.andre601.advancedserverlist.velocity.objects.placeholders.VelocityPlayerPlaceholders;
import ch.andre601.advancedserverlist.velocity.objects.placeholders.VelocityServerPlaceholders;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.Favicon;
import de.myzelyam.api.vanish.VelocityVanishAPI;
import org.bstats.charts.SimplePie;
import org.bstats.velocity.Metrics;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VelocityCore implements PluginCore<Favicon>{
    
    private final PluginLogger logger;
    private final ProxyServer proxy;
    private final Path path;
    private final Metrics.Factory metrics;
    
    private AdvancedServerList<Favicon> core;
    private FaviconHandler<Favicon> faviconHandler = null;
    
    @Inject
    public VelocityCore(ProxyServer proxy, @DataDirectory Path path, Metrics.Factory metrics){
        this.logger = new VelocityLogger(this, LoggerFactory.getLogger("AdvancedServerList"));
        
        this.proxy = proxy;
        this.path = path;
        this.metrics = metrics;
    }
    
    @Subscribe
    public void init(ProxyInitializeEvent event){
        this.core = AdvancedServerList.init(this, VelocityPlayerPlaceholders.init(), VelocityServerPlaceholders.init(this));
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
    public void loadFaviconHandler(AdvancedServerList<Favicon> core){
        faviconHandler = new FaviconHandler<>(core);
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
    
        faviconHandler.cleanCache();
    }
    
    @Override
    public AdvancedServerList<Favicon> getCore(){
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
    
    @Override
    public Favicon createFavicon(BufferedImage image){
        return Favicon.create(image);
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
    
    public int getOnlinePlayers(RegisteredServer server){
        if(getProxy() == null)
            return -1;
        
        List<Player> players = new ArrayList<>(server == null ? getProxy().getAllPlayers() : server.getPlayersConnected());
        
        // Exclude players when PremiumVanish is enabled and player is hidden.
        if(getProxy().getPluginManager().isLoaded("premiumvanish")){
            players.removeIf(VelocityVanishAPI::isInvisible);
        }
        
        return players.size();
    }
}
