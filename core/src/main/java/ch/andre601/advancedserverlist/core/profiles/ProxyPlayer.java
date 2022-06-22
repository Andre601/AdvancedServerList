package ch.andre601.advancedserverlist.core.profiles;

public class ProxyPlayer{
    
    private final String name;
    private final int clientVersion;
    
    public ProxyPlayer(String name, int clientVersion){
        this.name = name;
        this.clientVersion = clientVersion;
    }
    
    public String getName(){
        return name;
    }
    
    public int getClientVersion(){
        return clientVersion;
    }
}
