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

/**
 * A simple class to allowing to have a nullable boolean without fear of encountering a NullPointerException should
 * the Boolean instance be null.
 * <br>This class simply checks if the saved value is null and if it is, serves the default boolean provided.
 */
public class NullBool{
    
    /**
     * NullBool instance with boolean value {@link true} set.
     */
    public static final NullBool TRUE = new NullBool(true);
    /**
     * NullBool instance with boolean value {@code false} set.
     */
    public static final NullBool FALSE = new NullBool(false);
    /**
     * NullBool instance with boolean value {@code null} set.
     * <br>If this NullBool is used, will {@link #getOrDefault(boolean) getValue} return whatever has been set as the default.
     */
    public static final NullBool NULL = new NullBool(null);
    
    private final Boolean value;
    
    /**
     * Creates a new NullBool instance with the provided {@link Boolean Boolean} value.
     * <br>The provided value can be {@code null} in which case {@link #getOrDefault(boolean) getValue} returns whatever
     * has been given as default.
     * 
     * @param value
     *        The {@link Boolean Boolean} value to set. Can be {@code null}
     */
    public NullBool(Boolean value){
        this.value = value;
    }
    
    /**
     * Whether this NullBool has its value set to {@code null}.
     * 
     * @return {@code true} if the value of this NullBool is {@code null}, otherwise false.
     */
    public boolean isNull(){
        return value == null;
    }
    
    /**
     * Returns the stored {@link Boolean Boolean value} if not null. Otherwise returns the provided default.
     * 
     * @param  def
     *         The default boolean to return in case the stored Boolean value is null.
     * 
     * @return {@code true} or {@code false} depending on what Boolean value was stored and what default has been provided.
     */
    public boolean getOrDefault(boolean def){
        return isNull() ? def : value;
    }
}
