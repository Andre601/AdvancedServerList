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

package ch.andre601.advancedserverlist.core.file;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

public class ConfigMigrator{
    
    public static final int LATEST = 2;
    
    private ConfigMigrator(){}
    
    public static ConfigurationTransformation.Versioned create(){
        return ConfigurationTransformation.versionedBuilder()
            .versionKey("config-version")
            .addVersion(LATEST, oneToTwo())
            .build();
    }
    
    public static ConfigurationTransformation oneToTwo(){
        return ConfigurationTransformation.builder()
            .addAction(NodePath.path("unknown_player"), (path, value) -> new Object[]{"unknownPlayer", "name"})
            .addAction(NodePath.path("unknown_player_uuid"), (path, value) -> new Object[]{"unknownPlayer", "uuid"})
            .addAction(NodePath.path("disable_cache"), TransformAction.rename("disableCache"))
            .addAction(NodePath.path("check_updates"), TransformAction.rename("checkUpdates"))
            .addAction(NodePath.path(), (path, value) -> {
                value.node("config-version").set(LATEST);
                
                return null;
            })
            .build();
    }
    
    public static <N extends ConfigurationNode> N updateNode(final N node, PluginLogger logger) throws ConfigurateException{
        if(!node.virtual()){
            final ConfigurationTransformation.Versioned versioned = create();
            final int startVersion = versioned.version(node);
            versioned.apply(node);
            final int endVersion = versioned.version(node);
            if(startVersion != endVersion)
                logger.info("Migrated config.yml from " + startVersion + " to " + endVersion);
        }
        
        return node;
    }
}
