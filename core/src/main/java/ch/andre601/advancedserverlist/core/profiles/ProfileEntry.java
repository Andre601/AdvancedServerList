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

package ch.andre601.advancedserverlist.core.profiles;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileEntry{
    
    private final List<String> motd;
    private final List<String> players;
    private final String playerCountText;
    private final String favicon;
    private final boolean hidePlayersEnabled;
    private final boolean extraPlayersEnabled;
    private final int extraPlayersCount;
    
    public ProfileEntry(List<String> motd, List<String> players,
                               String playerCountText, String favicon, boolean hidePlayersEnabled,
                               boolean extraPlayersEnabled, int extraPlayersCount){
        this.motd = motd;
        this.players = players;
        this.playerCountText = playerCountText;
        this.favicon = favicon;
        this.hidePlayersEnabled = hidePlayersEnabled;
        this.extraPlayersEnabled = extraPlayersEnabled;
        this.extraPlayersCount = extraPlayersCount;
    }
    
    public List<String> getMOTD(){
        return motd;
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public String getPlayerCountText(){
        return playerCountText;
    }
    
    public String getFavicon(){
        return favicon;
    }
    
    public boolean isHidePlayersEnabled(){
        return hidePlayersEnabled;
    }
    
    public boolean isExtraPlayersEnabled(){
        return extraPlayersEnabled;
    }
    
    public int getExtraPlayersCount(){
        return extraPlayersCount;
    }
    
    public boolean isInvalidProfile(){
        return getMOTD().isEmpty() &&
            getPlayers().isEmpty() &&
            (getPlayerCountText().isEmpty() && !isHidePlayersEnabled()) &&
            getFavicon().isEmpty();
    }
    
    public static class Builder{
    
        private final ConfigurationNode node;
        
        private List<String> motd = new ArrayList<>();
        private List<String> players = new ArrayList<>();
        private String playerCountText = null;
        private String favicon = null;
        private boolean hidePlayersEnabled = false;
        private boolean extraPlayersEnabled = false;
        private int extraPlayersCount = 0;
    
        private Builder(ConfigurationNode node){
            this.node = node;
        }
    
        public static Builder resolve(ConfigurationNode node){
            return new Builder(node)
                .resolveMOTD()
                .resolvePlayers()
                .resolvePlayerCountText()
                .resolveFavicon()
                .resolveHidePlayersEnabled()
                .resolveExtraPlayersEnabled()
                .resolveExtraPlayersCount();
        }
        
        public Builder resolveMOTD(){
            List<String> motd = resolveList(node, "motd");
            if(motd.size() <= 2){
                this.motd = motd;
                return this;
            }
        
            this.motd = motd.subList(0, 2);
            return this;
        }
        
        public Builder resolvePlayers(){
            this.players = this.resolveList(node, "playerCount", "hover");
            return this;
        }
        
        public Builder resolvePlayerCountText(){
            this.playerCountText = node.node("playerCount", "text").getString("");
            return this;
        }
        
        public Builder resolveFavicon(){
            this.favicon = node.node("favicon").getString("");
            return this;
        }
        
        public Builder resolveHidePlayersEnabled(){
            this.hidePlayersEnabled = node.node("playerCount", "hidePlayers").getBoolean();
            return this;
        }
        
        public Builder resolveExtraPlayersEnabled(){
            this.extraPlayersEnabled = node.node("playerCount", "extraPlayers", "enabled").getBoolean();
            return this;
        }
        
        public Builder resolveExtraPlayersCount(){
            this.extraPlayersCount = node.node("playerCount", "extraPlayers", "amount").getInt();
            return this;
        }
        
        public ProfileEntry build(){
            return new ProfileEntry(this.motd, this.players, this.playerCountText,
                this.favicon, this.hidePlayersEnabled, this.extraPlayersEnabled, this.extraPlayersCount);
        }
    
        private List<String> resolveList(ConfigurationNode node, Object... path){
            try{
                return node.node(path).getList(String.class);
            }catch(SerializationException ex){
                return Collections.emptyList();
            }
        }
    }
}
