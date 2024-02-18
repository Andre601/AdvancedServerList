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

package ch.andre601.advancedserverlist.banplugins.providers;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import com.google.common.base.Strings;
import com.google.common.primitives.UnsignedLongs;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AdvancedBanProvider implements PunishmentProvider{
    
    private static PunishmentManager getAPI(){
        return PunishmentManager.get();
    }
    
    private static String noDashesUUID(UUID uuid){
        return hexString(uuid.getMostSignificantBits()) + hexString(uuid.getLeastSignificantBits());
    }
    
    private static Punishment mute(GenericPlayer player){
        return getAPI().getMute(noDashesUUID(player.getUUID()));
    }
    
    private static Punishment ban(GenericPlayer player){
        return getAPI().getBan(noDashesUUID(player.getUUID()));
    }
    
    private static String hexString(long unsigned){
        return Strings.padStart(UnsignedLongs.toString(unsigned, 16), 16, '0');
    }
    
    @Override
    public boolean muted(GenericPlayer player){
        return getAPI().isMuted(noDashesUUID(player.getUUID()));
    }
    
    @Override
    public boolean banned(GenericPlayer player){
        return getAPI().isBanned(noDashesUUID(player.getUUID()));
    }
    
    @Override
    public String muteReason(GenericPlayer player){
        Punishment mute = mute(player);
        if(mute == null)
            return null;
        
        return mute.getReason();
    }
    
    @Override
    public String banReason(GenericPlayer player){
        Punishment ban = ban(player);
        if(ban == null)
            return null;
        
        return ban.getReason();
    }
    
    @Override
    public String muteDuration(GenericPlayer player){
        return muteDuration(player, false);
    }
    
    @Override
    public String banDuration(GenericPlayer player){
        return banDuration(player, false);
    }
    
    @Override
    public String muteExpirationDate(GenericPlayer player, String pattern){
        Punishment mute = mute(player);
        if(mute == null)
            return null;
        
        long end = mute.getEnd();
        if(end < 0L)
            return "never";
        
        Date date = new Date(end);
        return new SimpleDateFormat(pattern).format(date);
    }
    
    @Override
    public String banExpirationDate(GenericPlayer player, String pattern){
        Punishment ban = ban(player);
        if(ban == null)
            return null;
        
        long end = ban.getEnd();
        if(end < 0L)
            return "never";
        
        Date date = new Date(end);
        return new SimpleDateFormat(pattern).format(date);
    }
    
    public String muteDuration(GenericPlayer player, boolean fromStart){
        Punishment mute = mute(player);
        if(mute == null)
            return null;
        
        return mute.getDuration(fromStart);
    }
    
    public String banDuration(GenericPlayer player, boolean fromStart){
        Punishment ban = ban(player);
        if(ban == null)
            return null;
        
        return ban.getDuration(fromStart);
    }
}
