package ch.andre601.advancedserverlist.spigot;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.spigot.commands.CmdAdvancedServerList;
import ch.andre601.advancedserverlist.spigot.events.LoadEvent;
import ch.andre601.advancedserverlist.spigot.logging.SpigotLogger;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class SpigotCore extends JavaPlugin implements PluginCore{
    
    private AdvancedServerList core;
    private ProtocolManager protocolManager;
    private final PluginLogger logger = new SpigotLogger(getLogger());
    
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
        
        protocolManager = ProtocolLibrary.getProtocolManager();
        
        this.core = new AdvancedServerList(this);
    }
    
    @Override
    public void onDisable(){
        getCore().disable();
    }
    
    @Override
    public void loadCommands(){
        PluginCommand cmd = getServer().getPluginCommand("advancedserverlist");
        if(cmd == null){
            getPluginLogger().warn("Could not register command /advancedserverlist");
            return;
        }
        cmd.setExecutor(new CmdAdvancedServerList(this));
    }
    
    @Override
    public void loadEvents(){
        new LoadEvent(this, protocolManager);
    }
    
    @Override
    public void loadMetrics(){
        
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
        return getServer().getName();
    }
    
    @Override
    public String getPlatformVersion(){
        return getServer().getVersion();
    }
    
    private void printPaperInfo(){
        getPluginLogger().warn("You are using the Spigot version of AdvancedServerList on a PaperMC server.");
        getPluginLogger().warn("It is recommended to use the dedicated PaperMC version, to benefit from the");
        getPluginLogger().warn("Following improvements:");
        getPluginLogger().warn("- No need to download external libraries already provided by PaperMC.");
        getPluginLogger().warn("- No dependency on ProtocolLib thanks to provided Events.");
        getPluginLogger().warn("");
        getPluginLogger().warn("AdvancedServerList may work as normal, but consider using the PaperMC version instead!");
    }
}
