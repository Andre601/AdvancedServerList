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
import com.google.gson.JsonSyntaxException;
import com.unascribed.flexver.FlexVerComparator;
import io.leangen.geantyref.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateChecker{
    
    private final String url = "https://api.modrinth.com/v2/project/advancedserverlist/version?loaders=[\"%s\"]";
    private final OkHttpClient client = new OkHttpClient();
    
    private final AdvancedServerList<?> core;
    private final PluginLogger logger;
    private final String loader;
    
    private final Type listType = new TypeToken<ArrayList<ModrinthVersion>>(){}.getType();
    private final Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setPrettyPrinting()
        .setLenient()
        .create();
    
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new UpdateCheckThread());
    
    public UpdateChecker(AdvancedServerList<?> core){
        this.core = core;
        this.logger = core.getPlugin().getPluginLogger();
        this.loader = core.getPlugin().getLoader();
        
        startUpdateChecker();
    }
    
    public void startUpdateChecker(){
        executor.scheduleAtFixedRate(() -> {
            logger.info("Checking for a new update...");
            checkUpdate().whenComplete((version, throwable) -> {
                if(version == null || throwable != null){
                    logger.warn("Failed to look for any updates. See previous messages for reasons.");
                    return;
                }
                
                logger.debug(UpdateChecker.class, "Comparing versions...");
                
                int result = version.compare(core.getVersion());
                switch(result){
                    case -1 -> printUpdateBanner(version);
                    case 0 -> logger.info("No new update found. You're running the latest version!");
                    case 1 -> logger.info(
                        "Your version (%s) is higher than the latest release (%s). Are you running a dev build?",
                        core.getVersion(), version.getVersionNumber()
                    );
                }
            });
        }, 0L, 12L, TimeUnit.HOURS);
    }
    
    public void disable(){
        executor.shutdown();
        client.dispatcher().executorService().shutdown();
        try{
            if(!executor.awaitTermination(1, TimeUnit.SECONDS)){
                executor.shutdownNow();
                if(!executor.awaitTermination(1, TimeUnit.SECONDS))
                    logger.warn("Scheduler didn't terminate in time!");
            }
            
            if(!client.dispatcher().executorService().awaitTermination(1, TimeUnit.SECONDS)){
                client.dispatcher().executorService().shutdownNow();
                if(!client.dispatcher().executorService().awaitTermination(1, TimeUnit.SECONDS))
                    logger.warn("OkHttp's Scheduler didn't terminate in time!");
            }
        }catch(InterruptedException ex){
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private CompletableFuture<ModrinthVersion> checkUpdate(){
        return CompletableFuture.supplyAsync(() -> {
            PluginLogger logger = core.getPlugin().getPluginLogger();
            String finalUrl = String.format(url, loader);
            
            logger.debug(UpdateChecker.class, "Looking for updates on '%s'...", finalUrl);
            
            if(core.getVersion().equals("UNKNOWN")){
                logger.warn("Cannot perform Update check! Plugin version couldn't be parsed.");
                return null;
            }
            
            Request request = new Request.Builder()
                .url(finalUrl)
                .header("User-Agent", "AdvancedServerList-" + loader + "/" + core.getVersion())
                .build();
            
            logger.debug(UpdateChecker.class, "Connecting to website...");
            
            try(Response response = client.newCall(request).execute()){
                logger.debug(UpdateChecker.class, "Connected! Checking response code...");
                
                if(!response.isSuccessful()){
                    logger.warn("Encountered a non-successful response from Modrinth! Code: %d", response.code());
                    return null;
                }
                
                logger.debug(UpdateChecker.class, "Received positive response code '%d'", response.code());
    
                ResponseBody body = response.body();
                if(body == null){
                    logger.warn("Received an empty Response body from Modrinth!");
                    return null;
                }
                
                String responseString = body.string();
                if(responseString.isEmpty()){
                    logger.warn("Received an empty Response body from Modrinth!");
                    return null;
                }
                
                logger.debug(UpdateChecker.class, "Received non-empty response body. Parsing it from JSON Array...");
                
                List<ModrinthVersion> list = gson.fromJson(responseString, listType);
                if(list == null || list.isEmpty()){
                    logger.warn("Couldn't convert JSON Array into a valid list.");
                    return null;
                }
                
                ModrinthVersion version = list.get(0);
                
                logger.debug(UpdateChecker.class, "Found entry '%s'", version);
                
                if(version.getVersionNumber() == null || version.getVersionNumber().isEmpty()){
                    logger.warn("Cannot check latest version. Received version number was null/empty.");
                    return null;
                }
                
                logger.debug(UpdateChecker.class, "Successfully parsed version!");
                
                return version;
            }catch(JsonSyntaxException ex){
                logger.warn("Encountered invalid JSON from Response body!", ex);
                return null;
            }catch(IOException ex){
                logger.warn("Encountered Exception while checking for an update!", ex);
                return null;
            }
        });
    }
    
    private void printUpdateBanner(ModrinthVersion version){
        logger.info("==================================================================");
        logger.info("You are running an outdated version of AdvancedServerList!");
        logger.info("");
        logger.info("Your version: %s", core.getVersion());
        logger.info("Modrinth version: %s", version.getVersionNumber());
        logger.info("");
        
        if(!version.isRelease()){
            logger.info("WARNING: This release is an Alpha/Beta! It may contain");
            logger.info("         breaking changes!");
            logger.info("");
        }
        
        logger.info("You can download the latest release from here:");
        logger.info("https://modrinth.com/plugin/advancedserverlist/version/%s", version.getId());
        logger.info("==================================================================");
    }
    
    public static class ModrinthVersion{
        @SuppressWarnings("FieldMayBeFinal")
        private String id;
        @SuppressWarnings("FieldMayBeFinal")
        private String versionNumber;
        @SuppressWarnings("FieldMayBeFinal")
        private String versionType;
        
        public ModrinthVersion(String id, String versionNumber, String versionType){
            this.id = id;
            this.versionNumber = versionNumber;
            this.versionType = versionType;
        }
    
        public String getId(){
            return id;
        }
    
        public String getVersionNumber(){
            return versionNumber;
        }
        
        public boolean isRelease(){
            return versionType.equals("release");
        }
        
        public int compare(String version){
            return FlexVerComparator.compare(version, versionNumber);
        }
        
        @Override
        public String toString(){
            return String.format("ModrinthVersion{id=%s,versionNumber=%s,versionType=%s}", id, versionNumber, versionNumber);
        }
    }
}
