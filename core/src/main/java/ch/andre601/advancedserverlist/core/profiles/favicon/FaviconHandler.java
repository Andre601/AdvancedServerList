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

package ch.andre601.advancedserverlist.core.profiles.favicon;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class FaviconHandler<F>{
    
    private final Cache<String, F> favicons = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();
    
    private final AdvancedServerList core;
    
    public FaviconHandler(AdvancedServerList core){
        this.core = core;
    }
    
    public F getFavicon(String input, Function<BufferedImage, F> function){
        return favicons.get(input, k -> {
            BufferedImage image = resolveImage(core, input);
            if(image == null)
                return null;
            
            return function.apply(image);
        });
    }
    
    public void clearCache(){
        favicons.invalidateAll();
    }
    
    private BufferedImage resolveImage(AdvancedServerList core, String input){
        InputStream stream;
        
        if(input.toLowerCase(Locale.ROOT).startsWith("https://")){
            stream = getFromUrl(core, input);
        }else
        if(input.toLowerCase(Locale.ROOT).endsWith(".png")){
            File folder = core.getPath().resolve("favicons").toFile();
            if(!folder.exists()){
                core.getPluginLogger().warn("Cannot get Favicon %s from favicons folder. Folder doesn't exist!", input);
                return null;
            }
            
            File file = new File(folder, input);
            
            try{
                stream = new FileInputStream(file);
            }catch(IOException ex){
                core.getPluginLogger().warn("Cannot create Favicon from File %s.", input);
                core.getPluginLogger().warn("Cause: %s", ex.getMessage());
                return null;
            }
        }else{
            stream = getFromUrl(core, "https://mc-heads.net/avatar/" + input + "/64");
        }
        
        if(stream == null){
            core.getPluginLogger().warn("Cannot create Favicon. InputStream was null.");
            return null;
        }
        
        try{
            BufferedImage original = ImageIO.read(stream);
            if(original == null){
                core.getPluginLogger().warn("Cannot create Favicon. Unable to create BufferedImage.");
                return null;
            }
            
            BufferedImage favicon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D img = favicon.createGraphics();
            
            // Resize original image to 64x64 and apply to favicon.
            img.drawImage(original, 0, 0, 64, 64, null);
            img.dispose();
            
            return favicon;
        }catch(IOException ex){
            core.getPluginLogger().warn("Unable to create Favicon. Encountered IOException during creation.");
            core.getPluginLogger().warn("Cause: %s", ex.getMessage());
            return null;
        }
    }
    
    private InputStream getFromUrl(AdvancedServerList core, String url){
        try{
            URL faviconUrl = new URL(url);
            URLConnection connection = faviconUrl.openConnection();
            connection.setRequestProperty("User-Agent", "AdvancedServerList/" + core.getVersion());
            connection.connect();
            
            return connection.getInputStream();
        }catch(IOException ex){
            core.getPluginLogger().warn("Error while connecting to %s for Favicon creation.", url);
            core.getPluginLogger().warn("Cause: %s", ex.getMessage());
            return null;
        }
    }
}
