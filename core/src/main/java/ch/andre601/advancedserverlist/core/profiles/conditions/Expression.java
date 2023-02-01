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

package ch.andre601.advancedserverlist.core.profiles.conditions;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

public class Expression{
    
    private final ExpressionResult result;
    
    private final String left;
    private final Operator operator;
    private final String right;
    
    private Expression(ExpressionResult result, String left, Operator operator, String right){
        this.result = result;
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    public static Expression createInvalid(ExpressionResult result){
        return new Expression(result, null, Operator.UNKNOWN, null);
    }
    
    public static Expression resolve(String expression){
        Operator operator = Operator.UNKNOWN;
        
        if(expression == null || expression.isEmpty()){
            return Expression.createInvalid(ExpressionResult.INVALID_NO_EXPRESSION);
        }
        
        char[] chars = expression.toCharArray();
        
        StringBuilder leftBuilder = new StringBuilder();
        StringBuilder rightBuilder = new StringBuilder();
        
        boolean foundOperator = false;
        
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            char next = (i == (chars.length - 1)) ? '\0' : chars[i + 1];
            
            Operator tmp = Operator.getOperand(c, next);
            if(tmp != Operator.UNKNOWN && !foundOperator){
                operator = tmp;
                foundOperator = true;
                
                if(tmp.hasSecond())
                    i++; // Skip next character if operator has 2
                
                continue;
            }
            
            if(foundOperator){
                rightBuilder.append(c);
            }else{
                leftBuilder.append(c);
            }
        }
        
        if(leftBuilder.isEmpty() || rightBuilder.isEmpty()){
            return Expression.createInvalid(ExpressionResult.INVALID_EMPTY_PARTS);
        }
        
        return new Expression(ExpressionResult.VALID, leftBuilder.toString().trim(), operator, rightBuilder.toString().trim());
    }
    
    public boolean evaluate(GenericPlayer player, GenericServer server){
        String newLeft = StringReplacer.replace(left, player, server);
        String newRight = StringReplacer.replace(right, player, server);
        
        return operator.evaluate(newLeft, newRight);
    }
    
    public ExpressionResult getResult(){
        return result;
    }
    
    public enum Operator{
        LESS('<', '\0'){
            @Override
            public boolean evaluate(String left, String right){
                int leftInt = Operator.getInt(left);
                int rightInt = Operator.getInt(right);
                
                return leftInt < rightInt;
            }
        },
        LESS_OR_EQUAL('<', '='){
            @Override
            public boolean evaluate(String left, String right){
                int leftInt = Operator.getInt(left);
                int rightInt = Operator.getInt(right);
                
                return leftInt <= rightInt;
            }
        },
        EQUAL('=', '\0'){
            @Override
            public boolean evaluate(String left, String right){
                return left.equals(right);
            }
        },
        NOT_EQUAL('!', '='){
            @Override
            public boolean evaluate(String left, String right){
                return !left.equals(right);
            }
        },
        GREATER('>', '\0'){
            @Override
            public boolean evaluate(String left, String right){
                int leftInt = Operator.getInt(left);
                int rightInt = Operator.getInt(right);
                
                return leftInt > rightInt;
            }
        },
        GREATER_OR_EQUAL('>', '='){
            @Override
            public boolean evaluate(String left, String right){
                int leftInt = Operator.getInt(left);
                int rightInt = Operator.getInt(right);
                
                return leftInt >= rightInt;
            }
        },
        
        UNKNOWN('\0', '\0'){
            @Override
            public boolean evaluate(String left, String right){
                return false;
            }
        };
        
        private static final Operator[] VALUES = values();
        
        private final char first;
        private final char second;
        
        Operator(char first, char second){
            this.first = first;
            this.second = second;
        }
        
        public static Operator getOperand(char first, char second){
            for(Operator operator : VALUES){
                if(operator.first == first){
                    if((!operator.hasSecond() && second != '=') || operator.second == second)
                        return operator;
                }
            }
            
            return Operator.UNKNOWN;
        }
        
        public boolean hasSecond(){
            return second != '\0';
        }
        
        public abstract boolean evaluate(String left, String right);
    
        private static int getInt(String text){
            try{
                return Integer.parseInt(text);
            }catch(NumberFormatException ex){
                return text.length();
            }
        }
    }
    
    public enum ExpressionResult{
        VALID(null),
        
        INVALID_NO_EXPRESSION("Received empty condition which isn't allowed!"),
        INVALID_EMPTY_PARTS  ("Received condition with either left or right part being empty.");
        
        private final String message;
        
        ExpressionResult(String message){
            this.message = message;
        }
        
        public String getMessage(){
            return message;
        }
    }
}
