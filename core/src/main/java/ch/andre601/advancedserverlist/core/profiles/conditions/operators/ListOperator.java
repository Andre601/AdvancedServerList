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

package ch.andre601.advancedserverlist.core.profiles.conditions.operators;

import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class ListOperator extends Operator{
    
    public ListOperator(int priority){
        super(priority);
    }
    
    public static ListOperator of(int priority, Function<Collection<ExpressionTemplate>, ExpressionTemplate> function){
        return new ListOperator(priority){
            @Override
            public ExpressionTemplate createTemplate(List<ExpressionTemplate> operands){
                return function.apply(operands);
            }
        };
    }
    
    @Override
    public ExpressionTemplate createTemplate(ExpressionTemplate a, ExpressionTemplate b){
        return createTemplate(Arrays.asList(a, b));
    }
    
    public abstract ExpressionTemplate createTemplate(List<ExpressionTemplate> operands);
}
