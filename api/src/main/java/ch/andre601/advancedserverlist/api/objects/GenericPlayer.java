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
 * Abstract class used to wrap around some generic player data and a Player instance itself.
 * <br>AdvancedServerList already provides its own set of players to use based on the platform:
 * <ul>
 *     <li>BungeeCord: BungeePlayer*</li>
 *     <li>Paper: PaperPlayer</li>
 *     <li>Spigot: SpigotPlayer</li>
 *     <li>Velocity: VelocityPlayer*</li>
 * </ul>
 * *These instances use the DummyPlayer instance as they don't have any persistent player data for offline players.
 *
 * @param <T>
 *        The Player instance to wrap this class around.
 */
public abstract class GenericPlayer<T>{
    
    protected T player = null;
    protected String name = null;
    protected String version = "version";
    protected int protocol = -1;
    protected boolean playedBefore = false;
    protected boolean banned = false;
    protected boolean whitelisted = false;
    protected UUID uuid = null;
    
    /**
     * Returns an instance of the Player used for the GenericPlayer instance.
     * <br>This is only used for the Spigot and Paper version as those allow the retrieval of offline player data
     * through their OfflinePlayer class.
     *
     * @return Possibly-null instance of the Player type used for this GenericPlayer.
     */
    public T getPlayer(){
        return player;
    }
    
    /**
     * Gets the name of the player.
     * <br>On BungeeCord and velocity will this be whatever name AdvancedServerList has last saved to the cache. On Spigot
     * and Paper can this be of an OfflinePlayer (if present) or the same as with BungeeCord and Velocity.
     *
     * @return String containing the name of the player.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns a String containing the readable MC version this player is using.
     * <br>This is only used on Velocity and will return {@code null} for the other Player types.
     *
     * @return Possibly-null String containing the MC version the player is using.
     */
    public String getVersion(){
        return version;
    }
    
    /**
     * Returns the numerical id of the Minecraft protocol version.
     * <br>Each release has its own unique protocol version represented by an integer.
     *
     * @return Integer representing the numerical protocol version the player uses.
     */
    public int getProtocol(){
        return protocol;
    }
    
    /**
     * Returns whether this player has played on the server before.
     * <br>This is only usable on Spigot and Paper as they keep offline player data to use.
     *
     * @return Whether this player has played before on this server. Always false for BungeeCord and Velocity.
     */
    public boolean hasPlayedBefore(){
        return playedBefore;
    }
    
    /**
     * Returns whether this player has been banned from this server.
     * <br>This is only usable on Spigot and Paper as they keep offline player data to use.
     *
     * @return Whether this player has played before on this server. Always false for BungeeCord and Velocity.
     */
    public boolean isBanned(){
        return banned;
    }
    
    /**
     * Returns whether this player is whitelisted on this server.
     * <br>This is only usable on Spigot and Paper as they keep offline player data to use.
     *
     * @return Whether this player is whitelisted on this server. Always false for BungeeCord and Velocity.
     */
    public boolean isWhitelisted(){
        return whitelisted;
    }
    
    /**
     * Returns the UUID of this player.
     * <br>The UUID will be retrieved from the plugin's player cache. If not present will a default value be used.
     *
     * @return UUID from the player.
     */
    public UUID getUuid(){
        return uuid;
    }
}
