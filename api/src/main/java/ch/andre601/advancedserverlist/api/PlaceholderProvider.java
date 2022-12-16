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
 * Abstract class that is used to provide your own Placeholder patterns for AdvancedServerList to parse.
 * <br>It is important to provide a valid identifier for the placeholder.
 */
public abstract class PlaceholderProvider{
    
    /**
     * String used for the identification of the placeholder. Cannot be null, empty or contain spaces.
     */
    protected String identifier = null;
    
    /**
     * This method is called by the StringReplacer class of AdvancedServerList to parse any appearances of
     * {@code ${identifier values}} into possible replacement values.
     * <br>AdvancedServerList does this by going through the full String and check for any placeholder pattern to appear.
     * When found will it look up any available PlaceholderProvider instance and see if the identifier matches. When it does
     * will this method be called on that provider.
     *
     * <p><b>Note!</b>
     * <br>What is returned is up to the respective PlaceholderProvider. Returning {@code null} will be seen as an
     * invalid placeholder being provided, resulting in the placeholder itself being used and not the replacement.
     *
     * @param  placeholder
     *         The part of the placeholder after the identifier ({@code ${<identifier> <placeholder>}}
     * @param  player
     *         The {@link GenericPlayer GenericPlayer instance} used.
     * @param  server
     *         The {@link GenericServer GenericServer instance} used.
     *
     * @return Parsed String based on the PlaceholderProvider or {@code null} for invalid placeholders.
     */
    public abstract String parsePlaceholder(String placeholder, GenericPlayer<?> player, GenericServer server);
    
    /**
     * Returns the identifier used by this instance.
     *
     * @return String containing the identifier used by this instance.
     */
    public String getIdentifier(){
        return identifier;
    }
}
