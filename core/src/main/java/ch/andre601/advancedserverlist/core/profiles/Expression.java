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

package ch.andre601.advancedserverlist.core.profiles;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

import java.util.Map;

public class Expression{
    
    private ExpressionResult result = ExpressionResult.VALID;
    
    private String left;
    private Operator operator = Operator.UNKNOWN;
    private String right;
    
    public Expression(String expression, PluginLogger logger){
        resolveExpression(expression, logger);
    }
    
    public boolean evaluate(Map<String, Object> replacements){
        String newLeft = StringReplacer.replace(left, replacements);
        String newRight = StringReplacer.replace(right, replacements);
        
        int leftInt = getInt(newLeft);
        int rightInt = getInt(newRight);
        
        return switch(operator){
            case LESS_THAN -> leftInt < rightInt;
            case LESS_OR_EQUAL -> leftInt <= rightInt;
            case EQUAL -> newLeft.equals(newRight);
            case NOT_EQUAL -> !newLeft.equals(newRight);
            case GREATER_THAN -> leftInt > rightInt;
            case GREATER_OR_EQUAL -> leftInt >= rightInt;
            default -> false;
        };
    }
    
    private void resolveExpression(String expression, PluginLogger logger){
        if(expression == null || expression.isEmpty()){
            result = ExpressionResult.INVALID_NO_EXPRESSION;
            logger.warn("Encountered empty expression!");
            return;
        }
        
        char[] chars = expression.toCharArray();
        
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        
        boolean foundOperator = false;
        
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            
            if((c != '<' && c != '>' && c != '=' && c != '!') || i + 1 >= chars.length){
                if(foundOperator){
                    right.append(c);
                }else{
                    left.append(c);
                }
                continue;
            }
            
            if(foundOperator){
                result = ExpressionResult.INVALID_DOUBLE_OPERATOR;
                logger.warn("Invalid Expression '%s'! Cannot have multiple operants.", expression);
                return;
            }
            
            foundOperator = true;
            
            char next = chars[i + 1];
            if(c == '!' && next != '='){
                result = ExpressionResult.INVALID_BROKEN_NOT_EQUAL;
                logger.warn("Invalid Expression '%s'! Cannot have ! without = afterwards.", expression);
                return;
            }
            
            if(c == '!'){
                operator = Operator.NOT_EQUAL;
                i++;
                continue;
            }
            
            if(c == '<'){
                if(next == '='){
                    i++;
                    operator = Operator.LESS_OR_EQUAL;
                }else{
                    operator = Operator.LESS_THAN;
                }
                
                continue;
            }
            
            if(c == '>'){
                if(next == '='){
                    i++;
                    operator = Operator.GREATER_OR_EQUAL;
                }else{
                    operator = Operator.GREATER_THAN;
                }
                
                continue;
            }
            
            operator = Operator.EQUAL;
        }
        
        if(left.isEmpty() || right.isEmpty()){
            result = ExpressionResult.INVALID_EMPTY_PARTS;
            logger.warn("Invalid Expression '%s'! One of the expression's parts was empty.", expression);
            return;
        }
        
        this.left = left.toString();
        this.right = right.toString();
    }
    
    public ExpressionResult getResult(){
        return result;
    }
    
    private int getInt(String text){
        try{
            return Integer.parseInt(text);
        }catch(NumberFormatException ex){
            return text.length();
        }
    }
    
    public enum Operator{
        LESS_THAN,
        LESS_OR_EQUAL,
        EQUAL,
        NOT_EQUAL,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        
        UNKNOWN
    }
    
    public enum ExpressionResult{
        VALID,
        
        INVALID_NO_EXPRESSION,
        INVALID_DOUBLE_OPERATOR,
        INVALID_BROKEN_NOT_EQUAL,
        INVALID_EMPTY_PARTS
    }
}
