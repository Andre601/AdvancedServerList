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

package ch.andre601.advancedserverlist.core.interfaces.commands;

public interface CmdSender{
    
    String prefix = "<grey>[<gradient:aqua:white>AdvancedServerList</gradient>] ";
    String errorPrefix = "<grey>[<gradient:dark_red:red>AdvancedServerList</gradient>] ";
    
    boolean hasPermission(String permission);
    
    void sendMsg(String msg, Object... args);
    
    default void sendMsg(){
        sendMsg("");
    }
    
    default void sendPrefixedMsg(String msg, Object... args){
        sendMsg(prefix + msg, args);
    }
    
    default void sendErrorMsg(String msg, Object... args){
        sendMsg(errorPrefix + msg, args);
    }
}
