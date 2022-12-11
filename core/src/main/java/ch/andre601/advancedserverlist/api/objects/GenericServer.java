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

/**
 * A basic class to wrap some simple Server/Proxy information into.
 * <br>An instance is created whenever a player pings the Server/Proxy while AdvancedServerList is running.
 */
public class GenericServer{
    
    private final int playersOnline;
    private final int playersMax;
    private final String host;
    
    public GenericServer(int online, int max, String host){
        this.playersOnline = online;
        this.playersMax = max;
        this.host = host;
    }
    
    /**
     * Returns the amount of players online.
     * 
     * @return Number of online players.
     */
    public int getPlayersOnline(){
        return playersOnline;
    }
    
    /**
     * Returns the amount of players that can join this Server/Proxy.
     *
     * @return Number of players total.
     */
    public int getPlayersMax(){
        return playersMax;
    }
    
    /**
     * Returns the host (domain/IP) that has been pinged by the player. Can be null
     *
     * @return Possibly-null String containing the domain/IP pinged by the player.
     */
    public String getHost(){
        return host;
    }
}
