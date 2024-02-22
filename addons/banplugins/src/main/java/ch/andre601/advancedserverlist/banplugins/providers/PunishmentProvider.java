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

public interface PunishmentProvider{
    
    boolean muted(GenericPlayer player);
    
    boolean banned(GenericPlayer player);
    
    String muteReason(GenericPlayer player);
    
    String banReason(GenericPlayer player);
    
    String muteDuration(GenericPlayer player);
    
    String banDuration(GenericPlayer player);
    
    default String muteExpirationDate(GenericPlayer player){
        return muteExpirationDate(player, "dd, MMM yyyy HH:mm:ss");
    }
    
    String muteExpirationDate(GenericPlayer player, String pattern);
    
    default String banExpirationDate(GenericPlayer player){
        return banExpirationDate(player, "dd, MMM yyyy HH:mm:ss");
    }
    
    String banExpirationDate(GenericPlayer player, String pattern);
    
}
