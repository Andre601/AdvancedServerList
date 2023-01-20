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

package ch.andre601.advancedserverlist.api.objects;

import java.util.UUID;

/**
 * Abstract class used by the different versions of AdvancedServerList to extend on.
 * 
 * <p>No matter the platform will the following values <b>always</b> be present:
 * <ul>
 *     <li>{@link #getName() Name of the player}</li>
 *     <li>{@link #getProtocol() Protocol version of the player}</li>
 *     <li>{@link #getUuid() UUID of the player}</li>
 * </ul>
 * Any other value may not be updated and default to a value such as {@code null} for platforms that don't offer any form
 * of offline player data retrieval.
 */
public abstract class GenericPlayer{
    protected String name = null;
    protected int protocol = -1;
    protected UUID uuid = null;
    
    protected String version = null;
    protected boolean playedBefore = false;
    protected boolean banned = false;
    protected boolean whitelisted = false;
    
    /**
     * Returns the name of the player.
     * <br>Note that on Spigot, Paper and other forks can the name differ from the one cached by AdvancedServerList,
     * if the plugin was able to retrieve an OfflinePlayer instance from the server. On BungeeCord and Velocity will
     * the returned String always be the name from the cache.
     * 
     * <p>Name may be whatever has been defined in AdvancedServerList's config.yml, should the player not be cached yet
     * by AdvancedServerList.
     * 
     * @return String representing the player's name.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the protocol ID the player is using. The protocol ID is an integer used by Minecraft to determine what
     * version a server or client is running.
     * 
     * @return Integer representing the protocol version of this player.
     */
    public int getProtocol(){
        return protocol;
    }
    
    /**
     * Returns the Unique ID associated with this player.
     *
     * <p>UUID may be whatever has been defined in AdvancedServerList's config.yml, should the player not be cached yet
     * by AdvancedServerList.
     * 
     * @return UUID of the player.
     */
    public UUID getUuid(){
        return uuid;
    }
    
    /**
     * Returns the {@link #getProtocol() protocol version} in a readable MC version format (i.e. 1.19.3).
     * 
     * <p>This only works on Velocity and will return {@code null} for any other platform.
     * 
     * @return The readable MC version the player uses.
     */
    public String getVersion(){
        return version;
    }
    
    /**
     * Returns whether this player has played on the Server before.
     * 
     * <p>The returned boolean is <b>always</b> false on BungeeCord and Velocity due to a lack of any offline player data
     * storage.
     * 
     * @return Boolean indicating whether this player has played on this Server before.
     */
    public boolean hasPlayedBefore() {
        return playedBefore;
    }
    
    /**
     * Returns whether this player is banned on the server.
     *
     * <p>The returned boolean is <b>always</b> false on BungeeCord and Velocity due to a lack of any offline player data
     * storage.
     * 
     * @return Boolean indicating whether this player was banned from the server.
     */
    public boolean isBanned(){
        return banned;
    }
    
    /**
     * Returns whether this player is whitelisted on the server.
     *
     * <p>The returned boolean is <b>always</b> false on BungeeCord and Velocity due to a lack of any offline player data
     * storage.
     * 
     * @return Boolean indicating whether this player was banned from the server.
     */
    public boolean isWhitelisted(){
        return whitelisted;
    }
}
