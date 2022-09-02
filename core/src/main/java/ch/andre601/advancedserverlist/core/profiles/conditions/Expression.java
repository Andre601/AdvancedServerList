/*
 * MIT License
 *
 * Copyright (c) 2022 Andre_601
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

import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

import java.util.Map;

public class Expression{
    
    private ExpressionResult result = ExpressionResult.VALID;
    
    private String left;
    private Operator operator = Operator.UNKNOWN;
    private String right;
    
    public Expression(String expression){
        resolveExpression(expression);
    }
    
    public boolean evaluate(Map<String, Object> replacements){
        String newLeft = StringReplacer.replace(left, replacements).trim();
        String newRight = StringReplacer.replace(right, replacements).trim();
        
        return operator.evaluate(newLeft, newRight);
    }
    
    public ExpressionResult getResult(){
        return result;
    }
    
    private void resolveExpression(String expression){
        if(expression == null || expression.isEmpty()){
            result = ExpressionResult.INVALID_NO_EXPRESSION;
            return;
        }
        
        char[] chars = expression.toCharArray();
        
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        
        boolean foundOperator = false;
        
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            char next = (i == (chars.length - 1)) ? '\0' : chars[i + 1];
            
            Operator tmp = Operator.getOperand(c, next);
            if(tmp == Operator.UNKNOWN){
                if(foundOperator){
                    right.append(c);
                }else{
                    left.append(c);
                }
                continue;
            }
            
            if(foundOperator){
                result = ExpressionResult.INVALID_DOUBLE_OPERATOR;
                return;
            }
            
            operator = tmp;
            foundOperator = true;
        }
        
        if(left.isEmpty() || right.isEmpty()){
            result = ExpressionResult.INVALID_EMPTY_PARTS;
            return;
        }
        
        this.left = left.toString();
        this.right = right.toString();
    }
    
    public enum Operator{
        LESS_THAN('<', '\0'){
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
        GREATER_THAN('>', '\0'){
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
        
        INVALID_NO_EXPRESSION  ("Received empty condition which isn't allowed!"),
        INVALID_DOUBLE_OPERATOR("Condition contained two operands."),
        INVALID_EMPTY_PARTS    ("Received condition with either left or right part being empty.");
        
        private final String message;
        
        ExpressionResult(String message){
            this.message = message;
        }
        
        public String getMessage(){
            return message;
        }
    }
}
