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
    
    private final static String errorPrefix = "<grey>[<gradient:dark_red:red>AdvancedServerList</gradient>]";
    private final static String prefix = "<grey>[<gradient:aqua:white>AdvancedServerList</gradient>]";
    
    public CommandHandler(AdvancedServerList core){
        subCommands.add(new Help());
        subCommands.add(new Reload(core));
    }
    
    public void handle(CmdSender sender, String[] args){
        if(args.length == 0){
            sender.sendMsg("%s <red>Too few arguments! Use <grey>/asl help</grey> for a list of commands.", errorPrefix);
            return;
        }
        
        for(PluginCommand subCommand : subCommands){
            if(subCommand.getArgument().equalsIgnoreCase(args[0])){
                if(!sender.hasPermission(subCommand.getPermission())){
                    sender.sendMsg(
                        "%s <red>You do not have the permission <grey>%s</grey> to execute this command.",
                        errorPrefix,
                        subCommand.getPermission()
                    );
                    return;
                }
                
                subCommand.handle(sender);
                return;
            }
        }
        
        sender.sendMsg("%s <red>Unknown subcommand <grey>%s</grey>. Use <grey>/asl help</grey> for a list of commands.", errorPrefix, args[0]);
    }
    
    private static class Help extends PluginCommand{
        
        public Help(){
            super("help");
        }
    
        @Override
        public void handle(CmdSender sender){
            sender.sendMsg("%s - Commands", prefix);
            sender.sendMsg();
            sender.sendMsg("<gradient:aqua:white>/asl help</gradient> <grey>- Shows this help");
            sender.sendMsg();
            sender.sendMsg("<gradient:aqua:white>/asl reload</gradient> <grey>- Reloads the plugin");
        }
    }
    
    private static class Reload extends PluginCommand{
        
        private final AdvancedServerList core;
        
        public Reload(AdvancedServerList core){
            super("reload");
            
            this.core = core;
        }
        
        @Override
        public void handle(CmdSender sender){
            sender.sendMsg("%s Reloading plugin...", prefix);
            
            if(core.getFileHandler().reloadConfig()){
                sender.sendMsg("%s <green>Reloaded <grey>config.yml</grey>!", prefix);
            }else{
                sender.sendMsg("%s <red>Error while reloading <grey>config.yml</grey>!", errorPrefix);
            }
            
            if(core.getFileHandler().reloadProfiles()){
                sender.sendMsg("%s <green>Loaded <grey>%d profile(s)</grey>!", prefix, core.getFileHandler().getProfiles().size());
            }else{
                sender.sendMsg("%s <red>Cannot load profile(s)!", errorPrefix);
            }
            
            core.clearFaviconCache();
            sender.sendMsg("%s <green>Successfully cleared Favicon Cache!", prefix);
            
            sender.sendMsg("%s <green>Reload complete!", prefix);
        }
    }
}
