package ch.andre601.advancedserverlist.core.profiles.replacer;

import java.util.Iterator;
import java.util.Map;

public class StringReplacer{
    
    private final EntryList<String, Object> entries = new EntryList<>();
    
    public void add(String from, Object to){
        if(from == null || to == null)
            return;
        
        entries.add(from, to);
    }
    
    public Object get(String from){
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        Map.Entry<String, Object> entry;
        while(iterator.hasNext()){
            entry = iterator.next();
            if(entry.getKey().equals(from)){
                iterator.remove();
                return entry.getValue();
            }
        }
        return null;
    }
    
    public String replace(String input){
        StringBuilder output = new StringBuilder(input);
        int index;
        for(Map.Entry<String, Object> entry : entries){
            index = 0;
            while((index = output.indexOf(entry.getKey(), index)) != -1){
                output.replace(index, index + entry.getKey().length(), String.valueOf(entry.getValue()));
                index += String.valueOf(entry.getValue()).length();
            }
        }
        return output.toString();
    }
}
