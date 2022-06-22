package ch.andre601.advancedserverlist.core.interfaces;

import net.kyori.adventure.text.format.NamedTextColor;

public interface CmdSender{
    
    boolean hasPermission(String permission);
    
    void sendMsg();
    
    void sendMsg(String msg, Object... args);
}
