package ch.andre601.advancedserverlist.core.parsing;

import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.Map;

public class ComponentParser{
    
    private static final MiniMessage mm;
    private static final LegacyComponentSerializer legacy;
    private static final StringReplacer replacer;
    
    static {
        mm = MiniMessage.miniMessage();
        legacy = LegacyComponentSerializer.legacySection();
        replacer = new StringReplacer();
    }
    
    private final String text;
    
    private ComponentParser(String text){
        this.text = text;
    }
    
    public static ComponentParser text(String text){
        return new ComponentParser(text);
    }
    
    public static ComponentParser list(List<String> lines){
        return new ComponentParser(String.join("\n", lines));
    }
    
    public ComponentParser replace(String from, Object to){
        replacer.add(from, to);
        return this;
    }
    
    public ComponentParser replacements(Map<String, Object> replacements){
        replacements.forEach(replacer::add);
        return this;
    }
    
    public Component toComponent(){
        return mm.deserialize(replacer.replace(text));
    }
    
    @Override
    public String toString(){
        return legacy.serialize(toComponent());
    }
}
