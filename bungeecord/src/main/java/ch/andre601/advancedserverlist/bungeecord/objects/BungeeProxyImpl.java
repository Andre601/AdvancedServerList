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

import ch.andre601.advancedserverlist.api.bungeecord.objects.BungeeProxy;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

public record BungeeProxyImpl(Map<String, ServerInfo> servers, int playersOnline, int playersMax, String host) implements BungeeProxy{
    @Override
    public Map<String, ServerInfo> getServers(){
        return servers;
    }
    
    @Override
    public int getPlayersOnline(){
        return playersOnline;
    }
    
    @Override
    public int getPlayersMax(){
        return playersMax;
    }
    
    @Override
    public String getHost(){
        return host;
    }
}
