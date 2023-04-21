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

package ch.andre601.advancedserverlist.spigot;

import ch.andre601.advancedserverlist.bukkit.BukkitCore;
import ch.andre601.advancedserverlist.bukkit.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.bukkit.logging.BukkitLogger;
import ch.andre601.advancedserverlist.bukkit.objects.BukkitPlayerPlaceholders;
import ch.andre601.advancedserverlist.bukkit.objects.PAPIPlaceholders;
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.spigot.events.LoadEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.command.PluginCommand;

import java.nio.file.Path;

public class SpigotCore extends BukkitCore<WrappedServerPing.CompressedImage>{
    
    private final PluginLogger logger = new BukkitLogger(getLogger());
    
    private AdvancedServerList<WrappedServerPing.CompressedImage> core;
    private FaviconHandler<WrappedServerPing.CompressedImage> faviconHandler = null;
    private PAPIPlaceholders<WrappedServerPing.CompressedImage> papiPlaceholders = null;
    
    @Override
    public void onEnable(){
        try{
            Class.forName("io.papermc.paper.configuration.ConfigurationLoaders");
            
            printPaperInfo();
        }catch(ClassNotFoundException ex){
            try{
                // Above class only exists since 1.19+, so this is a fallback for older versions.
                Class.forName("com.destroystokyo.paper.PaperConfig");
                
                printPaperInfo();
            }catch(ClassNotFoundException ignored){}
        }
        
        this.core = AdvancedServerList.init(this, BukkitPlayerPlaceholders.init());
    }
    
    @Override
    public void onDisable(){
        if(papiPlaceholders != null){
            papiPlaceholders.unregister();
            papiPlaceholders = null;
        }
        
        getCore().disable();
    }
    
    @Override
    public void loadCommands(){
        PluginCommand cmd = getServer().getPluginCommand("advancedserverlist");
        if(cmd == null){
            getPluginLogger().warn("Unable to register command /advancedserverlist");
            return;
        }
        cmd.setExecutor(new CmdAdvancedServerList(this, BukkitAudiences.create(this)));
    }
    
    @Override
    public void loadEvents(){
        new LoadEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        new Metrics(this, 15584).addCustomChart(new SimplePie("profiles",
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
    public AdvancedServerList<WrappedServerPing.CompressedImage> getCore(){
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
    public FaviconHandler<WrappedServerPing.CompressedImage> getFaviconHandler(){
        if(faviconHandler == null)
            faviconHandler = new FaviconHandler<>(core);
        
        return faviconHandler;
    }
    
    @Override
    public String getPlatformName(){
        return getServer().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getServer().getVersion();
    }
    
    @Override
    public String getLoader(){
        return "spigot";
    }
    
    public void setPapiPlaceholders(PAPIPlaceholders<WrappedServerPing.CompressedImage> papiPlaceholders){
        this.papiPlaceholders = papiPlaceholders;
    }
    
    private void printPaperInfo(){
        getPluginLogger().warn("======================================================================================");
        getPluginLogger().warn("You are using the Spigot version of AdvancedServerList on a Paper server.");
        getPluginLogger().warn("It is recommended to use the dedicated Paper version, to benefit from the");
        getPluginLogger().warn("following improvements:");
        getPluginLogger().warn(" - No downloading of external Libraries already provided by Paper (i.e. Adventure).");
        getPluginLogger().warn(" - No dependency on ProtocolLib thanks to provided Events.");
        getPluginLogger().warn("");
        getPluginLogger().warn("AdvancedServerList may work as normal, but consider using the PaperMC version instead!");
        getPluginLogger().warn("======================================================================================");
    }
}
