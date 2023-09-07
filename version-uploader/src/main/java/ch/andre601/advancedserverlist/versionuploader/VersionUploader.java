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

package ch.andre601.advancedserverlist.versionuploader;

import ch.andre601.advancedserverlist.versionuploader.hangar.HangarVersionUploader;
import ch.andre601.advancedserverlist.versionuploader.modrinth.ModrinthVersionUploader;

public class VersionUploader{
    
    public static void main(String[] args) {
        for(String arg : args){
            if(arg.equalsIgnoreCase("--modrinth")){
                new ModrinthVersionUploader().performUploads();
                break;
            }
            if(arg.equalsIgnoreCase("--hangar")){
                new HangarVersionUploader().performUpload();
                break;
            }
        }
    }
    
    public static String getVersion(){
        String version = System.getenv("CI_COMMIT_TAG");
        if(version == null || version.isEmpty())
            return null;
        
        return version.startsWith("v") ? version.substring(1) : version;
    }
    
    public static String getChangelog(){
        String changelog = System.getenv("PLUGIN_CHANGELOG");
        if(changelog == null || changelog.isEmpty())
            return "*No changelog provided*";
        
        return changelog;
    }
    
    public static boolean getPreReleaseState(){
        String preRelease = System.getenv("PLUGIN_PRE_RELEASE");
        if(preRelease == null || preRelease.isEmpty())
            return false;
        
        return preRelease.equalsIgnoreCase("true");
    }
}
