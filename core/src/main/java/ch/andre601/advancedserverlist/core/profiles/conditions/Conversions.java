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

import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToBooleanExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToDoubleExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToStringExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.AbstractUnaryToBooleanExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.AbstractUnaryToDoubleExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.AbstractUnaryToStringExpression;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Conversions{
    
    private final static NumberFormat NUMBER_FORMAT;
    
    static {
        NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.ROOT);
        NUMBER_FORMAT.setGroupingUsed(false);
    }
    
    public static ToBooleanExpression toBoolean(ToDoubleExpression expression){
        return new AbstractUnaryToBooleanExpression<>(expression) {
            @Override
            public boolean evaluate(){
                return delegate.evaluate() != 0;
            }
        };
    }
    
    public static ToBooleanExpression toBoolean(ToStringExpression expression){
        return new AbstractUnaryToBooleanExpression<>(expression) {
            @Override
            public boolean evaluate(){
                return Boolean.parseBoolean(delegate.evaluate());
            }
        };
    }
    
    public static ToDoubleExpression toDouble(ToBooleanExpression expression){
        return new AbstractUnaryToDoubleExpression<>(expression) {
            @Override
            public double evaluate(){
                return delegate.evaluate() ? 1 : 0;
            }
        };
    }
    
    public static ToDoubleExpression toDouble(ToStringExpression expression){
        return new AbstractUnaryToDoubleExpression<>(expression) {
            @Override
            public double evaluate(){
                String result = delegate.evaluate();
                try{
                    return NUMBER_FORMAT.parse(result).doubleValue();
                }catch(ParseException | NumberFormatException ex){
                    return 0;
                }
            }
        };
    }
    
    public static ToStringExpression toString(ToBooleanExpression expression){
        return new AbstractUnaryToStringExpression<>(expression) {
            @Override
            public String evaluate(){
                return Boolean.toString(delegate.evaluate());
            }
        };
    }
    
    public static ToStringExpression toString(ToDoubleExpression expression){
        return new AbstractUnaryToStringExpression<>(expression) {
            @Override
            public String evaluate(){
                double result = delegate.evaluate();
                return result == (int)result ? Integer.toString((int)result) : Double.toString(result);
            }
        };
    }
}
