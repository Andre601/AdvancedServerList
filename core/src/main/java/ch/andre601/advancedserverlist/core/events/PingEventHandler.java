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

package ch.andre601.advancedserverlist.core.events;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.compat.maintenance.MaintenanceUtil;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.compat.papi.PAPIUtil;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.ServerListProfile;
import ch.andre601.advancedserverlist.core.profiles.profile.ProfileManager;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

public class PingEventHandler{
    
    private static PAPIUtil papiUtil = null;
    private static MaintenanceUtil maintenanceUtil = null;
    
    public static <F, P extends GenericPlayer> void handleEvent(GenericEventWrapper<F, P> event){
        event.getPlugin().getPluginLogger().debug(PingEventHandler.class, "Received ping event. Handling it...");
        if(event.isInvalidProtocol() || event.isMaintenanceModeActive()){
            event.getPlugin().getPluginLogger().debug(PingEventHandler.class, "Not handling event. Either protocol was invalid of Maintenance is enabled.");
            return;
        }
        
        PluginCore<F> plugin = event.getPlugin();
        String host = event.getVirtualHost();
        PluginLogger logger = plugin.getPluginLogger();
        
        logger.debug(PingEventHandler.class, "Protocol valid. Continue handling...");
        
        int online = event.getOnlinePlayers();
        int max = event.getMaxPlayers();
        
        P player = event.createPlayer(
            plugin.getCore().getPlayerHandler().getCachedPlayer(event.getPlayerIP()),
            event.getProtocolVersion()
        );
        GenericServer server = event.createGenericServer(online, max, host);
        
        ServerListProfile profile = ProfileManager.resolveProfile(plugin.getCore(), player, server);
        
        if(profile == null){
            logger.debugWarn(PingEventHandler.class, "Server List Profile couldn't be resolved properly. Cancelling event handling...");
            return;
        }
        
        logger.debug(PingEventHandler.class, "Received valid Server List Profile. Calling PreServerListSetEvent...");
        
        GenericServerListEvent e = event.callEvent(ProfileManager.merge(profile));
        if(e == null || e.isCancelled()){
            logger.debug(PingEventHandler.class, "PreServerListSetEvent was cancelled. Stopping ping handling...");
            return;
        }
        
        logger.debug(PingEventHandler.class, "PreServerListSetEvent completed. Proceeding with ping handling...");
        
        ProfileEntry entry = e.getEntry();
        if(entry.isInvalid()){
            logger.debugWarn(PingEventHandler.class, "No valid ProfileEntry retrieved. Cancelling ping handling...");
            return;
        }
        
        boolean extraPlayers = ProfileManager.checkOption(entry.extraPlayersEnabled());
        
        if(extraPlayers){
            max = online + (entry.extraPlayersCount() == null ? 0 : entry.extraPlayersCount());
            
            logger.debug(PingEventHandler.class, "Extra Players enabled. Applying '%d' as max player count...", max);
            
            event.setMaxPlayers(max);
        }
        
        if(ProfileManager.checkOption(entry.maxPlayersEnabled()) && !extraPlayers){
            max = (entry.maxPlayersCount() == null) ? 0 : entry.maxPlayersCount();
            
            logger.debug(PingEventHandler.class, "Max Players enabled. Applying '%d' as max player count...", max);
            
            event.setMaxPlayers(max);
        }
        
        GenericServer finalServer = event.createGenericServer(online, max, host);
        
        if(ProfileManager.checkOption(entry.motd())){
            logger.debug(PingEventHandler.class, "MOTD set. Applying '%s'...", String.join("\\n", entry.motd()));
            
            event.setMotd(
                ComponentParser.list(entry.motd())
                    .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                    .modifyText(text -> event.parsePAPIPlaceholders(text, player))
                    .toComponent()
            );
        }
        
        boolean hidePlayers = ProfileManager.checkOption(entry.hidePlayersEnabled());
        
        if(hidePlayers){
            logger.debug(PingEventHandler.class, "Hide Players enabled. Hiding player count...");
            
            event.hidePlayers();
        }
        
        if(ProfileManager.checkOption(entry.playerCountText()) && !hidePlayers){
            logger.debug(PingEventHandler.class, "Player Count Text set. Applying '%s'...", entry.playerCountText());
            
            event.setPlayerCount(
                ComponentParser.text(entry.playerCountText())
                    .modifyText(text -> StringReplacer.replace(text, player, finalServer))
                    .modifyText(text -> event.parsePAPIPlaceholders(text, player))
                    .toString()
            );
        }
        
        if(ProfileManager.checkOption(entry.players()) && !hidePlayers){
            logger.debug(PingEventHandler.class, "Player Count Hover set. Applying '%s'...", String.join("\\n", entry.players()));
            
            event.setPlayers(entry.players(), player, server);
        }
        
        if(ProfileManager.checkOption(entry.favicon())){
            logger.debug(PingEventHandler.class, "Favicon set. Resolving '%s'...", entry.favicon());
            
            String favicon = StringReplacer.replace(entry.favicon(), player, server);
            
            F fav = plugin.getFaviconHandler().getFavicon(favicon, image -> {
                try{
                    logger.debug(PingEventHandler.class, "Converting BufferedImage to Server or Proxy Favicon instance...");
                    return event.createFavicon(image);
                }catch(Exception ex){
                    plugin.getPluginLogger().warn("Encountered an Exception while creating Favicon!", ex);
                    return null;
                }
            });
            
            if(fav == null){
                logger.debugWarn(PingEventHandler.class, "Favicon was invalid or not yet resolved! Using default favicon of Server/Proxy...");
                
                event.setDefaultFavicon();
            }else{
                logger.debug(PingEventHandler.class, "Applying favicon...");
                
                event.setFavicon(fav);
            }
        }
        
        logger.debug(PingEventHandler.class, "Event handling completed. Updating Ping data...");
        event.updateEvent();
    }
    
    public static PAPIUtil getPAPIUtil(){
        if(papiUtil != null)
            return papiUtil;
        
        return (papiUtil = new PAPIUtil());
    }
    
    public static MaintenanceUtil getMaintenanceUtil(){
        if(maintenanceUtil != null)
            return maintenanceUtil;
        
        return (maintenanceUtil = new MaintenanceUtil());
    }
}
