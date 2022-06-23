package ch.andre601.advancedserverlist.core.interfaces;

import net.kyori.adventure.text.format.NamedTextColor;

public interface CmdSender{
    
    boolean hasPermission(String permission);
    
    default void sendMsg(){
        sendMsg("");
    }
    
    void sendMsg(String msg, Object... args);
}
