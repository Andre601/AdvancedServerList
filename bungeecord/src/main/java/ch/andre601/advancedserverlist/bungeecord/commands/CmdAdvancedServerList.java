package ch.andre601.advancedserverlist.bungeecord.commands;

import ch.andre601.advancedserverlist.bungeecord.BungeeCordCore;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CmdAdvancedServerList extends Command{
    
    private final BungeeCordCore plugin;
    private final BungeeAudiences audiences;
    
    public CmdAdvancedServerList(BungeeCordCore plugin){
        super("advancedserverlist", "advancedserverlist.command.asl", "asl");
        this.plugin = plugin;
        this.audiences = BungeeAudiences.create(plugin);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args){
        plugin.getCore().getCommandHandler().handle(new BungeeCmdSender(sender, audiences), args);
    }
}
