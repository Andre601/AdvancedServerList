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

package ch.andre601.advancedserverlist.core.check;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unascribed.flexver.FlexVerComparator;
import io.leangen.geantyref.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateChecker{
    
    @SuppressWarnings("FieldCanBeLocal")
    // 'https://api.modrinth.com/v2/project/advancedserverlist/version?loaders=["%s"]' URL encoded.
    private final String url = "https://api.modrinth.com/v2/project/advancedserverlist/version?loaders=%%5B%%22%s%%22%%5D";
    private final HttpClient client = HttpClient.newHttpClient();
    
    private final Type listType = new TypeToken<ArrayList<ModrinthVersion>>(){}.getType();
    private final Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setPrettyPrinting()
        .setLenient()
        .create();
    
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new UpdateCheckThread());
    
    private final AdvancedServerList<?> core;
    private final PluginLogger logger;
    private final String loader;
    
    public UpdateChecker(AdvancedServerList<?> core){
        this.core = core;
        this.logger = core.getPlugin().getPluginLogger();
        this.loader = core.getPlugin().getLoader();
        
        startUpdateChecker();
    }
    
    public void disable(){
        executor.shutdown();
        try{
            if(!executor.awaitTermination(1, TimeUnit.SECONDS)){
                executor.shutdownNow();
                if(!executor.awaitTermination(1, TimeUnit.SECONDS))
                    logger.warn("Scheduler did not terminate in time!");
            }
        }catch(InterruptedException ex){
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private void startUpdateChecker(){
        executor.scheduleAtFixedRate(() -> {
            logger.info("Looking for an update...");
            
            performUpdateCheck().whenComplete((version, throwable) -> {
                if(version == null || throwable != null){
                    logger.warn("Failed to look for any updates. Check previous messages for causes.");
                    return;
                }
                
                logger.debug(UpdateChecker.class, "Comparing versions...");
                
                int result = version.compare(core.getVersion());
                switch(result){
                    case -1 -> printUpdateBanner(version);
                    case 0 -> logger.info("You are running the latest version!");
                    case 1 -> logger.info(
                        "Your version (%s) seems to be newer than the latest release (%s). Are you running a dev build?",
                        core.getVersion(), version.versionNumber()
                    );
                }
            });
        }, 0L, 12L, TimeUnit.HOURS);
    }
    
    private CompletableFuture<ModrinthVersion> performUpdateCheck(){
        String finalUrl = String.format(url, loader);
        
        logger.debug(UpdateChecker.class, "Checking '%s' for updates...", finalUrl);
        
        if(core.getVersion().equals("UNKNOWN")){
            logger.warn("Cannot perform update check. Plugin version is 'UNKNOWN'!");
            return CompletableFuture.completedFuture(null);
        }
        
        HttpRequest request;
        try{
            request = HttpRequest.newBuilder()
                .uri(new URI(finalUrl))
                .header("User-Agent", "AdvancedServerList-" + loader + "/" + core.getVersion())
                .build();
        }catch(URISyntaxException ex){
            logger.warn("Cannot perform update check. URL '%s' is not a valid URI.", ex, finalUrl);
            return CompletableFuture.completedFuture(null);
        }
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
            .thenApply(response -> new ArrayList<ModrinthVersion>(gson.fromJson(response.body(), listType)))
            .thenApply(list -> list.get(0))
            .exceptionally((throwable) -> {
                logger.warn("Encountered exception while performing update check!", throwable);
                return null;
            });
    }
    
    private void printUpdateBanner(ModrinthVersion version){
        logger.info("==================================================================");
        logger.info("You are running an outdated version of AdvancedServerList!");
        logger.info("");
        logger.info("Your version: %s", core.getVersion());
        logger.info("Modrinth:     %s", version.versionNumber());
        logger.info("");
        
        if(!version.isRelease()){
            logger.info("WARNING!");
            logger.info("This is a %s version and may contain breaking changes and/or bugs!", version.versionType());
            logger.info("");
        }
        
        logger.info("Download the latest version from here:");
        logger.info("https://modrinth.com/plugin/advancedserverlist/version/%s", version.id());
        logger.info("==================================================================");
    }
    
    public record ModrinthVersion(String id, String versionNumber, String versionType){
        
        public boolean isRelease(){
            return versionType().equals("release");
        }
        
        public int compare(String version){
            return FlexVerComparator.compare(version, versionNumber());
        }
        
        @Override
        public String toString(){
            return String.format("ModrinthVersion{id=%s,versionNumber=%s,versionType=%s}", id, versionNumber, versionType);
        }
    }
}
