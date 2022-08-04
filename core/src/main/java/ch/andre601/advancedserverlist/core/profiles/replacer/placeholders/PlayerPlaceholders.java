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

package ch.andre601.advancedserverlist.core.profiles.replacer.placeholders;

import ch.andre601.advancedserverlist.core.profiles.players.GenericPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerPlaceholders implements Placeholders{
    
    private final Map<String, Object> replacements = new HashMap<>();
    
    public PlayerPlaceholders(GenericPlayer<?> player){
        this.replacements.put("${player name}", player.getName());
        this.replacements.put("${player protocol}", player.getProtocol());
        
        if(player.getVersion() != null)
            this.replacements.put("${player version}", player.getVersion());
        
        if(player.getPlayer() == null)
            return;
        
        this.replacements.put("${player playedBefore}", player.hasPlayedBefore());
        this.replacements.put("${player isBanned}", player.isBanned());
        this.replacements.put("${player isWhitelisted}", player.isWhitelisted());
        this.replacements.put("${player uuid}", player.getUniqueId());
    }
    
    @Override
    public Map<String, Object> getReplacements(){
        return replacements;
    }
}
