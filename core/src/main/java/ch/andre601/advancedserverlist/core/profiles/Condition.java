package ch.andre601.advancedserverlist.core.profiles;

import ch.andre601.advancedserverlist.core.interfaces.ProxyLogger;

import java.util.Locale;

public class Condition{
    
    private final String expression;
    
    public Condition(String expression){
        this.expression = expression;
    }
    
    public boolean eval(String name, int protocol, ProxyLogger logger){
        if(expression == null || expression.isEmpty())
            return true;
        
        String newExpression = replacePlaceholders(name, protocol);
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
    
    private String replacePlaceholders(String name, int protocol){
        char[] chars = expression.toCharArray();
        
        StringBuilder builder = new StringBuilder(expression.length());
        StringBuilder placeholder = new StringBuilder();
        
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            
            if(c != '{' || i + 1 >= chars.length){
                builder.append(c);
                continue;
            }
            
            boolean valid = false;
            
            while(++i < chars.length){
                char p = chars[i];
                
                if(p == ' ' || p == '<' || p == '>' || p == '=' || p == '!')
                    break;
                
                if(p == '}'){
                    valid = true;
                    break;
                }
                
                placeholder.append(p);
            }
            
            String finalPlaceholder = placeholder.toString();
            
            if(!valid){
                builder.append('{').append(finalPlaceholder);
                continue;
            }
            
            if(finalPlaceholder.equals("playerName")){
                builder.append(name);
            }else
            if(finalPlaceholder.equals("playerVersion")){
                builder.append(protocol);
            }else{
                builder.append('{').append(finalPlaceholder).append('}');
            }
        }
        
        return builder.toString();
    }
    
    private int tryParse(String value){
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException ex){
            return value.length();
        }
    }
}
