/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
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

package ch.andre601.advancedserverlist.core.parsing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.function.Function;

public class ComponentParser{
    
    private static final MiniMessage mm;
    private static final LegacyComponentSerializer legacy;
    
    static {
        mm = MiniMessage.miniMessage();
        legacy = LegacyComponentSerializer.legacySection();
    }
    
    private String text;
    
    private ComponentParser(String text){
        this.text = text;
    }
    
    public static ComponentParser text(String text){
        return new ComponentParser(text);
    }
    
    public static ComponentParser list(List<String> lines){
        return new ComponentParser(String.join("\n", lines));
    }
    
    public ComponentParser modifyText(Function<String, String> function){
        this.text = function.apply(text);
        return this;
    }
    
    public Component toComponent(){
        return mm.deserialize(text);
    }
    
    @Override
    public String toString(){
        return legacy.serialize(toComponent());
    }
}
