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

package ch.andre601.advancedserverlist.core.profiles.conditions.expressions;

import ch.andre601.advancedserverlist.core.profiles.conditions.templates.*;

import java.util.Collection;
import java.util.function.Function;

public class Expressions{
    
    public static ToBooleanExpression negate(ToBooleanExpression expression){
        return new AbstractUnaryToBooleanExpression<>(expression) {
            @Override
            public boolean evaluate(){
                return !delegate.evaluate();
            }
        };
    }
    
    public static ToBooleanExpression and(Collection<ToBooleanExpression> operands){
        return new AbstractToBooleanExpression<>(operands) {
            @Override
            public boolean evaluate(){
                for(ToBooleanExpression operand : operands){
                    if(!operand.evaluate())
                        return false;
                }
                
                return true;
            }
        };
    }
    
    public static ToBooleanExpression or(Collection<ToBooleanExpression> operands){
        return new AbstractToBooleanExpression<>(operands) {
            @Override
            public boolean evaluate(){
                for(ToBooleanExpression operand : operands){
                    if(operand.evaluate())
                        return true;
                }
                
                return false;
            }
        };
    }
    
    public static ToStringExpression concat(Collection<ToStringExpression> operands){
        return new AbstractToStringExpression<>(operands) {
            @Override
            public String evaluate(){
                StringBuilder result = new StringBuilder();
                for(ToStringExpression operand : operands){
                    result.append(operand.evaluate());
                }
                
                return result.toString();
            }
        };
    }
    
    public static ToBooleanExpression equal(ToStringExpression a, ToStringExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return a.evaluate().equals(b.evaluate());
            }
        };
    }
    
    public static ToBooleanExpression notEqual(ToStringExpression a, ToStringExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return !a.evaluate().equals(b.evaluate());
            }
        };
    }
    
    public static ToBooleanExpression equalIgnoreCase(ToStringExpression a, ToStringExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return a.evaluate().equalsIgnoreCase(b.evaluate());
            }
        };
    }
    
    public static ToBooleanExpression notEqualIgnoreCase(ToStringExpression a, ToStringExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return !a.evaluate().equalsIgnoreCase(b.evaluate());
            }
        };
    }
    
    public static ToBooleanExpression greaterThan(ToDoubleExpression a, ToDoubleExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return a.evaluate() > b.evaluate();
            }
        };
    }
    
    public static ToBooleanExpression greaterOrEqualThan(ToDoubleExpression a, ToDoubleExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return a.evaluate() >= b.evaluate();
            }
        };
    }
    
    public static ToBooleanExpression lessThan(ToDoubleExpression a, ToDoubleExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return a.evaluate() < b.evaluate();
            }
        };
    }
    
    public static ToBooleanExpression lessOrEqualThan(ToDoubleExpression a, ToDoubleExpression b){
        return new AbstractBinaryToBooleanExpression<>(a, b) {
            @Override
            public boolean evaluate(){
                return a.evaluate() <= b.evaluate();
            }
        };
    }
    
    public static ToDoubleExpression sum(Collection<ToDoubleExpression> operands){
        return new AbstractToDoubleExpression<>(operands) {
            @Override
            public double evaluate(){
                double result = 0;
                for(ToDoubleExpression operand : operands){
                    result += operand.evaluate();
                }
                
                return result;
            }
        };
    }
    
    public static ToDoubleExpression product(Collection<ToDoubleExpression> operands){
        return new AbstractToDoubleExpression<>(operands) {
            @Override
            public double evaluate(){
                double result = 1;
                for(ToDoubleExpression operand : operands){
                    result *= operand.evaluate();
                }
                
                return result;
            }
        };
    }
    
    public static ToDoubleExpression sub(ToDoubleExpression a, ToDoubleExpression b){
        return new AbstractBinaryToDoubleExpression<>(a, b) {
            @Override
            public double evaluate(){
                return a.evaluate() - b.evaluate();
            }
        };
    }
    
    public static ToDoubleExpression div(ToDoubleExpression a, ToDoubleExpression b){
        return new AbstractBinaryToDoubleExpression<>(a, b) {
            @Override
            public double evaluate(){
                return a.evaluate() / b.evaluate();
            }
        };
    }
    
    public static ToDoubleExpression negateNumber(ToDoubleExpression a){
        return new AbstractUnaryToDoubleExpression<>(a) {
            @Override
            public double evaluate(){
                return -delegate.evaluate();
            }
        };
    }
    
    public static ToStringExpression applyToStringFunction(ToStringExpression expression, Function<String, String> function){
        return new AbstractUnaryToStringExpression<>(expression) {
            @Override
            public String evaluate(){
                return function.apply(delegate.evaluate());
            }
        };
    }
}
