package ch.andre601.advancedserverlist.spigot.commands;

import ch.andre601.advancedserverlist.spigot.SpigotCore;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdAdvancedServerList implements CommandExecutor{
    
    SpigotCore plugin;
    BukkitAudiences bukkitAudiences;
    
    public CmdAdvancedServerList(SpigotCore plugin){
        this.plugin = plugin;
        bukkitAudiences = BukkitAudiences.create(plugin);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args){
        plugin.getCore().getCommandHandler().handle(new SpigotCmdSender(sender, bukkitAudiences), args);
        
        return true;
    }
}
