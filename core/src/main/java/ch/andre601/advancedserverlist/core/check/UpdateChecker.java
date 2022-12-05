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

package ch.andre601.advancedserverlist.core.check;

import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.leangen.geantyref.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UpdateChecker{
    
    private final String url = "https://api.modrinth.com/v2/project/advancedserverlist/version?loaders=[\"%s\"]";
    private final OkHttpClient client = new OkHttpClient();
    
    private final AdvancedServerList core;
    private final PluginLogger logger;
    private final String loader;
    
    private final Type listType = new TypeToken<ArrayList<ModrinthVersion>>(){}.getType();
    private final Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setPrettyPrinting()
        .setLenient()
        .create();
    
    private final Timer timer = new Timer("AdvancedServerList Update-Thread");
    
    public UpdateChecker(AdvancedServerList core){
        this.core = core;
        this.logger = core.getPlugin().getPluginLogger();
        this.loader = core.getPlugin().getLoader();
        
        startUpdateChecker();
    }
    
    public void startUpdateChecker(){
        timer.scheduleAtFixedRate(
            new TimerTask(){
                @Override
                public void run(){
                    checkUpdate().whenComplete((version, throwable) -> {
                        if(version == null || throwable != null){
                            logger.warn("Failed to look for any updates. See previous messages for reasons.");
                            return;
                        }
                        
                        int result = version.compare(core.getVersion());
                        switch(result){
                            case -2 -> {
                                logger.warn("Encountered an exception while comparing versions. Are they valid?");
                                logger.warn("Own version: %s; New version: %s", core.getVersion(), version.getVersionNumber());
                            }
                            case -1 -> logger.info("You seem to run a newer version compared to Modrinth. Are you running a dev build?");
                            case 0 -> logger.info("No new update found. You're running the latest version!");
                            case 1 -> printUpdateBanner(version.getVersionNumber(), version.getId());
                        }
                    });
                }
            },
            0L,
            TimeUnit.HOURS.toMillis(12L)
        );
    }
    
    public void disable(){
        timer.cancel();
    }
    
    private CompletableFuture<ModrinthVersion> checkUpdate(){
        return CompletableFuture.supplyAsync(() -> {
            PluginLogger logger = core.getPlugin().getPluginLogger();
            if(core.getVersion().equals("UNKNOWN")){
                logger.warn("Cannot perform Update check! Plugin version couldn't be parsed.");
                return null;
            }
            
            Request request = new Request.Builder()
                .url(String.format(url, loader))
                .header("User-Agent", "AdvancedServerList-" + loader + "/" + core.getVersion())
                .build();
            
            try(Response response = client.newCall(request).execute()){
                if(!response.isSuccessful()){
                    logger.warn("Encountered a non-successful response from Modrinth! Code: %d", response.code());
                    return null;
                }
    
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
                
                List<ModrinthVersion> list = gson.fromJson(responseString, listType);
                if(list == null || list.isEmpty()){
                    logger.warn("Couldn't convert JSON Array into a valid list.");
                    return null;
                }
                
                ModrinthVersion version = list.get(0);
                if(version.getVersionNumber() == null || version.getVersionNumber().isEmpty()){
                    logger.warn("Cannot check latest version. Received version number was null/empty.");
                    return null;
                }
                
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
    
    private void printUpdateBanner(String version, String versionId){
        logger.info("==================================================================");
        logger.info("You are running an outdated version of AdvancedServerList!");
        logger.info("");
        logger.info("Your version: %s", core.getVersion());
        logger.info("Modrinth version: %s", version);
        logger.info("");
        logger.info("You can download the latest release from here:");
        logger.info("https://modrinth.com/plugin/advancedserverlist/version/%s", versionId);
        logger.info("==================================================================");
    }
    
    public static class ModrinthVersion{
        private String id;
        private String versionNumber;
        
        public ModrinthVersion(String id, String versionNumber){
            this.id = id;
            this.versionNumber = versionNumber;
        }
    
        public String getId(){
            return id;
        }
    
        public String getVersionNumber(){
            return versionNumber;
        }
        
        public int compare(String version){
            String[] oldParts = version.split("\\.");
            String[] newParts = versionNumber.split("\\.");
            int length = Math.max(oldParts.length, newParts.length);
            
            for(int i = 0; i < length; i++){
                try{
                    int oldPart = i < oldParts.length ? Integer.parseInt(oldParts[i]) : 0;
                    int newPart = i < newParts.length ? Integer.parseInt(newParts[i]) : 0;
                    
                    if(oldPart < newPart)
                        return 1;
                    
                    if(oldPart > newPart)
                        return -1;
                }catch(NumberFormatException ex){
                    return -2;
                }
            }
            return 0;
        }
    }
}
