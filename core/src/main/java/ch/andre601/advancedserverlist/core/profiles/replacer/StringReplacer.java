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

package ch.andre601.advancedserverlist.core.profiles.replacer;

import java.util.Iterator;
import java.util.Map;

/*
 * Original by Imo van den Berge (aka Bergerkiller: https://github.com/Bergerkiller)
 *
 * Original Source: https://github.com/bergerhealer/BKCommonLib/blob/master/src/main/java/com/bergerkiller/bukkit/common/StringReplaceBundle.java
 */
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
