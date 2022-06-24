package ch.andre601.advancedserverlist.paper.commands;

import ch.andre601.advancedserverlist.paper.PaperCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdAdvancedServerList implements CommandExecutor{
    
    private final PaperCore plugin;
    
    public CmdAdvancedServerList(PaperCore plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args){
        plugin.getCore().getCommandHandler().handle(new PaperCmdSender(commandSender), args);
        
        return true;
    }
}
