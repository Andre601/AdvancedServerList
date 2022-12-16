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

import ch.andre601.advancedserverlist.api.internal.CheckUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Core class of the API for AdvancedServerList.
 * <br>Use {@link #get() get()} to retrieve the instance currently used.
 */
public class AdvancedServerListAPI{
    
    private static AdvancedServerListAPI instance;
    private final Map<String, PlaceholderProvider> placeholderProviders = new HashMap<>();
    
    private AdvancedServerListAPI(){}
    
    /**
     * Retrieves the instance used of this API.
     * <br>If no instance has been made so far will a new one be created.
     *
     * @return Instance of this API.
     */
    public static AdvancedServerListAPI get(){
        if(instance != null)
            return instance;
        
        return (instance = new AdvancedServerListAPI());
    }
    
    /**
     * Adds the provided {@link PlaceholderProvider PlaceholderProvider} to the list, if it passes the following checks:
     * <ul>
     *     <li>The identifier is not null or empty.</li>
     *     <li>The identifier does not contain any spaces.</li>
     *     <li>A PlaceholderProvider with the same identifier doesn't exist already.</li>
     * </ul>
     *
     * @param placeholderProvider
     *        The {@link PlaceholderProvider PlaceholderProvider} to add.
     */
    public void addPlaceholderProvider(PlaceholderProvider placeholderProvider){
        String identifier = placeholderProvider.getIdentifier().toLowerCase(Locale.ROOT);
        CheckUtil.notEmpty(identifier, "Identifier");
        CheckUtil.check(identifier.contains(" "), "Identifier may not contain spaces.");
        CheckUtil.check(placeholderProviders.containsKey(identifier), "A PlaceholderProvider with name " + identifier + " already exists.");
        
        placeholderProviders.put(identifier, placeholderProvider);
    }
    
    /**
     * Retrieves the {@link PlaceholderProvider PlaceholderProvider} associated with the given identifier, or {@code null}
     * should no such entry exist.
     *
     * @param  identifier
     *         The identifier to find a matching {@link PlaceholderProvider PlaceholderProvider} for.
     *
     * @return Possibly-null {@link PlaceholderProvider PlaceholderProvider instance}.
     */
    public PlaceholderProvider retrievePlaceholderProvider(String identifier){
        return placeholderProviders.get(identifier.toLowerCase(Locale.ROOT));
    }
}
