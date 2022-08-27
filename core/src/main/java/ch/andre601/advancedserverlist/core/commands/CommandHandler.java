/*
 * MIT License
 *
 * Copyright (c) 2022 Andre_601
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
                sender.sendMsg("<aqua>/asl reload <grey>- Reloads the config and profiles.");
            }else{
                sender.sendMsg("<red>You do not have the permissions to execute this command!");
            }
        }else
        if(args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission("advancedserverlist.command.reload")){
                sender.sendMsg("<grey>Reloading config.yml...");
                if(core.getFileHandler().reloadConfig()){
                    sender.sendMsg("<green>Successfully reloaded <grey>config.yml</grey>!");
                }else{
                    sender.sendMsg("<red>Error while reloading the config.yml.");
                }
                
                sender.sendMsg("<grey>Reloading Profiles...");
                if(core.getFileHandler().reloadProfiles()){
                    sender.sendMsg("<green>Successfully loaded <grey>%d</grey> Profiles!", core.getFileHandler().getProfiles().size());
                }else{
                    sender.sendMsg("<red>Error while reloading Profiles. No profiles have been loaded.");
                }
                
                sender.sendMsg("<grey>Clearing Favicon cache...");
                core.clearFaviconCache();
                sender.sendMsg("<green>Successfully cleared Favicon cache!");
                
                sender.sendMsg("<green>Reload complete!");
            }
        }else{
            sender.sendMsg("<red>Unknown subcommand <grey>%s</grey>. Use <grey>/asl help</grey> for a list of commands.", args[0]);
        }
    }
}
