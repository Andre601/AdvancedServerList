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

package ch.andre601.advancedserverlist.advancedban.placeholders;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;

import java.util.List;

public class AdvancedBanPlaceholders extends PlaceholderProvider{
    public AdvancedBanPlaceholders(){
        super("advancedban");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        PunishmentManager manager = PunishmentManager.get();
        
        String[] values = placeholder.split("\\s", 2);
        Punishment punishment = getPunishment(player.getUUID().toString(), manager, values[0]);
        
        if(punishment == null)
            return null;
        
        return switch(values[0]){
            case "isMuted" -> String.valueOf(manager.isMuted(player.getUUID().toString()));
            case "isBanned" -> String.valueOf(manager.isBanned(player.getUUID().toString()));
            // Mute/Ban reason
            case "muteReason", "banReason" -> punishment.getReason();
            // Mute/Ban duration
            case "muteDuration", "banDuration" -> {
                boolean fromStart = false;
                if(values.length > 2)
                    yield null;
                
                if(values.length == 2)
                    fromStart = Boolean.getBoolean(values[1]);
                
                yield punishment.getDuration(fromStart);
            }
            // Unknown placeholder
            default -> null;
        };
    }
    
    private Punishment getPunishment(String uuid, PunishmentManager manager, String placeholder){
        PunishmentType type = null;
        switch(placeholder){
            case "muteReason", "muteDuration" -> type = PunishmentType.MUTE;
            case "banReason", "banDuration" -> type = PunishmentType.BAN;
        }
        
        if(type == null)
            return null;
        
        List<Punishment> punishments = manager.getPunishments(uuid, type, true);
        if(punishments.isEmpty())
            return null;
        
        return punishments.get(0);
    }
}
