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

package ch.andre601.advancedserverlist.core.profiles.conditions.tokens;

public class DefaultTokens{
    public static final Token AND = new Token("AND");
    public static final Token OR = new Token("OR");
    public static final Token OPENING_PARENTHESIS = new Token("OPENING_PARENTHESIS");
    public static final Token CLOSING_PARENTHESIS = new Token("CLOSING_PARENTHESIS");
    public static final Token EQUAL = new Token("EQUAL");
    public static final Token NOT_EQUAL = new Token("NOT_EQUAL");
    public static final Token EQUAL_IGNORE_CASE = new Token("EQUAL_IGNORE_CASE");
    public static final Token NOT_EQUAL_IGNORE_CASE = new Token("NOT_EQUAL_IGNORE_CASE");
    public static final Token NEGATION = new Token("NEGATION");
    public static final Token GREATER_THAN = new Token("GREATER_THAN");
    public static final Token GREATER_OR_EQUAL_THAN = new Token("GREATER_OR_EQUAL_THAN");
    public static final Token LESSER_THAN = new Token("LESSER_THAN");
    public static final Token LESSER_OR_EQUAL_THAN = new Token("LESSER_OR_EQUAL_THAN");
    public static final Token CONCAT_STRING = new Token("CONCAT_STRING");
    public static final Token ADD = new Token("ADD");
    public static final Token SUB = new Token("SUB");
    public static final Token MULT = new Token("MULT");
    public static final Token DIV = new Token("DIV");
}
