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

package ch.andre601.advancedserverlist.bukkit.objects.placeholders;

import ch.andre601.advancedserverlist.api.bukkit.objects.BukkitServer;
import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class BukkitServerPlaceholders extends PlaceholderProvider{
    
    private BukkitServerPlaceholders(){
        super("server");
    }
    
    public static BukkitServerPlaceholders init(){
        return new BukkitServerPlaceholders();
    }
    
    @Override
    public String parsePlaceholder(String placeholders, GenericPlayer player, GenericServer server){
        if(!(server instanceof BukkitServer bukkitServer))
            return null;
        
        String[] args = placeholders.split("\\s", 2);
        
        return switch(args[0]){
            case "playersOnline" -> {
                if(args.length >= 2){
                    int players = 0;
                    for(int i = 1; i < args.length; i++){
                        World world = bukkitServer.getWorlds().get(args[i]);
                        if(world == null)
                            continue;
                        
                        players += world.getPlayers().size();
                    }
                    
                    yield String.valueOf(players);
                }
                
                yield String.valueOf(bukkitServer.getPlayersOnline());
            }
            case "playersMax" -> {
                if(args.length >= 2)
                    yield null;
                
                yield String.valueOf(bukkitServer.getPlayersMax());
            }
            case "host" -> {
                if(args.length >= 2)
                    yield null;
                
                yield bukkitServer.getHost();
            }
            case "whitelistEnabled" -> {
                if(args.length >= 2)
                    yield null;
                
                yield String.valueOf(Bukkit.isWhitelistEnforced());
            }
            default -> null;
        };
    }
}
