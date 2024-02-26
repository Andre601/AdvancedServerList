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
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.punish.Punishment;
import space.arim.libertybans.api.user.KnownAccount;
import space.arim.omnibus.Omnibus;
import space.arim.omnibus.OmnibusProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class LibertyBansProvider implements PunishmentProvider{
    private final LibertyBans libertyBans;
    
    private LibertyBansProvider(LibertyBans libertyBans){
        this.libertyBans = libertyBans;
    }
    
    public static LibertyBansProvider create(){
        Omnibus omnibus = OmnibusProvider.getOmnibus();
        LibertyBans libertyBans = omnibus.getRegistry().getProvider(LibertyBans.class).orElseThrow();
        
        return new LibertyBansProvider(libertyBans);
    }
    
    @Override
    public boolean muted(GenericPlayer player){
        return punishment(player.getUUID(), PunishmentType.MUTE) != null;
    }
    
    @Override
    public boolean banned(GenericPlayer player){
        return punishment(player.getUUID(), PunishmentType.BAN) != null;
    }
    
    @Override
    public String muteReason(GenericPlayer player){
        Punishment mute = punishment(player.getUUID(), PunishmentType.MUTE);
        if(mute == null)
            return null;
        
        return mute.getReason();
    }
    
    @Override
    public String banReason(GenericPlayer player){
        Punishment ban = punishment(player.getUUID(), PunishmentType.BAN);
        if(ban == null)
            return null;
        
        return ban.getReason();
    }
    
    @Override
    public String muteDuration(GenericPlayer player){
        return null;
    }
    
    @Override
    public String banDuration(GenericPlayer player){
        return null;
    }
    
    @Override
    public String muteExpirationDate(GenericPlayer player, String pattern){
        Punishment mute = punishment(player.getUUID(), PunishmentType.MUTE);
        if(mute == null)
            return null;
        
        if(mute.isPermanent())
            return "never";
        
        return this.returnDate(pattern, mute.getEndDate().toEpochMilli());
    }
    
    @Override
    public String banExpirationDate(GenericPlayer player, String pattern){
        Punishment ban = punishment(player.getUUID(), PunishmentType.BAN);
        if(ban == null)
            return null;
        
        if(ban.isPermanent())
            return "never";
        
        return this.returnDate(pattern, ban.getEndDate().toEpochMilli());
    }
    
    private Punishment punishment(UUID uuid, PunishmentType type){
        List<? extends KnownAccount> accounts = libertyBans.getAccountSupervisor()
            .findAccountsMatching(uuid)
            .join();
        
        if(accounts.isEmpty())
            return null;
        
        KnownAccount sorted = Collections.max(accounts, Comparator.comparing(KnownAccount::recorded));
        
        return libertyBans.getSelector()
            .selectionByApplicabilityBuilder(sorted.uuid(), sorted.address())
            .type(type)
            .build()
            .getFirstSpecificPunishment()
            .toCompletableFuture()
            .join()
            .orElse(null);
    }
}
