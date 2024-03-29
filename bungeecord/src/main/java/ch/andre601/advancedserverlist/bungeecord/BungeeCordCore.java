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

package ch.andre601.advancedserverlist.bungeecord;

import ch.andre601.advancedserverlist.bungeecord.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.bungeecord.listeners.JoinEvent;
import ch.andre601.advancedserverlist.bungeecord.listeners.PingEvent;
import ch.andre601.advancedserverlist.bungeecord.logging.BungeeLogger;
import ch.andre601.advancedserverlist.bungeecord.objects.placeholders.BungeePlayerPlaceholders;
import ch.andre601.advancedserverlist.bungeecord.objects.placeholders.BungeeServerPlaceholders;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimplePie;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BungeeCordCore extends Plugin implements PluginCore<Favicon>{
    
    private AdvancedServerList<Favicon> core;
    private FaviconHandler<Favicon> faviconHandler = null;
    private final PluginLogger logger = new BungeeLogger(this);
    
    @Override
    public void onEnable(){
        this.core = AdvancedServerList.init(this, BungeePlayerPlaceholders.init(), BungeeServerPlaceholders.init(this));
    }
    
    @Override
    public void onDisable(){
        core.disable();
    }
    
    @Override
    public void loadCommands(){
        getProxy().getPluginManager().registerCommand(this, new CmdAdvancedServerList(this));
    }
    
    @Override
    public void loadEvents(){
        new JoinEvent(this);
        new PingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15585).addCustomChart(new SimplePie("profiles",
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
        return getDataFolder().toPath();
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
        return getProxy().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getProxy().getVersion();
    }
    
    @Override
    public String getLoader(){
        return "bungeecord";
    }
    
    @Override
    public Favicon createFavicon(BufferedImage image) throws IllegalArgumentException{
        return Favicon.create(image);
    }
    
    public int getOnlinePlayers(ServerInfo server){
        List<ProxiedPlayer> players = new ArrayList<>(server == null ? getProxy().getPlayers() : server.getPlayers());
        
        // Exclude players when PremiumVanish is enabled and player is hidden.
        if(getProxy().getPluginManager().getPlugin("PremiumVanish") != null){
            players.removeIf(BungeeVanishAPI::isInvisible);
        }
        
        return players.size();
    }
}
