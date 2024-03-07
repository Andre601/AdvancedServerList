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

package ch.andre601.advancedserverlist.core.profiles.conditions;

import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.core.profiles.conditions.placeholders.reader.PlaceholderTokenReader;
import ch.andre601.advancedserverlist.core.profiles.conditions.placeholders.reader.PlaceholderValueReader;
import ch.andre601.advancedserverlist.core.profiles.conditions.placeholders.tokens.CustomExpressionTokenizer;
import ch.andre601.expressionparser.ExpressionParserEngine;
import ch.andre601.expressionparser.ParseWarnCollector;
import ch.andre601.expressionparser.operator.Operator;
import ch.andre601.expressionparser.parsers.ExpressionTemplateParser;
import ch.andre601.expressionparser.parsers.ValueReader;
import ch.andre601.expressionparser.templates.ExpressionTemplate;
import ch.andre601.expressionparser.tokens.Token;
import ch.andre601.expressionparser.tokens.readers.TokenReader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class ProfileConditionParser implements ExpressionParserEngine{
    
    private final CustomExpressionTokenizer tokenizer;
    private final ExpressionTemplateParser parser;
    
    private ProfileConditionParser(List<TokenReader> tokenReaders, ImmutableMap<Token, Operator> operators, ImmutableList<ValueReader> valueReaders){
        this.tokenizer = new CustomExpressionTokenizer(tokenReaders);
        this.parser = new ExpressionTemplateParser(operators, valueReaders);
    }
    
    public static ProfileConditionParser create(){
        return new ProfileConditionParserBuilder().addDefaults()
            .addTokenReader(new PlaceholderTokenReader(-20))
            .addValueReader(new PlaceholderValueReader())
            .build();
    }
    
    public ExpressionTemplate compile(String text, GenericPlayer player, GenericServer server, ParseWarnCollector collector){
        return parser.parse(tokenizer.parse(text, player, server, collector), collector);
    }
    
    @Override
    public ExpressionTemplate compile(String s, ParseWarnCollector parseWarnCollector){
        return null;
    }
    
    private static class ProfileConditionParserBuilder extends ExpressionParserEngine.Builder<ProfileConditionParser>{
        @Override
        public ProfileConditionParser build(){
            return new ProfileConditionParser(this.getTokenReaders(), ImmutableMap.copyOf(this.getOperators()), ImmutableList.copyOf(this.getValueReaders()));
        }
    }
}
