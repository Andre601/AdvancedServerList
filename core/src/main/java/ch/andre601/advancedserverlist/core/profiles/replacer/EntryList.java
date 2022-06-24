package ch.andre601.advancedserverlist.core.profiles.replacer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class EntryList<K, V> extends ArrayList<Map.Entry<K, V>>{
    public void add(K key, V value){
        add(new AbstractMap.SimpleEntry<>(key, value));
    }
}
