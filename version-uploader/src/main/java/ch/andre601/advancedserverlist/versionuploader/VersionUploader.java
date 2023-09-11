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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionUploader{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionUploader.class);
    
    public static void main(String[] args) {
        LOGGER.info("Starting release uploader...");
        
        CodebergRelease release = CodebergReleaseFetcher.fetchRelease();
        if(release == null){
            LOGGER.warn("Non-successful attempt at fetching a release from Codeberg! Aborting upload");
            System.exit(1);
            return;
        }
        
        if(release.tagName() == null || release.tagName().isEmpty()){
            LOGGER.info("Received invalid Release Structure. Tag name was null/empty.");
            System.exit(1);
            return;
        }
        
        // Gson uses 0 for nummerics by default
        if(release.id() == 0){
            LOGGER.info("Received invalid Release Structure. ID was 0.");
            System.exit(1);
            return;
        }
        
        LOGGER.info("Successfully obtained release info: [ID: {}, Tag: {}, Prerelease: {}]", release.id(), release.tagName(), release.prerelease());
        
        for(String arg : args){
            if(arg.equalsIgnoreCase("--modrinth")){
                new ModrinthVersionUploader().performUploads(release);
                break;
            }
            if(arg.equalsIgnoreCase("--hangar")){
                new HangarVersionUploader().performUpload(release);
                break;
            }
        }
    }
}
