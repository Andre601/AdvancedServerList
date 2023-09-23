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

package ch.andre601.advancedserverlist.versionuploader.data;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CodebergReleaseFetcher{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CodebergReleaseFetcher.class);
    
    private static final Gson GSON = new Gson();
    
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();
    
    public static CodebergRelease fetch(){
        String tag = System.getenv("CI_COMMIT_TAG");
        String codbergToken = System.getenv("CODEBERG_API_TOKEN");
        
        if(tag == null || codbergToken == null || tag.isEmpty() || codbergToken.isEmpty()){
            LOGGER.warn("Cannot fetch release data. Either Tag or Codeberg token was null/empty.");
            return null;
        }
        
        String url = "https://codeberg.org/api/v1/repos/Andre601/AdvancedServerList/releases/tags/" + tag;
        LOGGER.info("Fetching Release data from {}...", url);
        
        Request request = new Request.Builder()
            .url(url)
            .header("User-Agent", "VersionUploader-CodebergReleaseFetcher/v1.0.0")
            .header("Accept", "application/json")
            .header("Authorization", "token " + codbergToken)
            .build();
        
        try(Response response = CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                LOGGER.warn("Received non-successful response code from Codeberg API:");
                LOGGER.warn("Response Code:    {}", response.code());
                LOGGER.warn("Response Message: {}", response.message());
                return null;
            }
            
            ResponseBody body = response.body();
            if(body == null){
                LOGGER.warn("Received null response body while fetching Codeberg Release.");
                return null;
            }
            
            String bodyString = body.string();
            if(bodyString.isEmpty()){
                LOGGER.warn("Received empty response body while fetching Codeberg Release.");
                return null;
            }
            
            return GSON.fromJson(bodyString, CodebergRelease.class);
        }catch(IOException ex){
            LOGGER.warn("Encountered an IOException while fetching Codeberg Release data.", ex);
            return null;
        }
    }
}
