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

package ch.andre601.advancedserverlist.core.profiles.favicon;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class FaviconHandler<F>{
    
    private final AdvancedServerList<F> core;
    private final PluginLogger logger;
    private final ThreadPoolExecutor faviconThreadPool;
    private final Cache<String, CompletableFuture<F>> faviconCache;
    
    private final Map<String, F> localFavicons = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();
    
    public FaviconHandler(AdvancedServerList<F> core){
        this.core = core;
        this.logger = core.getPlugin().getPluginLogger();
        this.faviconThreadPool = createFaviconThreadPool();
        this.faviconCache = CacheBuilder.newBuilder()
            .expireAfterWrite(core.getFileHandler().getLong(1, 1, "faviconCacheTime"), TimeUnit.MINUTES)
            .build();
        
        loadLocalFavicons();
    }
    
    public F getFavicon(String input){
        if(localFavicons.containsKey(input.toLowerCase(Locale.ROOT).split("\\.")[0])){
            return localFavicons.get(input.toLowerCase(Locale.ROOT).split("\\.")[0]);
        }
        
        try{
            return faviconCache.get(input, () -> getFuture(input)).getNow(null);
        }catch(ExecutionException ex){
            logger.warn("Received ExecutionException while retrieving Favicon for '%s'.", ex, input);
            return null;
        }
    }
    
    public void cleanCache(){
        faviconCache.invalidateAll();
        loadLocalFavicons();
    }
    
    private CompletableFuture<F> getFuture(String input){
        if(input.toLowerCase(Locale.ROOT).startsWith("https://")){
            logger.debug(FaviconHandler.class, "Resolving URL '%s'...", input);
            return CompletableFuture.supplyAsync(() -> fromURL(core, input), this.faviconThreadPool);
        }else
        if(input.toLowerCase(Locale.ROOT).endsWith(".png")){
            logger.debug(FaviconHandler.class, "Resolving image file '%s'...", input);
            return CompletableFuture.completedFuture(localFavicons.get(input.toLowerCase(Locale.ROOT).split("\\.")[0]));
        }else{
            logger.debug(FaviconHandler.class, "Resolving Name/UUID as https://mc-heads.net/avatar/%s/64...", input);
            return CompletableFuture.supplyAsync(() -> fromURL(core, "https://mc-heads.net/avatar/" + input + "/64"), this.faviconThreadPool);
        }
    }
    
    private void loadLocalFavicons(){
        this.localFavicons.clear();
        
        logger.info("Loading local image files as Favicons...");
        
        Path folder = core.getPlugin().getFolderPath().resolve("favicons");
        if(!Files.exists(folder)){
            try{
                Files.createDirectories(folder);
                logger.info("Created favicons folder.");
            }catch(IOException ex){
                logger.warn("Cannot create favicons folder. Encountered an IOException.", ex);
                return;
            }
        }
        
        try(Stream<Path> pathStream = Files.list(folder)){
            pathStream.filter(Files::isRegularFile)
                .filter(file -> file.getFileName().toString().endsWith(".png"))
                .forEach(this::loadFile);
        }catch(IOException ex){
            logger.warn("Cannot load files from Favicons folder.", ex);
        }
    }
    
    private void loadFile(Path path){
        try(InputStream stream = Files.newInputStream(path)){
            F favicon = createFavicon(stream);
            if(favicon == null){
                logger.warn("Cannot create Favicon from file '%s'. Received Favicon was null.", path.getFileName().toString());
                return;
            }
            
            localFavicons.put(path.getFileName().toString().split("\\.")[0], favicon);
            logger.info("Loaded file '%s' as Favicon.", path.getFileName().toString());
        }catch(IOException ex){
            logger.warn("Cannot create Favicon from file '%s'. Encountered IOException.", ex);
        }
    }
    
    private F fromURL(AdvancedServerList<F> core, String url){
        try{
            logger.debug(FaviconHandler.class, "Creating Request for URL '%s'...", url);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("User-Agent", "AdvancedServerList-" + core.getPlugin().getLoader() + "/" + core.getVersion())
                .build();
            
            try(InputStream stream = client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body()){
                return createFavicon(stream);
            }catch(InterruptedException | IOException ex){
                logger.warn("Cannot create Favicon from URL '%s'. Encountered an Exception.", ex, url);
                return null;
            }
        }catch(URISyntaxException ex){
            logger.warn("Cannot create Favicon from URL '%s'. Encountered an IOException.", ex, url);
            return null;
        }
    }
    
    private F createFavicon(InputStream stream){
        try{
            logger.debug(FaviconHandler.class, "Creating BufferedImage from InputStream...");
            BufferedImage original = ImageIO.read(stream);
            if(original == null){
                logger.warn("Cannot create Favicon. Received null BufferedImage.");
                return null;
            }
            
            // Don't waste resources resizing images already having right size.
            if(original.getWidth() == 64 && original.getHeight() == 64){
                logger.debug(FaviconHandler.class, "BufferedImage is 64x64 pixels. No resizing needed.");
                return core.getPlugin().createFavicon(original);
            }
            
            logger.debug(FaviconHandler.class, "Resizing image to 64x64 pixels...");
            BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = image.createGraphics();
            
            graphics2D.drawImage(original, 0, 0, 64, 64, null);
            graphics2D.dispose();
            
            logger.debug(FaviconHandler.class, "Image resized! Returning Favicon...");
            return core.getPlugin().createFavicon(image);
        }catch(Exception ex){
            logger.warn("Unable to create Favicon. Received Exception.", ex);
            return null;
        }
    }
    
    private ThreadPoolExecutor createFaviconThreadPool(){
        return new ThreadPoolExecutor(3, 3, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024), r -> {
            Thread t = new Thread(r, "AdvancedServerList-FaviconThread");
            t.setDaemon(true);
            return t;
        });
    }
}
