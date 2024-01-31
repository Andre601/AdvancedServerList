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

package ch.andre601.advancedserverlist.bungeecord.objects;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.bungeecord.objects.BungeeProxy;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class BungeeServerPlaceholders extends PlaceholderProvider{
    
    private BungeeServerPlaceholders(){
        super("server");
    }
    
    public static BungeeServerPlaceholders init(){
        return new BungeeServerPlaceholders();
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        if(!(server instanceof BungeeProxy proxy))
            return null;
        
        String[] args = placeholder.split("\\s", 2);
        
        return switch(args[0]){
            case "playersOnline" -> {
                if(args.length >= 2){
                    String[] servers = args[1].split(",");
                    
                    int players = 0;
                    for(String serverName : servers){
                        if(serverName.isEmpty())
                            continue;
                        
                        ServerInfo info = proxy.getServers().get(serverName.strip());
                        if(info == null)
                            continue;
                        
                        players += info.getPlayers().size();
                    }
                    
                    yield String.valueOf(players);
                }
                
                yield String.valueOf(proxy.getPlayersOnline());
            }
            case "playersMax" -> {
                // ServerInfo doesn't provide the max players that could join, so we won't allow an extra argument
                if(args.length >= 2)
                    yield null;
                
                yield String.valueOf(proxy.getPlayersMax());
            }
            case "host" -> {
                if(args.length > 2)
                    yield null;
                
                if(args.length == 2){
                    ServerInfo info = proxy.getServers().get(args[1]);
                    if(info == null)
                        yield null;
                    
                    yield String.valueOf(((InetSocketAddress)info.getSocketAddress()).getHostString());
                }
                
                yield proxy.getHost();
            }
            default -> null;
        };
    }
}
