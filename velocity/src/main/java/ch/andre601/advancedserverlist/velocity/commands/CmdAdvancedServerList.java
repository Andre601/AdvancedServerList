package ch.andre601.advancedserverlist.velocity.commands;

import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.velocitypowered.api.command.SimpleCommand;

public class CmdAdvancedServerList implements SimpleCommand{
    
    private final VelocityCore plugin;
    
    public CmdAdvancedServerList(VelocityCore plugin){
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation){
        plugin.getCore().getCommandHandler().handle(new VelocityCmdSender(invocation.source()), invocation.arguments());
    }
}
