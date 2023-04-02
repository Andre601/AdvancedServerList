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

package ch.andre601.advancedserverlist.hangaruploader;

import java.util.List;
import java.util.Map;

public record VersionUpload(String version, Map<Platform, List<PluginDependency>> pluginDependencies,
                            Map<Platform, List<String>> platformDependencies, String description,
                            List<MultipartFileOrUrl> files, String channel){
    
    public record Namespace(String owner, String slug){
        
        @Override
        public String toString(){
            return this.owner + "/" + this.slug;
        }
    }
    
    public record PluginDependency(String name, boolean required, Namespace namespace, String externalUrl){
        
        public PluginDependency{
            if(namespace == null && externalUrl == null)
                throw new IllegalStateException("Either a Hangar Namespace or an external URL needs to be defined.");
        }
        
        public static PluginDependency createWithHangarNamespace(String name, boolean required, Namespace namespace){
            return new PluginDependency(name, required, namespace, null);
        }
        
        public static PluginDependency createWithExternalUrl(String name, boolean required, String externalUrl){
            return new PluginDependency(name, required, null, externalUrl);
        }
    }
    
    public record MultipartFileOrUrl(List<Platform> platforms, String externalUrl){}
    
    public enum Platform{
        PAPER,
        WATERFALL,
        VELOCITY
    }
}
