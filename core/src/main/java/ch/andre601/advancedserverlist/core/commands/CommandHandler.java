package ch.andre601.advancedserverlist.core.commands;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.CmdSender;

public class CommandHandler{
    
    private final AdvancedServerList core;
    
    public CommandHandler(AdvancedServerList core){
        this.core = core;
    }
    
    public void handle(CmdSender sender, String[] args){
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            if(sender.hasPermission("advancedserverlist.command.help")){
                sender.sendMsg("AdvancedServerList Commands");
                sender.sendMsg();
                sender.sendMsg("<aqua>/asl help <grey>- Shows this help.");
                sender.sendMsg();
                sender.sendMsg("<aqua>/asl reload <grey>- Reloads the configuration.");
            }else{
                sender.sendMsg("<red>You do not have the permissions to execute this command!");
            }
        }else
        if(args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission("advancedserverlist.command.reload")){
                sender.sendMsg("<grey>Reloading config.yml...");
                if(core.getFileHandler().reloadProfiles()){
                    sender.sendMsg("<green>Config.yml successfully reloaded!");
                }else{
                    sender.sendMsg("<red>Error while reloading config.yml");
                }
                
                sender.sendMsg("<grey>Reloading Profiles...");
                if(core.getFileHandler().reloadProfiles()){
                    sender.sendMsg("<green>Successfully loaded %d Profiles!", core.getFileHandler().getProfiles().size());
                }else{
                    sender.sendMsg("<red>Error while reloading Profiles. No profiles have been loaded.");
                }
                
                sender.sendMsg("<green>Reload complete!");
            }
        }else{
            sender.sendMsg("<red>Unknown subcommand <grey>%s</grey>. Use <grey>/asl help</grey> for a list of commands.", args[0]);
        }
    }
}
