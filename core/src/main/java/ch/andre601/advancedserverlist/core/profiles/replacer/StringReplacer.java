/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
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
import ch.andre601.advancedserverlist.core.AdvancedServerList;
import ch.andre601.advancedserverlist.core.profiles.conditions.placeholders.tokens.PlaceholderToken;
import ch.andre601.expressionparser.ParseWarnCollector;

import java.text.ParsePosition;

public class StringReplacer{
    
    public static String replace(String input, GenericPlayer player, GenericServer server){
        if(input == null)
            return null;
        
        ParsePosition position = new ParsePosition(0);
        StringBuilder builder = new StringBuilder(input.length());
        
        while(position.getIndex() < input.length()){
            if(position.getIndex() + 1 < input.length() && input.charAt(position.getIndex()) == '$' && input.charAt(position.getIndex() + 1) == '{'){
                position.setIndex(position.getIndex() + 2);
                builder.append(parsePlaceholder0(input, position, player, server, null));
                continue;
            }
            
            builder.append(input.charAt(position.getIndex()));
            position.setIndex(position.getIndex() + 1);
        }
        
        return builder.toString();
    }
    
    public static PlaceholderToken.Placeholder parsePlaceholder(String input, ParsePosition position, GenericPlayer player, GenericServer server, ParseWarnCollector collector){
        return new PlaceholderToken.Placeholder(parsePlaceholder0(input, position, player, server, collector));
    }
    
    private static String parsePlaceholder0(String input, ParsePosition position, GenericPlayer player, GenericServer server, ParseWarnCollector collector){
        AdvancedServerListAPI api = AdvancedServerList.getApi();
        
        int placeholderStart = position.getIndex() - 2;
        int index = position.getIndex();
        
        StringBuilder identifier = new StringBuilder();
        StringBuilder values = new StringBuilder();
        
        boolean identified = false;
        boolean invalid = true;
        
        char[] chars = input.substring(index).toCharArray();
        for(final char c : chars){
            index++;
            
            if(c == '}'){
                invalid = false;
                break;
            }
            
            if(c == ' ' && !identified){
                identified = true;
                continue;
            }
            
            if(identified){
                values.append(c);
            }else{
                identifier.append(c);
            }
        }
        
        String identifierStr = identifier.toString();
        String valuesStr = values.toString();
        
        StringBuilder raw = new StringBuilder();
        
        // Condition parsing needs one extra increase of index... for some reason
        if(collector != null)
            index++;
        
        position.setIndex(index);
        
        if(invalid){
            raw.append("${").append(identifierStr);
            
            if(identified)
                raw.append(' ').append(valuesStr);
            
            if(collector != null)
                collector.appendWarningFormatted(placeholderStart, "Placeholder '%s' does not have a closing bracket (})", raw.toString());
            
            return raw.toString();
        }
        
        PlaceholderProvider provider = api.retrievePlaceholderProvider(identifierStr);
        if(provider == null){
            raw.append("${").append(identifierStr);
            
            if(identified)
                raw.append(' ').append(valuesStr);
            
            raw.append('}');
            
            if(collector != null)
                collector.appendWarningFormatted(placeholderStart, "Placeholder '%s' does not have any available PlaceholderProvider", raw.toString());
            
            return raw.toString();
        }
        
        String replacement = provider.parsePlaceholder(valuesStr, player, server);
        if(replacement == null){
            raw.append("${").append(identifierStr);
            
            if(identified)
                raw.append(' ').append(valuesStr);
            
            raw.append('}');
            
            if(collector != null)
                collector.appendWarningFormatted(placeholderStart, "Placeholder '%s' has an invalid value String '%s'", raw.toString(), valuesStr);
            
            return raw.toString();
        }
        
        return replacement;
    }
}
