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
        String errorPrefix = "<grey>[<gradient:dark_red:red>AdvancedServerList</gradient>]";
        String prefix = "<grey>[<gradient:aqua:white>AdvancedServerList</gradient>]";
        
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            if(sender.hasPermission("advancedserverlist.command.help")){
                sender.sendMsg("%s - Commands", prefix);
                sender.sendMsg();
                sender.sendMsg("<gradient:aqua:white>/asl help</gradient> <grey>- Shows this Help");
                sender.sendMsg();
                sender.sendMsg("<gradient:aqua:white>/asl reload</gradient> <grey>- Reloads the plugin");
            }else{
                sender.sendMsg("%s <red>You do not have permissions to execute this command!", errorPrefix);
            }
        }else
        if(args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission("advancedserverlist.command.reload")){
                sender.sendMsg("%s Reloading plugin...", prefix);
                
                if(core.getFileHandler().reloadConfig()){
                    sender.sendMsg("%s <green>Reloaded <grey>config.yml</grey>!", prefix);
                }else{
                    sender.sendMsg("%s <red>Error while reloading the <grey>config.yml</grey>!", errorPrefix);
                }
                
                if(core.getFileHandler().reloadProfiles()){
                    sender.sendMsg("%s <green>Loaded <grey>%s Profile(s)</grey>!", prefix, core.getFileHandler().getProfiles());
                }else{
                    sender.sendMsg("%s <red>Cannot load Profiles!", errorPrefix);
                }
                
                core.clearFaviconCache();
                sender.sendMsg("%s <green>Successfully cleared Favicon cache!", prefix);
                
                sender.sendMsg("%s <green>Reload complete!", prefix);
            }else{
                sender.sendMsg("%s <red>You do not have permissions to execute this command!", errorPrefix);
            }
        }else{
            sender.sendMsg("%s <red>Unknown subcommand <grey>%s</grey>. Use <grey>/asl help</grey> for a list of commands.", prefix, args[0]);
        }
    }
}
