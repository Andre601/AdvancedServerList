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

package ch.andre601.advancedserverlist.core.compat.papi;

import net.william278.papiproxybridge.api.PlaceholderAPI;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class PAPIUtil{
    
    private final PlaceholderAPI papi;
    
    private final PAPICache cache = new PAPICache();
    
    public PAPIUtil(){
        this.papi = PlaceholderAPI.createInstance();
    }
    
    // Make sure the version of PAPIProxyBridge we get has the required methods we need...
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCompatible(){
        try{
            papi.getClass().getMethod("findServers");
            return true;
        }catch(NoSuchMethodException ex){
            return false;
        }
    }
    
    public String getServer(){
        return cache.get(() -> {
            List<String> servers;
            try{
                servers = papi.findServers().getNow(Collections.emptyList());
            }catch(CancellationException | CompletionException ex){
                return null;
            }
            
            if(servers == null || servers.isEmpty())
                return null;
            
            return servers.get(0);
        });
    }
    
    public <P> P getPlayer(Collection<P> players){
        if(players.isEmpty())
            return null;
        
        return List.copyOf(players).get(0);
    }
    
    public String parse(String text, UUID carrier, UUID player){
        try{
            CompletableFuture<String> future = papi.formatPlaceholders(text, carrier, player);
            return future.getNow(text);
        }catch(IllegalArgumentException | CancellationException | CompletionException ex){
            return text;
        }
    }
}
