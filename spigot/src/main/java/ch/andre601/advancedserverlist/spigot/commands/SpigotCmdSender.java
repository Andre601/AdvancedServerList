package ch.andre601.advancedserverlist.spigot.commands;

import ch.andre601.advancedserverlist.core.interfaces.CmdSender;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;

public class SpigotCmdSender implements CmdSender{
    
    private final CommandSender sender;
    private final BukkitAudiences bukkitAudiences;
    
    public SpigotCmdSender(CommandSender sender, BukkitAudiences bukkitAudiences){
        this.sender = sender;
        this.bukkitAudiences = bukkitAudiences;
    }
    
    @Override
    public boolean hasPermission(String permission){
        return sender.hasPermission(permission) || sender.hasPermission("advancedserverlist.admin");
    }
    
    @Override
    public void sendMsg(String msg, Object... args){
        bukkitAudiences.sender(sender).sendMessage(ComponentParser.text(String.format(msg, args)).toComponent());
    }
}
