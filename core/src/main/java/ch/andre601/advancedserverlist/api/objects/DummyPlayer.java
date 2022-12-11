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
 * Placeholder class used for Platforms that do not allow to retrieve data from specific players that aren't online such
 * as BungeeCord and Velocity.
 * <br>It is also used in the {@link ch.andre601.advancedserverlist.api.internals.placeholders.ServerPlaceholders ServerPlaceholders class}
 * as it doesn't require any player data.
 * 
 * <p>Feel free to use this class in your {@link ch.andre601.advancedserverlist.api.PlaceholderProvider PlaceholderProvider instance}
 * should you not need any player data from the server.
 */
public class DummyPlayer extends GenericPlayer<Object>{
    public DummyPlayer(){
        this.name = "dummy";
        this.protocol = 0;
    }
}
