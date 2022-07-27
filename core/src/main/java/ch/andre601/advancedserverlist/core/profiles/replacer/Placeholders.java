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

package ch.andre601.advancedserverlist.core.profiles.replacer;

import ch.andre601.advancedserverlist.core.AdvancedServerList;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Placeholders{
    
    private final AdvancedServerList core;
    private final Map<String, Object> replacements;
    
    public Placeholders(AdvancedServerList core){
        this.core = core;
        this.replacements = new HashMap<>();
    }
    
    public static Placeholders get(AdvancedServerList core){
        return new Placeholders(core);
    }
    
    
    public Placeholders withProtocol(int protocol){
        this.replacements.put("${player protocol}", protocol);
        return this;
    }
    
    public Placeholders withPlayersOnline(int online){
        this.replacements.put("${players online}", online);
        return this;
    }
    
    public Placeholders withPlayersMax(int max){
        this.replacements.put("${players max}", max);
        return this;
    }
    
    public Placeholders withPlayerName(InetSocketAddress playerAddress){
        if(playerAddress != null) 
            this.replacements.put("${player name}", core.getPlayerHandler().getPlayerByIp(playerAddress.getHostString()));
        
        return this;
    }
    
    public Placeholders withHostAddress(InetSocketAddress hostAddress){
        if(hostAddress != null)
            this.replacements.put("${server host}", hostAddress.getHostString());
        
        return this;
    }
    
    public Placeholders withHostAddress(String hostAddress){
        if(hostAddress != null) 
            this.replacements.put("${server host}", hostAddress);
        
        return this;
    }
    
    public Placeholders withPlayerVersion(String version){
        this.replacements.put("${player version}", version);
        return this;
    }
    
    public Map<String, Object> getReplacements(){
        return replacements;
    }
}
