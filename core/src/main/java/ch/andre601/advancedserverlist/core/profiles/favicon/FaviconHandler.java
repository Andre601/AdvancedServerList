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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class FaviconHandler{
    
    private final BufferedImage image;
    
    public FaviconHandler(AdvancedServerList core, String input, String playerName){
        this.image = resolve(core, input, playerName);
    }
    
    public byte[] getAsByteArray(){
        if(image == null)
            return null;
        
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.setUseCache(false);
            ImageIO.write(image, "PNG", baos);
            
            return baos.toByteArray();
        }catch(IOException ex){
            return null;
        }
    }
    
    public BufferedImage getAsBufferedImage(){
        return image;
    }
    
    private BufferedImage resolve(AdvancedServerList core, String input, String playerName){
        InputStream stream;
        
        if(input.toLowerCase(Locale.ROOT).startsWith("https://")){
            stream = getFromUrl(core, input);
        }else
        if(input.equalsIgnoreCase("${player name}")){
            stream = getFromUrl(core, "https://mc-heads.net/avatar/" + playerName + "/64");
        }else{
            File folder = core.getPath().resolve("favicons").toFile();
            if(!folder.exists())
                return null;
    
            File file = new File(folder, input);
    
            try{
                stream = new FileInputStream(file);
            }catch(FileNotFoundException ex){
                return null;
            }
        }
        
        if(stream == null)
            return null;
        
        try{
            BufferedImage original = ImageIO.read(stream);
            if(original == null)
                return null;
            
            BufferedImage favicon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D img = favicon.createGraphics();
            
            // We use this to resize the image to 64x64 pixels.
            img.drawImage(original, 0, 0, 64, 64, null);
            img.dispose();
            
            return favicon;
        }catch(IOException ignored){
            return null;
        }
    }
    
    private InputStream getFromUrl(AdvancedServerList core, String url){
        try{
            URL avatarUrl = new URL(url);
            URLConnection connection = avatarUrl.openConnection();
            connection.setRequestProperty("User-Agent", "AdvancedServerList/" + core.getVersion());
            connection.connect();
            
            return connection.getInputStream();
        }catch(IOException ex){
            return null;
        }
    }
}
