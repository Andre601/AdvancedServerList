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

package ch.andre601.advancedserverlist.paper.objects.impl;

import ch.andre601.advancedserverlist.api.bukkit.objects.BukkitPlayer;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.profiles.players.GenericPlayerImpl;
import org.bukkit.OfflinePlayer;

public class PaperPlayerImpl extends GenericPlayerImpl implements BukkitPlayer{
    
    private final OfflinePlayer player;
    private boolean playedBefore = false;
    private boolean banned = false;
    private boolean whitelisted = false;
    
    public PaperPlayerImpl(OfflinePlayer player, CachedPlayer cachedPlayer, int protocol){
        this.player = player;
        
        this.name = player == null ? cachedPlayer.name() : player.getName();
        this.protocol = protocol;
        this.uuid = player == null ? cachedPlayer.uuid() : player.getUniqueId();
        
        if(player == null)
            return;
        
        this.playedBefore = player.hasPlayedBefore();
        this.banned = player.isBanned();
        this.whitelisted = player.isWhitelisted();
    }
    
    @Override
    public OfflinePlayer getPlayer(){
        return player;
    }
    
    @Override
    public boolean hasPlayedBefore(){
        return playedBefore;
    }
    
    @Override
    public boolean isBanned(){
        return banned;
    }
    
    @Override
    public boolean isWhitelisted(){
        return whitelisted;
    }
}
