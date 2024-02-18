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

import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;

import java.util.ArrayList;
import java.util.List;

public class ExpressionsWarnHelper{
    
    private final String expression;
    private final List<Context> warnings = new ArrayList<>();
    
    public ExpressionsWarnHelper(String expression){
        this.expression = expression;
    }
    
    public void appendWarning(int position, String warning, Object... args){
        warnings.add(new Context(position, String.format(warning, args)));
    }
    
    public boolean hasWarnings(){
        return !warnings.isEmpty();
    }
    
    public void printWarnings(PluginLogger logger){
        logger.warn("Encountered %d issue(s) while parsing expression '%s'", warnings.size(), expression);
        for(Context warning : warnings){
            if(warning.position() > -1){
                logger.warn(" - At Position %d: %s", warning.position(), warning.message());
            }else{
                logger.warn(" - Cause: %s", warning.message());
            }
        }
    }
    
    public record Context(int position, String message){}
}
