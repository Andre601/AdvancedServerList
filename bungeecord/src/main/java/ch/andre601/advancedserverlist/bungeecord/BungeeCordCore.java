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
import ch.andre601.advancedserverlist.bungeecord.events.JoinEvent;
import ch.andre601.advancedserverlist.bungeecord.events.PingEvent;
import ch.andre601.advancedserverlist.bungeecord.logging.BungeeLogger;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.ProxyCore;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.Placeholders;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimplePie;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeeCordCore extends Plugin implements ProxyCore<Favicon, ServerPing.PlayerInfo>{
    
    private AdvancedServerList core;
    private FaviconHandler<Favicon> faviconHandler = null;
    private final PluginLogger logger = new BungeeLogger(getLogger());
    
    @Override
    public void onEnable(){
        this.core = new AdvancedServerList(this);
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
    public List<ServerPing.PlayerInfo> createPlayers(List<String> lines, Placeholders... placeholders){
        List<ServerPing.PlayerInfo> players = new ArrayList<>(lines.size());
        
        for(String line : lines){
            String parsed = ComponentParser.text(line)
                .replacements(placeholders[0])
                .replacements(placeholders[1])
                .toString();
            
            players.add(new ServerPing.PlayerInfo(parsed, UUID.randomUUID()));
        }
        
        return players;
    }
}
