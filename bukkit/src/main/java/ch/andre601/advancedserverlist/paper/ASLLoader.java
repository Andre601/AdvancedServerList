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

package ch.andre601.advancedserverlist.paper;

import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class ASLLoader implements PluginLoader{
    @Override
    public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder){
        ComponentLogger logger = pluginClasspathBuilder.getContext().getLogger();
        
        logger.info(Component.text("Loading Libraries for plugin..."));
        
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        
        resolver.addRepository(new RemoteRepository.Builder(
            "paper-maven",
            "default",
            "https://repo.papermc.io/repository/maven-public/"
        ).build());
        
        logger.info(Component.text("Loading com.squareup.okhttp3:okhttp:4.10.0 from repo.papermc.io..."));
        
        resolver.addDependency(new Dependency(new DefaultArtifact("com.squareup.okhttp3:okhttp:4.10.0"), null));
        pluginClasspathBuilder.addLibrary(resolver);
        
        logger.info(Component.text("Loading libraries complete!"));
    }
}
