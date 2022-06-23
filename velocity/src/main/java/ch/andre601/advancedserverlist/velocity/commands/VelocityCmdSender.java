package ch.andre601.advancedserverlist.velocity.commands;

import ch.andre601.advancedserverlist.core.interfaces.CmdSender;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import com.velocitypowered.api.command.CommandSource;

public class VelocityCmdSender implements CmdSender{
    
    private final CommandSource sender;
    
    public VelocityCmdSender(CommandSource sender){
        this.sender = sender;
    }
    
    @Override
    public boolean hasPermission(String permission){
        return sender.hasPermission(permission) || sender.hasPermission("advancedserverlist.admin");
    }
    
    @Override
    public void sendMsg(String msg, Object... args){
        sender.sendMessage(ComponentParser.toComponent(String.format(msg, args)));
    }
}
