/*
 * MIT License
 *
 * Copyright (c) 2022-2024 Andre_601
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

package ch.andre601.advancedserverlist.velocity.objects.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.velocity.objects.VelocityProxy;
import ch.andre601.advancedserverlist.velocity.VelocityCore;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class VelocityServerPlaceholders extends PlaceholderProvider{
    
    private final VelocityCore plugin;
    
    private VelocityServerPlaceholders(VelocityCore plugin){
        super("server");
        this.plugin = plugin;
    }
    
    public static VelocityServerPlaceholders init(VelocityCore core){
        return new VelocityServerPlaceholders(core);
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        if(!(server instanceof VelocityProxy proxy))
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
                        
                        RegisteredServer registeredServer = proxy.getServers().get(serverName.strip());
                        if(registeredServer == null)
                            continue;
                        
                        int connected = plugin.getOnlinePlayers(registeredServer);
                        if(connected == -1)
                            continue;
                        
                        players += connected;
                    }
                    
                    yield String.valueOf(players);
                }
                
                yield String.valueOf(proxy.getPlayersOnline());
            }
            case "playersMax" -> {
                if(args.length >= 2)
                    yield null;
                
                yield String.valueOf(proxy.getPlayersMax());
            }
            case "host" -> {
                if(args.length > 2)
                    yield null;
                
                if(args.length == 2){
                    RegisteredServer info = proxy.getServers().get(args[1]);
                    if(info == null)
                        yield null;
                    
                    yield info.getServerInfo().getAddress().getHostString();
                }
                
                yield proxy.getHost();
            }
            default -> null;
        };
    }
}
