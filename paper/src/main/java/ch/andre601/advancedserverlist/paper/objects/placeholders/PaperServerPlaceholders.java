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

package ch.andre601.advancedserverlist.paper.objects.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.paper.PaperCore;
import ch.andre601.advancedserverlist.paper.objects.impl.PaperServerImpl;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class PaperServerPlaceholders extends PlaceholderProvider{
    
    private final PaperCore plugin;
    
    public PaperServerPlaceholders(PaperCore plugin){
        super("server");
        this.plugin = plugin;
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        if(!(server instanceof PaperServerImpl paperServer))
            return null;
        
        String[] args = placeholder.split("\\s", 2);
        
        return switch(args[0]){
            case "playersOnline" -> {
                if(args.length >= 2){
                    String[] worlds = args[1].split(",");
                    
                    int players = 0;
                    for(String worldName : worlds){
                        if(worldName.isEmpty())
                            continue;
                        
                        World world = paperServer.worlds().get(worldName.strip());
                        if(world == null)
                            continue;
                        
                        players += plugin.getPlayersOnline(world);
                    }
                    
                    yield String.valueOf(players);
                }
                
                yield String.valueOf(paperServer.playersOnline());
            }
            case "playersMax" -> {
                if(args.length >= 2)
                    yield null;
                
                yield String.valueOf(paperServer.getPlayersMax());
            }
            case "host" -> {
                if(args.length >= 2)
                    yield null;
                
                yield paperServer.getHost();
            }
            case "whitelistEnabled" -> {
                if(args.length >= 2)
                    yield null;
                
                yield String.valueOf(Bukkit.hasWhitelist());
            }
            default -> null;
        };
    }
}
