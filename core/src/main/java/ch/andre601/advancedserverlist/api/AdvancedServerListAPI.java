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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Main class for the AdvancedServerList API.
 * <br>Use {@link #get() AdvancedServerListAPI.get()} to retrieve the currently used instance of this API. 
 */
public class AdvancedServerListAPI{
    
    private static AdvancedServerListAPI instance;
    private final Map<String, PlaceholderProvider<?>> placeholderProviders = new HashMap<>();
    
    private AdvancedServerListAPI(){}
    
    public static AdvancedServerListAPI get(){
        if(instance != null)
            return instance;
        
        return (instance = new AdvancedServerListAPI());
    }
    
    /**
     * Registers a new {@link PlaceholderProvider PlaceholderProvider} to manage placeholders.<br>
     * This will effectively create a new <code>${&lt;identifier&gt; &lt;placeholder&gt;}</code> placeholder pattern
     * with the PlaceholderProvider's identifier as the key.
     * 
     * <p>Note: The Placeholder provider will only be registered under the following conditions:
     * <ul>
     *     <li>The {@link PlaceholderProvider#getIdentifier() identifier} is not {@code null} or empty</li>
     *     <li>The API doesn't have a PlaceholderProvider with this identifier already.</li>
     * </ul>
     * 
     * @param provider
     *        The PlaceholderProvider instance to register.
     */
    public void addPlaceholderProvider(PlaceholderProvider<?> provider){
        if(provider.getIdentifier() == null || provider.getIdentifier().isEmpty()){
            throw new IllegalStateException("Identifier may not be empty or null.");
        }
        
        if(placeholderProviders.containsKey(provider.getIdentifier().toLowerCase(Locale.ROOT))){
            throw new IllegalStateException("A PlaceholderProvider with identifier " + provider.identifier + " has already been registered.");
        }
        
        placeholderProviders.put(provider.getIdentifier().toLowerCase(Locale.ROOT), provider);
    }
    
    /**
     * Returns a Set of currently registered Identifier Strings.
     * 
     * @return Set containing currently registered Identifier Strings.
     */
    public Set<String> getRegisteredIdentifiers(){
        return placeholderProviders.keySet();
    }
    
    /**
     * Retrieves the currently registered instance of a {@link PlaceholderProvider PlaceholderProvider} matching the
     * provided identifier.
     * <br>This may return {@code null} if no entry with the matching identifier exists.
     * 
     * @param  identifier
     *         Identifier to find the matching PlaceholderProvider for.
     * 
     * @return Possibly-null PlaceholderProvider instance.
     */
    public PlaceholderProvider<?> retrievePlaceholderProvider(String identifier){
        return placeholderProviders.get(identifier);
    }
}
