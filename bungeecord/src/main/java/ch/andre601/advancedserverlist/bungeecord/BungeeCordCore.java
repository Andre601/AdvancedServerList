/*
 * MIT License
 *
 * Copyright (c) 2022 Andre_601
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
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.nio.file.Path;

public class BungeeCordCore extends Plugin implements PluginCore{
    
    private AdvancedServerList core;
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
        new Metrics(this, 15585);
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getPath(){
        return getDataFolder().toPath();
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public String getPlatformName(){
        return getProxy().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getProxy().getVersion();
    }
}
