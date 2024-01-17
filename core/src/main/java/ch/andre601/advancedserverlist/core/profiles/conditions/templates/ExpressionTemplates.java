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

package ch.andre601.advancedserverlist.core.profiles.conditions.templates;

import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.Expressions;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToBooleanExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToDoubleExpression;
import ch.andre601.advancedserverlist.core.profiles.conditions.expressions.ToStringExpression;

import java.util.Collection;

public class ExpressionTemplates{
    
    public static ExpressionTemplate negate(ExpressionTemplate template){
        return new Negation(template);
    }
    
    public static ExpressionTemplate and(Collection<ExpressionTemplate> operands){
        return new And(operands);
    }
    
    public static ExpressionTemplate or(Collection<ExpressionTemplate> operands){
        return new Or(operands);
    }
    
    public static ExpressionTemplate concat(Collection<ExpressionTemplate> operands){
        return new Concatenate(operands);
    }
    
    public static ExpressionTemplate equal(ExpressionTemplate a, ExpressionTemplate b){
        return new Equal(a, b);
    }
    
    public static ExpressionTemplate notEqual(ExpressionTemplate a, ExpressionTemplate b){
        return new NotEqual(a, b);
    }
    
    public static ExpressionTemplate equalIgnoreCase(ExpressionTemplate a, ExpressionTemplate b){
        return new EqualIgnoreCase(a, b);
    }
    
    public static ExpressionTemplate notEqualIgnoreCase(ExpressionTemplate a, ExpressionTemplate b){
        return new NotEqualIgnoreCase(a, b);
    }
    
    public static ExpressionTemplate greater(ExpressionTemplate a, ExpressionTemplate b){
        return new Greater(a, b);
    }
    
    public static ExpressionTemplate greaterOrEqual(ExpressionTemplate a, ExpressionTemplate b){
        return new GreaterOrEqual(a, b);
    }
    
    public static ExpressionTemplate less(ExpressionTemplate a, ExpressionTemplate b){
        return new Less(a, b);
    }
    
    public static ExpressionTemplate lessOrEqual(ExpressionTemplate a, ExpressionTemplate b){
        return new LessOrEqual(a, b);
    }
    
    public static ExpressionTemplate sum(Collection<ExpressionTemplate> operands){
        return new Sum(operands);
    }
    
    public static ExpressionTemplate product(Collection<ExpressionTemplate> operands){
        return new Product(operands);
    }
    
    public static ExpressionTemplate sub(ExpressionTemplate a, ExpressionTemplate b){
        return new Sub(a, b);
    }
    
    public static ExpressionTemplate div(ExpressionTemplate a, ExpressionTemplate b){
        return new Div(a, b);
    }
    
    public static ExpressionTemplate negateNumber(ExpressionTemplate template){
        return new NegationNumber(template);
    }
    
    private static class Negation extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate template;
        
        Negation(ExpressionTemplate template){
            this.template = template;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.negate(template.instantiateWithBooleanResult());
        }
    }
    
    private static class And extends AbstractBooleanExpressionTemplate{
        
        private final Collection<ExpressionTemplate> operands;
        
        And(Collection<ExpressionTemplate> operands){
            this.operands = operands;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.and(operands.stream().map(ExpressionTemplate::instantiateWithBooleanResult).toList());
        }
    }
    
    private static class Or extends AbstractBooleanExpressionTemplate{
        
        private final Collection<ExpressionTemplate> operands;
        
        Or(Collection<ExpressionTemplate> operands){
            this.operands = operands;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.or(operands.stream().map(ExpressionTemplate::instantiateWithBooleanResult).toList());
        }
    }
    
    private static class Concatenate extends AbstractStringExpressionTemplate{
        
        private final Collection<ExpressionTemplate> operands;
        
        Concatenate(Collection<ExpressionTemplate> operands){
            this.operands = operands;
        }
        
        @Override
        public ToStringExpression instantiateWithStringResult(){
            return Expressions.concat(operands.stream().map(ExpressionTemplate::instantiateWithStringResult).toList());
        }
    }
    
    private static class Equal extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        Equal(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.equal(a.instantiateWithStringResult(), b.instantiateWithStringResult());
        }
    }
    
    private static class NotEqual extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        NotEqual(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.notEqual(a.instantiateWithStringResult(), b.instantiateWithStringResult());
        }
    }
    
    private static class EqualIgnoreCase extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        EqualIgnoreCase(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.equalIgnoreCase(a.instantiateWithStringResult(), b.instantiateWithStringResult());
        }
    }
    
    private static class NotEqualIgnoreCase extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        NotEqualIgnoreCase(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.notEqualIgnoreCase(a.instantiateWithStringResult(), b.instantiateWithStringResult());
        }
    }
    
    private static class Greater extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        Greater(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.greaterThan(a.instantiateWithDoubleResult(), b.instantiateWithDoubleResult());
        }
    }
    
    private static class GreaterOrEqual extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        GreaterOrEqual(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.greaterOrEqualThan(a.instantiateWithDoubleResult(), b.instantiateWithDoubleResult());
        }
    }
    
    private static class Less extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        Less(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.lessThan(a.instantiateWithDoubleResult(), b.instantiateWithDoubleResult());
        }
    }
    
    private static class LessOrEqual extends AbstractBooleanExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        LessOrEqual(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToBooleanExpression instantiateWithBooleanResult(){
            return Expressions.lessOrEqualThan(a.instantiateWithDoubleResult(), b.instantiateWithDoubleResult());
        }
    }
    
    private static class Sum extends AbstractDoubleExpressionTemplate{
        
        private final Collection<ExpressionTemplate> operands;
        
        Sum(Collection<ExpressionTemplate> operands){
            this.operands = operands;
        }
        
        @Override
        public ToDoubleExpression instantiateWithDoubleResult(){
            return Expressions.sum(operands.stream().map(ExpressionTemplate::instantiateWithDoubleResult).toList());
        }
    }
    
    private static class Product extends AbstractDoubleExpressionTemplate{
        
        private final Collection<ExpressionTemplate> operands;
        
        Product(Collection<ExpressionTemplate> operands){
            this.operands = operands;
        }
        
        @Override
        public ToDoubleExpression instantiateWithDoubleResult(){
            return Expressions.product(operands.stream().map(ExpressionTemplate::instantiateWithDoubleResult).toList());
        }
    }
    
    private static class Sub extends AbstractDoubleExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        Sub(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToDoubleExpression instantiateWithDoubleResult(){
            return Expressions.sub(a.instantiateWithDoubleResult(), b.instantiateWithDoubleResult());
        }
    }
    
    private static class Div extends AbstractDoubleExpressionTemplate{
        
        private final ExpressionTemplate a;
        private final ExpressionTemplate b;
        
        Div(ExpressionTemplate a, ExpressionTemplate b){
            this.a = a;
            this.b = b;
        }
        
        @Override
        public ToDoubleExpression instantiateWithDoubleResult(){
            return Expressions.div(a.instantiateWithDoubleResult(), b.instantiateWithDoubleResult());
        }
    }
    
    private static class NegationNumber extends AbstractDoubleExpressionTemplate{
        
        private final ExpressionTemplate template;
        
        NegationNumber(ExpressionTemplate template){
            this.template = template;
        }
        
        @Override
        public ToDoubleExpression instantiateWithDoubleResult(){
            return Expressions.negateNumber(template.instantiateWithDoubleResult());
        }
    }
}
