package ch.andre601.advancedserverlist.core.parsing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class ComponentParser{
    
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
    
    public static Component toComponent(String msg){
        return mm.deserialize(msg);
    }
    
    public static Component toComponent(List<String> lines){
        return toComponent(String.join("\n", lines));
    }
    
    public static String toString(String msg){
        return legacy.serialize(toComponent(msg));
    }
}
