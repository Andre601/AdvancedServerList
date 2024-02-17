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

package ch.andre601.advancedserverlist.advancedban.ban;

import ch.andre601.advancedserverlist.api.ban.PunishmentProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import com.google.common.base.Strings;
import com.google.common.primitives.UnsignedLongs;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;

import java.util.UUID;

public class AdvancedBanProvider implements PunishmentProvider{
    
    private static PunishmentManager getAPI(){
        return PunishmentManager.get();
    }
    
    private static String getNoDashesUUID(UUID uuid){
        return toHexString(uuid.getMostSignificantBits()) + toHexString(uuid.getLeastSignificantBits());
    }
    
    private static Punishment getMute(GenericPlayer player){
        return getAPI().getMute(getNoDashesUUID(player.getUUID()));
    }
    
    private static Punishment getBan(GenericPlayer player){
        return getAPI().getBan(getNoDashesUUID(player.getUUID()));
    }
    
    private static String toHexString(long unsigned){
        return Strings.padStart(UnsignedLongs.toString(unsigned, 16), 16, '0');
    }
    
    @Override
    public boolean isMuted(GenericPlayer player){
        return getAPI().isMuted(getNoDashesUUID(player.getUUID()));
    }
    
    @Override
    public boolean isBanned(GenericPlayer player){
        return getAPI().isBanned(getNoDashesUUID(player.getUUID()));
    }
    
    @Override
    public String getMuteReason(GenericPlayer player){
        Punishment mute = getMute(player);
        if(mute == null)
            return null;
        
        return mute.getReason();
    }
    
    @Override
    public String getBanReason(GenericPlayer player){
        Punishment ban = getBan(player);
        if(ban == null)
            return null;
        
        return ban.getReason();
    }
    
    @Override
    public String getMuteDuration(GenericPlayer player){
        return getMuteDuration(player, false);
    }
    
    @Override
    public String getBanDuration(GenericPlayer player){
        return getBanDuration(player, false);
    }
    
    public String getMuteDuration(GenericPlayer player, boolean fromStart){
        Punishment mute = getMute(player);
        if(mute == null)
            return null;
        
        return mute.getDuration(fromStart);
    }
    
    public String getBanDuration(GenericPlayer player, boolean fromStart){
        Punishment ban = getMute(player);
        if(ban == null)
            return null;
        
        return ban.getDuration(fromStart);
    }
}
