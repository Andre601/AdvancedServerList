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

package ch.andre601.advancedserverlist.versionuploader.modrinth;

import ch.andre601.advancedserverlist.versionuploader.PlatformInfo;
import ch.andre601.advancedserverlist.versionuploader.VersionUploader;
import masecla.modrinth4j.client.agent.UserAgent;
import masecla.modrinth4j.endpoints.version.CreateVersion;
import masecla.modrinth4j.main.ModrinthAPI;
import masecla.modrinth4j.model.version.ProjectVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ModrinthVersionUploader{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ModrinthVersionUploader.class);
    private final List<PlatformInfo> platforms = List.of(
        PlatformInfo.BUKKIT,
        PlatformInfo.BUNGEECORD,
        PlatformInfo.VELOCITY
    );
    private final List<String> versions = List.of("1.19.4", "1.20", "1.20.1");
    
    private final ModrinthAPI api;
    
    public ModrinthVersionUploader(){
        this.api = createClient();
    }
    
    public void performUploads(){
        LOGGER.info("Starting ModrinthVersionUploader...");
        
        if(api == null){
            LOGGER.warn("Unable to obtain ModrinthAPI instance!");
            System.exit(1);
            return;
        }
        
        final String version = VersionUploader.getVersion();
        if(version == null){
            LOGGER.warn("Unable to retrieve Version!");
            System.exit(1);
            return;
        }
        
        final String changelog = VersionUploader.getChangelog();
        final boolean preRelease = VersionUploader.getPreReleaseState();
        
        for(PlatformInfo platform : platforms){
            File file = new File(platform.getFilePath().replace("{{version}}", version));
            
            CreateVersion.CreateVersionRequest request = CreateVersion.CreateVersionRequest.builder()
                .projectId("xss83sOY")
                .name("v" + version + " (" + String.join(", ", platform.getLoaders()) + ")")
                .versionNumber(version)
                .changelog(changelog)
                .featured(false)
                .files(file)
                .gameVersions(versions)
                .loaders(platform.getLoaders())
                .versionType(preRelease ? ProjectVersion.VersionType.BETA : ProjectVersion.VersionType.RELEASE)
                .dependencies(Collections.singletonList(
                    new ProjectVersion.ProjectDependency(null, "papiproxabridge", null, ProjectVersion.ProjectDependencyType.OPTIONAL)
                ))
                .build();
            
            api.versions().createProjectVersion(request).whenComplete((project, throwable) -> {
                if(throwable != null){
                    LOGGER.warn("Encountered error while creating a release on Modrinth!", throwable);
                    return;
                }
                
                LOGGER.info("Successfully uploaded new Modrinth release!");
                LOGGER.info("https://modrinth.com/plugin/advancedserverlist/version/{}", project.getId());
            });
        }
        
        LOGGER.info("Upload task completed! Shutting down jar...");
        System.exit(0);
    }
    
    private ModrinthAPI createClient(){
        String apiToken = System.getenv("MODRINTH_API_TOKEN");
        if(apiToken == null)
            return null;
        
        UserAgent agent = UserAgent.builder()
            .authorUsername("Andre_601")
            .projectVersion("v1.0.0")
            .projectName("ModrinthVersionUploader")
            .contact("github@andre601.ch")
            .build();
        
        return ModrinthAPI.rateLimited(agent, apiToken);
    }
}
