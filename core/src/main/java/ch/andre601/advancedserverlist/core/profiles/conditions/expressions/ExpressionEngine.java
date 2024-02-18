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

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.interfaces.PluginLogger;
import ch.andre601.advancedserverlist.core.profiles.conditions.ExpressionTokenizer;
import ch.andre601.advancedserverlist.core.profiles.conditions.operators.ListOperator;
import ch.andre601.advancedserverlist.core.profiles.conditions.operators.Operator;
import ch.andre601.advancedserverlist.core.profiles.conditions.parsers.*;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplate;
import ch.andre601.advancedserverlist.core.profiles.conditions.templates.ExpressionTemplates;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.BooleanToken;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.DefaultTokens;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.Token;
import ch.andre601.advancedserverlist.core.profiles.conditions.tokens.readers.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionEngine{
    
    private final ExpressionTokenizer tokenizer;
    private final ExpressionTemplateParser templateParser;
    
    public ExpressionEngine(){
        Options options = new Options.Builder()
            .withDefaultTokenReaders()
            .withDefaultOperators()
            .withDefaultValueReaders()
            .build();
        
        this.tokenizer = new ExpressionTokenizer(options.tokenReaders);
        this.templateParser = new ExpressionTemplateParser(options.operators, options.valueReaders);
    }
    
    public ExpressionTemplate compile(String expression, GenericPlayer player, GenericServer server, ExpressionsWarnHelper warnHelper){
        return templateParser.parse(tokenizer.parse(expression, player, server, warnHelper), warnHelper);
    }
    
    public static class Options{
        
        private final List<TokenReader> tokenReaders;
        private final ImmutableMap<Token, Operator> operators;
        private final ImmutableList<ValueReader> valueReaders;
        
        private Options(List<TokenReader> tokenReaders, ImmutableMap<Token, Operator> operators, ImmutableList<ValueReader> valueReaders){
            this.tokenReaders = tokenReaders;
            this.operators = operators;
            this.valueReaders = valueReaders;
        }
        
        public static class Builder{
            private final List<TokenReader> tokenReaders = new ArrayList<>();
            private final Map<Token, Operator> operators = new HashMap<>();
            private final List<ValueReader> valueReaders = new ArrayList<>();
            
            public Builder(){}
            
            public Builder withDefaultTokenReaders(){
                return this
                    .tokenReader(new PatternTokenReader(BooleanToken.FALSE, "false"))
                    .tokenReader(new PatternTokenReader(BooleanToken.TRUE, "true"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.AND, "and"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.AND, "&&"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.OR, "or"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.OR, "||"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.EQUAL, "=="))
                    .tokenReader(new PatternTokenReader(DefaultTokens.EQUAL_IGNORE_CASE, "=~"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.EQUAL, "="))
                    .tokenReader(new PatternTokenReader(DefaultTokens.EQUAL_IGNORE_CASE, "~"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.NOT_EQUAL, "!="))
                    .tokenReader(new PatternTokenReader(DefaultTokens.NOT_EQUAL_IGNORE_CASE, "!~"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.STARTS_WITH, "|-"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.ENDS_WITH, "-|"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.CONTAINS, "<_"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.GREATER_OR_EQUAL_THAN, ">="))
                    .tokenReader(new PatternTokenReader(DefaultTokens.LESSER_OR_EQUAL_THAN, "<="))
                    .tokenReader(new PatternTokenReader(DefaultTokens.OPENING_PARENTHESIS, "("))
                    .tokenReader(new PatternTokenReader(DefaultTokens.CLOSING_PARENTHESIS, ")"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.NEGATION, "!"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.GREATER_THAN, ">"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.LESSER_THAN, "<"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.CONCAT_STRING, "."))
                    .tokenReader(new PatternTokenReader(DefaultTokens.ADD, "+"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.SUB, "-"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.MULT, "*"))
                    .tokenReader(new PatternTokenReader(DefaultTokens.DIV, "/"))
                    .tokenReader(new QuotedLiteralTokenReader(-10, '\"'))
                    .tokenReader(new QuotedLiteralTokenReader(-10, '\''))
                    .tokenReader(new PlaceholderTokenReader(-20))
                    .tokenReader(new NumberTokenReader(-50))
                    .tokenReader(new NonQuotedLiteralTokenReader(-100));
            }
            
            public Builder withDefaultOperators(){
                return this
                    .operator(DefaultTokens.AND, ListOperator.of(100, ExpressionTemplates::and))
                    .operator(DefaultTokens.OR, ListOperator.of(50, ExpressionTemplates::or))
                    .operator(DefaultTokens.EQUAL, Operator.of(25, ExpressionTemplates::equal))
                    .operator(DefaultTokens.NOT_EQUAL, Operator.of(25, ExpressionTemplates::notEqual))
                    .operator(DefaultTokens.EQUAL_IGNORE_CASE, Operator.of(25, ExpressionTemplates::equalIgnoreCase))
                    .operator(DefaultTokens.NOT_EQUAL_IGNORE_CASE, Operator.of(25, ExpressionTemplates::notEqualIgnoreCase))
                    .operator(DefaultTokens.STARTS_WITH, Operator.of(25, ExpressionTemplates::startsWith))
                    .operator(DefaultTokens.ENDS_WITH, Operator.of(25, ExpressionTemplates::endsWith))
                    .operator(DefaultTokens.CONTAINS, Operator.of(25, ExpressionTemplates::contains))
                    .operator(DefaultTokens.GREATER_THAN, Operator.of(25, ExpressionTemplates::greater))
                    .operator(DefaultTokens.GREATER_OR_EQUAL_THAN, Operator.of(25, ExpressionTemplates::greaterOrEqual))
                    .operator(DefaultTokens.LESSER_THAN, Operator.of(25, ExpressionTemplates::less))
                    .operator(DefaultTokens.LESSER_OR_EQUAL_THAN, Operator.of(25, ExpressionTemplates::lessOrEqual))
                    .operator(DefaultTokens.CONCAT_STRING, ListOperator.of(10, ExpressionTemplates::concat))
                    .operator(DefaultTokens.ADD, ListOperator.of(4, ExpressionTemplates::sum))
                    .operator(DefaultTokens.SUB, Operator.of(3, ExpressionTemplates::sub))
                    .operator(DefaultTokens.MULT, ListOperator.of(2, ExpressionTemplates::product))
                    .operator(DefaultTokens.DIV, Operator.of(1, ExpressionTemplates::div));
            }
            
            public Builder withDefaultValueReaders(){
                return this
                    .valueReader(new BooleanConstantReader())
                    .valueReader(new NumberConstantReader())
                    .valueReader(new StringConstantReader())
                    .valueReader(new NegatedExpressionReader(DefaultTokens.NEGATION))
                    .valueReader(new ParenthesisedExpressionReader(DefaultTokens.OPENING_PARENTHESIS, DefaultTokens.CLOSING_PARENTHESIS))
                    .valueReader(new PlaceholderReader())
                    .valueReader(new NegatedNumberReader(DefaultTokens.SUB));
            }
            
            public Builder tokenReader(TokenReader tokenReader){
                this.tokenReaders.add(tokenReader);
                return this;
            }
            
            public Builder operator(Token token, Operator operator){
                this.operators.put(token, operator);
                return this;
            }
            
            public Builder valueReader(ValueReader valueReader){
                this.valueReaders.add(valueReader);
                return this;
            }
            
            public Options build(){
                return new Options(this.tokenReaders, ImmutableMap.copyOf(operators), ImmutableList.copyOf(valueReaders));
            }
        }
    }
}
