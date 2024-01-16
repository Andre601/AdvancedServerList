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

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.conditions.operators.ListOperator;
import ch.andre601.advancedserverlist.core.profiles.conditions.operators.Operator;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ConstantExpressionTemplate;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplate;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplates;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.Token;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTemplateParser{
    
    private final ImmutableMap<Token, Operator> operators;
    private final ImmutableList<ValueReader> valueReaders;
    
    public ExpressionTemplateParser(ImmutableMap<Token, Operator> operators, ImmutableList<ValueReader> valueReaders){
        this.operators = operators;
        this.valueReaders = valueReaders;
    }
    
    public ExpressionTemplate parse(List<Token> tokenList, PluginLogger logger){
        List<ExpressionTemplate> parts = new ArrayList<>();
        List<Operator> operators = new ArrayList<>();
        
        try{
            parts.add(read(tokenList));
        }catch(IllegalArgumentException ex){
            logger.warn(ex.getMessage());
            return ConstantExpressionTemplate.of("");
        }
        
        while(!tokenList.isEmpty()){
            Token token = tokenList.remove(0);
            Operator operator = this.operators.get(token);
            if(operator == null){
                logger.warn("Error while parsing Expression. Got \"" + token + "\" but expected OPERATOR.");
                return ConstantExpressionTemplate.of("");
            }
            
            operators.add(operator);
            if(tokenList.isEmpty()){
                logger.warn("Unexpected end of input for expression.");
                return ConstantExpressionTemplate.of("");
            }
            
            try{
                parts.add(read(tokenList));
            }catch(IllegalArgumentException ex){
                logger.warn(ex.getMessage());
                return ConstantExpressionTemplate.of("");
            }
        }
        
        while(!operators.isEmpty()){
            Operator operator = operators.get(0);
            int lowest = operator.getPriority();
            int start = 0;
            int end = 1;
            for(int i = 1; i < operators.size(); i++){
                operator = operators.get(i);
                if(operator.getPriority() < lowest){
                    lowest = operator.getPriority();
                    start = i;
                    end = i + 1;
                }else
                if(operator.getPriority() > lowest){
                    break;
                }else{
                    end++;
                }
            }
            
            operator = operators.get(start);
            
            ExpressionTemplate replacement;
            if(start + 1 == end){
                replacement = operator.createTemplate(parts.get(start), parts.get(end));
            }else
            if(operator instanceof ListOperator listOperator){
                replacement = listOperator.createTemplate(new ArrayList<>(parts.subList(start, end + 1)));
            }else{
                List<ExpressionTemplate> conditions = new ArrayList<>(end - start);
                for(int i = start; i < end; i++)
                    conditions.add(operators.get(i).createTemplate(parts.get(i), parts.get(i + 1)));
                
                replacement = ExpressionTemplates.and(conditions);
            }
            
            for(int i = start; i < end; i++){
                parts.remove(start);
                operators.remove(start);
            }
            parts.set(start, replacement);
        }
        
        return parts.get(0);
    }
    
    ExpressionTemplate read(List<Token> tokenList){
        for(ValueReader valueReader : valueReaders){
            ExpressionTemplate template = valueReader.read(this, tokenList);
            if(template != null)
                return template;
        }
        
        throw new IllegalArgumentException("Invalid Expression. Expected literal but got token \"" + tokenList.get(0).toString() + "\"");
    }
}
