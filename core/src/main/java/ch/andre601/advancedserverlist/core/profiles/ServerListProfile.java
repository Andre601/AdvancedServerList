package ch.andre601.advancedserverlist.core.profiles;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public class ServerListProfile{
    
    private final ConditionsHolder conditions;
    private final int priority;
    
    private final List<String> motd;
    private final List<String> players;
    private final String playerCount;
    
    public ServerListProfile(ConfigurationNode node){
        this.conditions = new ConditionsHolder(getList(node, "conditions", false));
        this.priority = node.node("priority").getInt();
        
        this.motd = getList(node, "motd", true);
        this.players = getList(node, "players", false);
        this.playerCount = node.node("playerCount").getString("");
    }
    
    public ConditionsHolder getConditions(){
        return conditions;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public List<String> getMotd(){
        return motd;
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public String getPlayerCount(){
        return playerCount;
    }
    
    private List<String> getList(ConfigurationNode node, String key, boolean trim){
        List<String> list;
        try{
            list = node.node(key).getList(String.class);
        }catch(SerializationException ex){
            return null;
        }
        
        if(list == null)
            return null;
        
        if(trim && list.size() > 2)
            return list.subList(0, 2);
        
        return list;
    }
}
