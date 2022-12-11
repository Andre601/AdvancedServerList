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

import ch.andre601.advancedserverlist.api.AdvancedServerListAPI;
import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;

import java.util.Locale;
import java.util.Map;

public class StringReplacer{
    
    public static String replace(String input, Map<String, Object> replacements){
        StringBuilder output = new StringBuilder(input);
        int index;
        for(Map.Entry<String, Object> entry : replacements.entrySet()){
            index = 0;
            while((index = output.indexOf(entry.getKey(), index)) != -1){
                output.replace(index, index + entry.getKey().length(), String.valueOf(entry.getValue()));
                index += String.valueOf(entry.getValue()).length();
            }
        }
        return output.toString();
    }
    
    public static <P extends GenericPlayer<?>> String replace(String input, P player, GenericServer server){
        char[] chars = input.toCharArray();
        StringBuilder builder = new StringBuilder(input.length());
        
        StringBuilder identifier = new StringBuilder();
        StringBuilder placeholder = new StringBuilder();
        
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            char next = (i == (chars.length - 1)) ? '\0' : chars[i + 1];
            
            if(c != '$' || i + 2 >= chars.length){
                builder.append(c);
                continue;
            }
            
            if(next != '{'){
                if(next == '\0')
                    continue;
                
                builder.append(next);
                i++;
                continue;
            }
            
            boolean identified = false;
            boolean invalid = true;
            
            while(++i < chars.length){
                final char id = chars[i];
                
                if(id == '}'){
                    invalid = false;
                    break;
                }
                
                if(id == ' ' && !identified){
                    identified = true;
                    continue;
                }
                
                if(identified){
                    placeholder.append(id);
                }else{
                    identifier.append(id);
                }
            }
            
            String identifierString = identifier.toString();
            String placeholderString = placeholder.toString();
            
            identifier.setLength(0);
            placeholder.setLength(0);
            
            if(invalid){
                builder.append("${").append(identifierString);
                
                if(identified){
                    builder.append(' ').append(placeholderString);
                }
                continue;
            }
    
            PlaceholderProvider provider = api.retrievePlaceholderProvider(identifierString.toLowerCase(Locale.ROOT));
            if(provider == null){
                builder.append("${").append(identifierString);
    
                if(identified){
                    builder.append(' ').append(placeholderString);
                }
                
                builder.append('}');
                continue;
            }
            
            String replacement = provider.parsePlaceholder(placeholderString, player, server);
            if(replacement == null){
                builder.append("${").append(identifierString);
    
                if(identified){
                    builder.append(' ').append(placeholderString);
                }
    
                builder.append('}');
                continue;
            }
            
            builder.append(replacement);
        }
        
        return builder.toString();
    }
}
