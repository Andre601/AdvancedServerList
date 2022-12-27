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

package ch.andre601.advancedserverlist.core.profiles.profile;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ProfileSerializer implements TypeSerializer<ProfileEntry>{
    public static final ProfileSerializer INSTANCE = new ProfileSerializer();
    
    @Override
    public ProfileEntry deserialize(Type type, ConfigurationNode node){
        return ProfileEntry.Builder.resolve(node).build();
    }
    
    @Override
    public void serialize(Type type, @Nullable ProfileEntry profile, ConfigurationNode node) throws SerializationException{
        if(profile == null){
            node.raw(null);
            return;
        }
        
        node.node("motd").set(profile.getMOTD());
        node.node("playerCount", "hover").set(profile.getPlayers());
        node.node("playerCount", "text").set(profile.getPlayerCountText());
        node.node("favicon").set(profile.getFavicon());
        node.node("playerCount", "hidePlayers").set(profile.isHidePlayersEnabled());
        node.node("playerCount", "extraPlayers", "enabled").set(profile.isExtraPlayersEnabled());
        node.node("playerCount", "extraPlayers", "amount").set(profile.getExtraPlayersCount());
    }
}
