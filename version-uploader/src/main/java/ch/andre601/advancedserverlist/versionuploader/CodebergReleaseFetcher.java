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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CodebergReleaseFetcher{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CodebergReleaseFetcher.class);
    
    private static final Gson GSON = new Gson();
    private static final Type LIST_TYPE = new TypeToken<ArrayList<CodebergRelease>>(){}.getType();
    
    private static final OkHttpClient CLIENT = new OkHttpClient();
    
    public static CodebergRelease fetchRelease(){
        String tag = System.getenv("CI_COMMIT_TAG");
        String authToken = System.getenv("CB_TOKEN");
        
        if(tag == null || authToken == null || tag.isEmpty() || authToken.isEmpty()){
            LOGGER.warn("Cannot fetch release data. Either CI_COMMIT_TAG or CB_TOKEN was null or empty.");
            return null;
        }
        
        Request request = new Request.Builder()
            .url("https://codeberg.org/api/v1/repos/Andre601/AdvancedServerList/releases?limit=10")
            .header("User-Agent", "VersionUploader-CodebergFetcher/${plugin.version}")
            .header("Accept", "application/json")
            .header("Authorization", "token " + authToken)
            .build();
        
        try(Response response = CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                LOGGER.warn("Cannot fetch releases. Received non-successful response-code: {}", response.code());
                LOGGER.warn("Message: {}", response.message());
                return null;
            }
            
            ResponseBody body = response.body();
            if(body == null){
                LOGGER.warn("Cannot fetch release info. Retrieved Response body was null");
                return null;
            }
            
            String bodyString = body.string();
            if(bodyString.isEmpty()){
                LOGGER.warn("Cannot fetch release info. Retrieved Response body was null");
                return null;
            }
            
            List<CodebergRelease> releases = GSON.fromJson(bodyString, LIST_TYPE);
            
            CodebergRelease release = null;
            for(CodebergRelease tmp : releases){
                if(tmp.tagName().equals(tag)){
                    release = tmp;
                    break;
                }
            }
            
            return release;
        }catch(IOException ex){
            return null;
        }
    }
}
