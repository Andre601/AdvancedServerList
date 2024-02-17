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

import ch.andre601.advancedserverlist.advancedban.ban.AdvancedBanProvider;
import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;

public class AdvancedBanPlaceholders extends PlaceholderProvider{
    
    private final AdvancedBanProvider provider = new AdvancedBanProvider();
    
    public AdvancedBanPlaceholders(){
        super("advancedban");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server){
        return switch(placeholder) {
            // Mute-related placeholders
            case "isMuted" -> String.valueOf(provider.isMuted(player));
            case "muteReason" -> provider.getMuteReason(player);
            case "muteDuration" -> {
                String[] args = placeholder.split("\\s");
                if(args.length == 1)
                    yield provider.getMuteDuration(player);
                
                if(args.length > 2)
                    yield null;
                
                boolean fromStart = Boolean.getBoolean(args[1]);
                yield provider.getMuteDuration(player, fromStart);
            }
            
            // Ban-related placeholders
            case "isBanned" -> String.valueOf(provider.isBanned(player));
            case "banReason" -> provider.getBanReason(player);
            case "banDuration" -> {
                String[] args = placeholder.split("\\s");
                if(args.length == 1)
                    yield provider.getBanDuration(player);
                
                if(args.length > 2)
                    yield null;
                
                boolean fromStart = Boolean.getBoolean(args[1]);
                yield provider.getBanDuration(player, fromStart);
            }
            
            // Unknown/Invalid placeholder.
            default -> null;
        };
    }
}
