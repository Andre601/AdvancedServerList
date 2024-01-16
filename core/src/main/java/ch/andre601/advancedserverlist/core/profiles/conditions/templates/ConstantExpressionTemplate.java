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

package ch.andre601.advancedserverlist.core.profiles.conditions.templates;

import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToBooleanExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToDoubleExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToStringExpression;

public class ConstantExpressionTemplate implements ExpressionTemplate{
    
    private final ToStringExpression stringExpression;
    private final ToDoubleExpression doubleExpression;
    private final ToBooleanExpression booleanExpression;
    
    private ConstantExpressionTemplate(String stringValue, double doubleValue, boolean booleanValue){
        this.stringExpression = ToStringExpression.literal(stringValue);
        this.doubleExpression = ToDoubleExpression.literal(doubleValue);
        this.booleanExpression = ToBooleanExpression.literal(booleanValue);
    }
    
    public static ConstantExpressionTemplate of(String stringValue){
        double doubleValue;
        try{
            doubleValue = Double.parseDouble(stringValue);
        }catch(NumberFormatException ex){
            doubleValue = stringValue.length();
        }
        return new ConstantExpressionTemplate(stringValue, doubleValue, Boolean.parseBoolean(stringValue));
    }
    
    public static ConstantExpressionTemplate of(double doubleValue){
        return new ConstantExpressionTemplate(((int)doubleValue) == doubleValue ? Integer.toString((int)doubleValue) : Double.toString(doubleValue), doubleValue, doubleValue != 0);
    }
    
    public static ConstantExpressionTemplate of(boolean booleanValue){
        return new ConstantExpressionTemplate(Boolean.toString(booleanValue), booleanValue ? 1 : 0, booleanValue);
    }
    
    
    @Override
    public ToStringExpression instantiateWithStringResult(){
        return stringExpression;
    }
    
    @Override
    public ToDoubleExpression instantiateWithDoubleResult(){
        return doubleExpression;
    }
    
    @Override
    public ToBooleanExpression instantiateWithBooleanResult(){
        return booleanExpression;
    }
}
