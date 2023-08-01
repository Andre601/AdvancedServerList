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

package ch.andre601.advancedserverlist.hangaruploader.version;

public record Dependency(String name, boolean required, Namespace namespace, String externalUrl){
    
    public Dependency{
        if(namespace == null && externalUrl == null)
            throw new IllegalStateException("Either a namespace or external URL needs to be defined.");
    }
    
    public static Dependency fromNamespace(String name, boolean required, Namespace namespace){
        return new Dependency(name, required, namespace, null);
    }
    
    public static Dependency fromUrl(String name, boolean required, String url){
        return new Dependency(name, required, null, url);
    }
}
