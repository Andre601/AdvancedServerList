package ch.andre601.advancedserverlist.paper.commands;

import ch.andre601.advancedserverlist.core.interfaces.CmdSender;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import org.bukkit.command.CommandSender;

public class PaperCmdSender implements CmdSender{
    
    private final CommandSender sender;
    
    public PaperCmdSender(CommandSender sender){
        this.sender = sender;
    }
    
    @Override
    public boolean hasPermission(String permission){
        return sender.hasPermission(permission) || sender.hasPermission("advancedserverlist.admin");
    }
    
    @Override
    public void sendMsg(String msg, Object... args){
        sender.sendMessage(ComponentParser.text(String.format(msg, args)).toComponent());
    }
}
