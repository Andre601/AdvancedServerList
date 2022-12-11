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

package ch.andre601.advancedserverlist.api.objects;

import java.util.UUID;

/**
 * Abstract class used to wrap around some generic values including the provided Type instance.
 * 
 * @param <T>
 *        The Player Object/instance to use for the {@link #getPlayer() Player instance}.
 */
public abstract class GenericPlayer<T>{
    
    protected T player = null;
    protected String name = null;
    protected String version = null;
    protected int protocol = -1;
    protected boolean playedBefore = false;
    protected boolean banned = false;
    protected boolean whitelisted = false;
    protected UUID uuid = null;
    
    public T getPlayer(){
        return player;
    }
    
    public String getName(){
        return name;
    }
    
    public int getProtocol(){
        return protocol;
    }
    
    public String getVersion(){
        return version;
    }
    
    public boolean hasPlayedBefore(){
        return playedBefore;
    }
    
    public boolean isBanned(){
        return banned;
    }
    
    public boolean isWhitelisted(){
        return whitelisted;
    }
    
    public UUID getUniqueId(){
        return uuid;
    }
}
