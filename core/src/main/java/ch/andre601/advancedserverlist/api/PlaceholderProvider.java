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

package ch.andre601.advancedserverlist.api;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;

/**
 * Abstract class used to register a new Placeholder provider.
 * <br>To provide your own set of placeholders to use, create a new class and extend it with this one. Then, set an identifier
 * in your class constructor.
 * 
 * <h2>Example</h2>
 * <pre>{@code
 * public MyPlaceholders(){
 *     this.identifier = "myplaceholders";
 * }
 * }</pre>
 * 
 * <h2>Note</h2>
 * It is recommended to use an already existing {@link GenericPlayer GenericPlayer instance} depending on the platform
 * your plugin runs on and what AdvancedServerList version is used:
 * <ul>
 *     <li>BungeeCord: BungeePlayer</li>
 *     <li>Paper: PaperPlayer</li>
 *     <li>Spigot: SpigotPlayer</li>
 *     <li>Velocity: VelocityPlayer</li>
 * </ul>
 * You can also use {@link ch.andre601.advancedserverlist.api.objects.DummyPlayer DummyPlayer} if your PlaceholderProvider doesn't
 * rely on any player data being present.
 * 
 * @param <P>
 *        The Object to wrap this around. Needs to extend the {@link GenericPlayer GenericPlayer class}
 */
public abstract class PlaceholderProvider<P extends GenericPlayer<?>>{
    
    protected String identifier = null;
    
    /**
     * Called by the API to receive replacement values for specific placeholders.
     * <br>Returning {@code null} will be treated as an invalid placeholder, resulting in the raw (original) one being
     * returned.
     * 
     * @param  placeholder
     *         The placeholder part from {@code ${<identifier> <placeholder>}}
     * @param  player
     *         The {@link GenericPlayer GenericPlayer instance} defined in the {@code <P>} parameter.
     * @param  server
     *         The {@link GenericServer GenericServer instance} used.
     * 
     * @return Parsed placeholder value or null for invalid values.
     */
    public abstract String parsePlaceholder(String placeholder, P player, GenericServer server);
    
    /**
     * Returns the identifier of this PlaceholderProvider.
     * 
     * @return Identifier of this PlaceholderProvider.
     */
    public String getIdentifier(){
        return identifier;
    }
}
