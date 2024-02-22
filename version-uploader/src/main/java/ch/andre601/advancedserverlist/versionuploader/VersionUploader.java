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

import ch.andre601.advancedserverlist.versionuploader.data.CodebergRelease;
import ch.andre601.advancedserverlist.versionuploader.data.CodebergReleaseFetcher;
import ch.andre601.advancedserverlist.versionuploader.hangar.HangarVersionUploader;
import ch.andre601.advancedserverlist.versionuploader.modrinth.ModrinthVersionUploader;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class VersionUploader{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionUploader.class);
    
    public static void main(String[] args) {
        LOGGER.info("Starting release uploader...");
        
        if(args.length == 0){
            LOGGER.warn("MISSING ARGUMENT!");
            LOGGER.warn("COMMAND USAGE: java -jar VersionUploader.jar <--all|--modrinth|--hangar> [--dryrun]");
            System.exit(1);
            return;
        }
        
        CodebergRelease release = CodebergReleaseFetcher.fetch();
        
        if(release == null){
            LOGGER.warn("Couldn't fetch Codeberg Release information!");
            System.exit(1);
            return;
        }
        
        boolean dryrun = false;
        if(args.length >= 2){
            dryrun = args[1].equalsIgnoreCase("--dryrun");
        }
        
        CompletableFuture<?> future;
        ModrinthRelease modrinthRelease = null;
        switch(args[0].toLowerCase(Locale.ROOT)){
            case "--modrinth" -> {
                modrinthRelease = new ModrinthRelease();
                future = new ModrinthVersionUploader().performUpload(release, modrinthRelease, dryrun);
            }
            case "--hangar" -> future = new HangarVersionUploader().performUpload(release, dryrun);
            case "--all" -> {
                modrinthRelease = new ModrinthRelease();
                future = CompletableFuture.allOf(
                    new ModrinthVersionUploader().performUpload(release, modrinthRelease, dryrun),
                    new HangarVersionUploader().performUpload(release, dryrun)
                );
            }
            default -> {
                LOGGER.warn("Unknown argument '{}' provided. Supported are '--all', '--modrinth' or '--hangar'", args[0]);
                System.exit(1);
                return;
            }
        }
        
        if(future == null){
            LOGGER.warn("Issue while performing upload. Received CompletableFuture is null.");
            System.exit(1);
            return;
        }
        
        try{
            future.join();
            if(dryrun){
                LOGGER.info("Dryrun complete!");
            }else{
                LOGGER.info("Upload completed!");
            }
        }catch(Exception ex){
            LOGGER.warn("Upload was not successful! Encountered an Exception!", ex);
            System.exit(1);
        }
        
        if(modrinthRelease == null || modrinthRelease.getReleases().isEmpty()){
            LOGGER.warn("No Modrinth release info on Discord to share... Skipping.");
            return;
        }
        
        String webhookUrl = System.getenv("DISCORD_WEBHOOK_URL");
        if(webhookUrl == null || webhookUrl.isEmpty()){
            LOGGER.warn("No valid DISCORD_WEBHOOK_URL Environment Variable found. Skipping Webhook sending.");
            return;
        }
        
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder()
            .setTitle(new WebhookEmbed.EmbedTitle("New Release", null))
            .setDescription("A new release of AdvancedServerList is available on Modrinth for you to download!")
            .setColor(0x1BD96A);
        
        for(Map.Entry<String, String> entry : modrinthRelease.getReleases().entrySet()){
            builder.addField(new WebhookEmbed.EmbedField(
                false,
                entry.getKey(),
                "https://modrinth.com/plugin/advancedserverlist/version/" + entry.getValue()
            ));
        }
        
        try(WebhookClient client = new WebhookClientBuilder(webhookUrl).build()){
            CompletableFuture<ReadonlyMessage> webhookFuture = client.send(builder.build()).whenComplete((readonlyMessage, throwable) -> {
                if(throwable != null){
                    LOGGER.warn("Error while sending webhook message!", throwable);
                    System.exit(1);
                    return;
                }
                
                LOGGER.info("Webhook message send!");
                client.close();
            });
            
            try{
                webhookFuture.join();
            }catch(Exception ex){
                LOGGER.warn("Unable to complete webhook message sending!", ex);
                System.exit(1);
            }
        }catch(Exception ex){
            LOGGER.warn("Encountered Exception while creating WebhookClient!", ex);
            System.exit(1);
        }
    }
    
    public static String retrieveVersion(){
        try(InputStream is = VersionUploader.class.getResourceAsStream("/version.properties")){
            Properties properties = new Properties();
            
            properties.load(is);
            
            return properties.getProperty("version");
        }catch(IOException ex){
            return null;
        }
    }
}
