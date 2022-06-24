package ch.andre601.advancedserverlist.bungeecord.commands;

import ch.andre601.advancedserverlist.core.interfaces.CmdSender;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;

public class BungeeCmdSender implements CmdSender{
    
    private final CommandSender sender;
    private final BungeeAudiences bungeeAudiences;
    
    public BungeeCmdSender(CommandSender sender, BungeeAudiences bungeeAudiences){
        this.sender = sender;
        this.bungeeAudiences = bungeeAudiences;
    }
    
    @Override
    public boolean hasPermission(String permission){
        return sender.hasPermission(permission) || sender.hasPermission("advancedserverlist.admin");
    }
    
    @Override
    public void sendMsg(String msg, Object... args){
        bungeeAudiences.sender(sender).sendMessage(ComponentParser.text(String.format(msg, args)).toComponent());
    }
}
