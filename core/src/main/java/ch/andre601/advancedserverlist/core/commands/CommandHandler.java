/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ch.andre601.advancedserverlist.core.commands;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.commands.CmdSender;
import ch.andre601.advancedserverlist.core.interfaces.commands.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler{
    
    private final List<PluginCommand> subCommands = new ArrayList<>();
    
    public CommandHandler(AdvancedServerList<?> core){
        subCommands.add(new Help());
        subCommands.add(new Reload(core));
        subCommands.add(new ClearCache(core));
    }
    
    public void handle(CmdSender sender, String[] args){
        if(args.length == 0){
            sender.sendErrorMsg("<red>Too few arguments! Use <grey>/asl help</grey> for a list of commands.");
            return;
        }
        
        for(PluginCommand subCommand : subCommands){
            if(subCommand.getArgument().equalsIgnoreCase(args[0])){
                if(!sender.hasPermission(subCommand.getPermission())){
                    sender.sendErrorMsg(
                        "<red>You do not have the permission <grey>%s</grey> to execute this command.",
                        subCommand.getPermission()
                    );
                    return;
                }
                
                subCommand.handle(sender);
                return;
            }
        }
        
        sender.sendErrorMsg("<red>Unknown subcommand <grey>%s</grey>. Use <grey>/asl help</grey> for a list of commands.", args[0]);
    }
    
    private static class Help extends PluginCommand{
        
        public Help(){
            super("help");
        }
    
        @Override
        public void handle(CmdSender sender){
            sender.sendPrefixedMsg("- Commands");
            sender.sendMsg();
            sender.sendMsg("<aqua>/asl <white>help <grey>- Shows this help");
            sender.sendMsg();
            sender.sendMsg("<aqua>/asl <white>reload <grey>- Reloads the config.yml and profiles");
            sender.sendMsg();
            sender.sendMsg("<aqua>/asl <white>clearCache <grey>- Clears the Player and Favicon cache");
        }
    }
    
    private static class Reload extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public Reload(AdvancedServerList<?> core){
            super("reload");
            
            this.core = core;
        }
        
        @Override
        public void handle(CmdSender sender){
            sender.sendPrefixedMsg("Reloading plugin...");
            
            if(core.getFileHandler().reloadConfig()){
                sender.sendPrefixedMsg("<green>Reloaded <grey>config.yml</grey>!");
            }else{
                sender.sendErrorMsg("<red>Error while reloading <grey>config.yml</grey>!");
            }
            
            if(core.getFileHandler().reloadProfiles()){
                sender.sendPrefixedMsg("<green>Loaded <grey>%d profile(s)</grey>!", core.getFileHandler().getProfiles().size());
            }else{
                sender.sendErrorMsg("<red>Error while loading profile(s)!");
            }
            
            sender.sendPrefixedMsg("<green>Reload complete!");
        }
    }
    
    private static class ClearCache extends PluginCommand{
        
        private final AdvancedServerList<?> core;
        
        public ClearCache(AdvancedServerList<?> core){
            super("clearCache");
            
            this.core = core;
        }
    
        @Override
        public void handle(CmdSender sender){
            sender.sendPrefixedMsg("Clearing caches...");
            
            core.clearFaviconCache();
            sender.sendPrefixedMsg("<green>Successfully cleared Favicon Cache!");
            
            core.clearPlayerCache();
            sender.sendPrefixedMsg("<green>Successfully cleared Player Cache!");
            
            sender.sendPrefixedMsg("<green>Cache clearing complete!");
        }
    }
}
