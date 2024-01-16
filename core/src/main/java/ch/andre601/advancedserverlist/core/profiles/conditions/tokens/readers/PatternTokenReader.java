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

package ch.andre601.advancedserverlist.core.profiles.conditions.tokens.readers;

import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.Token;

import java.text.ParsePosition;

public class PatternTokenReader extends TokenReader{
    
    private final Token token;
    private final String pattern;
    private final boolean ignoreCase;
    
    public PatternTokenReader(Token token, String pattern){
        this(token, pattern, true);
    }
    
    public PatternTokenReader(Token token, String pattern, boolean ignoreCase){
        super(pattern.length());
        
        this.token = token;
        this.pattern = pattern;
        this.ignoreCase = ignoreCase;
    }
    
    @Override
    public Token read(String text, ParsePosition position){
        if(text.regionMatches(ignoreCase, position.getIndex(), pattern, 0, pattern.length())){
            position.setIndex(position.getIndex() + pattern.length());
            return token;
        }
        
        return null;
    }
}
