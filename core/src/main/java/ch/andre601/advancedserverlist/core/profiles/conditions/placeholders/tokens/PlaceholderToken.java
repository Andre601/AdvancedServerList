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

package ch.andre601.advancedserverlist.core.profiles.conditions.placeholders.tokens;

import ch.andre601.expressionparser.expressions.ToBooleanExpression;
import ch.andre601.expressionparser.expressions.ToDoubleExpression;
import ch.andre601.expressionparser.expressions.ToStringExpression;
import ch.andre601.expressionparser.templates.ExpressionTemplate;
import ch.andre601.expressionparser.tokens.Token;

public class PlaceholderToken extends Token{
    
    private final Placeholder value;
    
    public PlaceholderToken(Placeholder value){
        super("PLACEHOLDER");
        this.value = value;
    }
    
    public Placeholder getValue(){
        return value;
    }
    
    public static class Placeholder implements ExpressionTemplate{
        
        private final ToBooleanExpression toBooleanExpression;
        private final ToDoubleExpression toDoubleExpression;
        private final ToStringExpression toStringExpression;
        
        public Placeholder(String value){
            double doubleValue;
            try{
                doubleValue = Double.parseDouble(value);
            }catch(NumberFormatException ex){
                doubleValue = value.length();
            }
            
            this.toBooleanExpression = ToBooleanExpression.literal(Boolean.parseBoolean(value));
            this.toDoubleExpression = ToDoubleExpression.literal(doubleValue);
            this.toStringExpression = ToStringExpression.literal(value);
        }
        
        @Override
        public ToBooleanExpression returnBooleanExpression(){
            return toBooleanExpression;
        }
        
        @Override
        public ToDoubleExpression returnDoubleExpression(){
            return toDoubleExpression;
        }
        
        @Override
        public ToStringExpression returnStringExpression(){
            return toStringExpression;
        }
    }
}
