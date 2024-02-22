/*
 * MIT License
 *
 * Copyright (c) 2022-2024 Andre_601
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

package ch.andre601.advancedserverlist.core.profiles.conditions;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ExpressionsWarnHelper;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.Token;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.readers.TokenReader;
import com.google.common.collect.Ordering;

import java.text.ParsePosition;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ExpressionTokenizer{
    
    private static final Ordering<TokenReader> TOKEN_READER_ORDERING = Ordering.from(Comparator.comparingInt(TokenReader::getPriority)).reverse();
    
    private final List<TokenReader> tokenReaders;
    
    public ExpressionTokenizer(Iterable<TokenReader> tokenReaders){
        this.tokenReaders = TOKEN_READER_ORDERING.immutableSortedCopy(tokenReaders);
    }
    
    public List<Token> parse(String text, GenericPlayer player, GenericServer server, ExpressionsWarnHelper warnHelper){
        ParsePosition position = new ParsePosition(0);
        
        List<Token> tokens = new LinkedList<>();
        
        next_token:
        while(true){
            while(position.getIndex() < text.length() && Character.isWhitespace(text.charAt(position.getIndex()))){
                position.setIndex(position.getIndex() + 1);
            }
            
            if(position.getIndex() >= text.length())
                break;
            
            for(TokenReader tokenReader : tokenReaders){
                Token token;
                if(null != (token = tokenReader.read(text, position, player, server, warnHelper))){
                    tokens.add(token);
                    continue next_token;
                }
            }
            
            warnHelper.appendWarning(position.getIndex(), "Illegal token '%c'.", text.charAt(position.getIndex()));
            break;
        }
        
        return tokens;
    }
}
