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

package ch.andre601.advancedserverlist.sponge;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.ServerCore;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.favicon.FaviconHandler;
import ch.andre601.advancedserverlist.core.profiles.replacer.placeholders.Placeholders;
import ch.andre601.advancedserverlist.sponge.events.PingEvent;
import ch.andre601.advancedserverlist.sponge.logging.SpongeLogger;
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Plugin("advancedserverlist")
public class SpongeCore implements ServerCore<Favicon, GameProfile, ServerPlayer>{
    
    private final PluginContainer pluginContainer;
    private final PluginLogger logger;
    
    private final AdvancedServerList core;
    private FaviconHandler<Favicon> faviconHandler = null;
    
    @Inject
    public SpongeCore(PluginContainer pluginContainer, Logger logger){
        this.core = new AdvancedServerList(this);
        
        this.pluginContainer = pluginContainer;
        this.logger = new SpongeLogger(logger);
    }
    
    @Override
    public void loadCommands(){
        
    }
    
    @Override
    public void loadEvents(){
        new PingEvent(this);
    }
    
    @Override
    public void loadMetrics(){
        
    }
    
    @Override
    public void clearFaviconCache(){
        if(faviconHandler == null)
            return;
        
        faviconHandler.clearCache();
    }
    
    @Override
    public AdvancedServerList getCore(){
        return core;
    }
    
    @Override
    public Path getFolderPath(){
        return null;
    }
    
    @Override
    public PluginLogger getPluginLogger(){
        return logger;
    }
    
    @Override
    public FaviconHandler<Favicon> getFaviconHandler(){
        if(faviconHandler == null)
            faviconHandler = new FaviconHandler<>(core);
        
        return faviconHandler;
    }
    
    @Override
    public List<GameProfile> createPlayers(List<String> lines, ServerPlayer player, Placeholders... placeholders){
        List<GameProfile> players = new ArrayList<>(lines.size());
        
        for(String line : lines){
            String parsed = ComponentParser.text(line)
                .replacements(placeholders[0])
                .replacements(placeholders[1])
                .toString();
            
            players.add(GameProfile.of(UUID.randomUUID(), parsed));
        }
        
        return players;
    }
    
    @Override
    public String getPlatformName(){
        return Sponge.platform().toString();
    }
    
    @Override
    public String getPlatformVersion(){
        return Sponge.platform().minecraftVersion().name();
    }
    
    public PluginContainer getPluginContainer(){
        return pluginContainer;
    }
}
