package ch.andre601.advancedserverlist.core.profiles;

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConditionsHolder{
    
    private final List<String> expressions = new ArrayList<>();
    private final StringReplacer replacer = new StringReplacer();
    
    public ConditionsHolder(List<String> expressions){
        if(expressions != null && !expressions.isEmpty())
            this.expressions.addAll(expressions);
    }
    
    public void replace(String from, Object to){
        replacer.add(from, to);
    }
    
    public boolean eval(PluginLogger logger){
        if(expressions.isEmpty())
            return true;
        
        for(String expression : expressions){
            if(!parseExpression(expression, logger))
                return false;
        }
        
        return true;
    }
    
    private boolean parseExpression(String expression, PluginLogger logger){
        if(expression == null || expression.isEmpty())
            return true;
    
        String newExpression = replacer.replace(expression);
        
        char[] chars = newExpression.toCharArray();
    
        StringBuilder left = new StringBuilder();
        StringBuilder operator = new StringBuilder();
        StringBuilder right = new StringBuilder();
    
        boolean operatorFound = false;
    
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
        
            if(c == ' ')
                continue;
        
            if((c != '<' && c != '>' && c != '=' && c != '!') || i + 1 >= chars.length){
                if(operatorFound){
                    right.append(c);
                    continue;
                }
            
                left.append(c);
                continue;
            }
        
            if(operatorFound){
                logger.warn("Encountered second operator in condition! AdvancedServerList currently only supports one operator!");
                return false;
            }
        
            operatorFound = true;
        
            char next = chars[i + 1];
            if(c == '!' && next != '='){
                logger.warn("Invalid condition found. Expected '!=' but found '!" + next + "' in condition " + newExpression);
                return false;
            }
        
            operator.append(c);
            i++;
        
            if(c == '!' || ((c == '<' || c == '>') && next == '=')){
                operator.append(next);
            }
        }
    
        if(left.isEmpty() || operator.isEmpty() || right.isEmpty()){
            logger.warn("Failed to evaluate condition. One part of the expression was empty!");
            return false;
        }
    
        int leftInt = tryParse(left.toString());
        int rightInt = tryParse(right.toString());
    
        return switch(operator.toString().toLowerCase(Locale.ROOT)){
            case ">" -> leftInt > rightInt;
            case ">=" -> leftInt >= rightInt;
            case "=" -> left.toString().equals(right.toString());
            case "!=" -> !left.toString().equals(right.toString());
            case "<" -> leftInt < rightInt;
            case "<=" -> leftInt <= rightInt;
            default -> false;
        };
    
    }
    
    private int tryParse(String value){
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException ex){
            return value.length();
        }
    }
}
