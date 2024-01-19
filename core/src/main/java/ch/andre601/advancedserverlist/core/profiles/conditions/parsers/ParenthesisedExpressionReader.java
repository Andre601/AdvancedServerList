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

package ch.andre601.advancedserverlist.core.profiles.conditions.parsers;

import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplate;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class ParenthesisedExpressionReader extends ValueReader{
    
    private final Token openingParenthesis;
    private final Token closingParenthesis;
    
    public ParenthesisedExpressionReader(Token openingParenthesis, Token closingParenthesis){
        this.openingParenthesis = openingParenthesis;
        this.closingParenthesis = closingParenthesis;
    }
    
    @Override
    public ExpressionTemplate read(ExpressionTemplateParser parser, List<Token> tokenList){
        if(tokenList.get(0) == openingParenthesis){
            int index = 0;
            int cnt = 1;
            do {
                index += 1;
                if(tokenList.size() <= index)
                    return null;
                
                Token token = tokenList.get(index);
                if(token == openingParenthesis){
                    cnt++;
                }else
                if(token == closingParenthesis){
                    cnt--;
                }
            }while(cnt != 0);
            
            ExpressionTemplate result = parser.parse(new ArrayList<>(tokenList.subList(1, index)));
            tokenList.subList(0, index + 1).clear();
            
            return result;
        }
        
        return null;
    }
}
