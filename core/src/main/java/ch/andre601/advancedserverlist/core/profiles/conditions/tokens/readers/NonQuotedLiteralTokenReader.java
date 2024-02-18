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

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ExpressionsWarnHelper;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.StringToken;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.Token;

import java.text.ParsePosition;

public class NonQuotedLiteralTokenReader extends TokenReader{
    
    public NonQuotedLiteralTokenReader(int priority){
        super(priority);
    }
    
    @Override
    public Token read(String text, ParsePosition position, GenericPlayer player, GenericServer server, ExpressionsWarnHelper warnHelper){
        int startIndex = position.getIndex();
        int index = position.getIndex();
        
        while(++index < text.length() && !Character.isWhitespace(text.charAt(index)))
            ;
        
        position.setIndex(index);
        
        return new StringToken(text.substring(startIndex, position.getIndex()));
    }
}
