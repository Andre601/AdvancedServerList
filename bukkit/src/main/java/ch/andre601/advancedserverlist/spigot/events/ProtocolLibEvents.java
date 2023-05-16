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

package ch.andre601.advancedserverlist.spigot.events;

import ch.andre601.advancedserverlist.core.events.PingEventHandler;
import ch.andre601.advancedserverlist.spigot.SpigotCore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.TreeMap;

public class ProtocolLibEvents implements Listener{
    
    private final ProtocolManager protocolManager;
    
    private static final Map<String, String> hostAddresses = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    
    private ProtocolLibEvents(SpigotCore plugin, ProtocolManager protocolManager){
        this.protocolManager = protocolManager;
        
        loadPacketListener(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public static void init(SpigotCore plugin, ProtocolManager protocolManager){
        new ProtocolLibEvents(plugin, protocolManager);
    }
    
    public static Map<String, String> getHostAddresses(){
        return hostAddresses;
    }
    
    private void loadPacketListener(SpigotCore spigotPlugin){
        protocolManager.addPacketListener(new PacketAdapter(spigotPlugin, ListenerPriority.LOW, PacketType.Handshake.Client.SET_PROTOCOL){
            @Override
            public void onPacketReceiving(PacketEvent event){
                InetSocketAddress address = event.getPlayer().getAddress();
                if(address == null)
                    return;
                
                String host = event.getPacket().getStrings().read(0);
                
                hostAddresses.put(address.getHostString(), host);
            }
        });
        
        protocolManager.addPacketListener(new PacketAdapter(spigotPlugin, ListenerPriority.LOW, PacketType.Status.Server.SERVER_INFO){
            @Override
            public void onPacketSending(PacketEvent event){
                PingEventHandler.handleEvent(new ProtocolLibEventWrapper(spigotPlugin, event));
            }
        });
    }
}
