/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.wcommands.reflection;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;

import org.junit.Test;

import java.util.Optional;

/**
 * Created by jonathan on 28/02/16.
 */
public class Readme {

    @Test
    public void readme() throws ProcessingError {
        Readme readme = new Readme();
        ReflectionCommandProcessor commandProcessor = ReflectionAPI.createWCommand(readme);

        commandProcessor.processAndInvoke("say");

        System.out.println("[SAY] "+readme.say);

        commandProcessor.processAndInvoke("say", "foo bar");

        System.out.println("[SAY] "+readme.say);

        commandProcessor.processAndInvoke("say2");

        commandProcessor.processAndInvoke("say2", "foo bar");

        subCommandTest(commandProcessor);

    }

    @Command
    @Argument(isOptional = true)
    private String say = "foo";

    @Command
    public void say2(@Argument(isOptional = true) Optional<String> text) {
        System.out.println("[SAY2] "+ text.orElse("foo"));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    public static void subCommandTest(WCommandCommon commandProcessor) throws ProcessingError {
        commandProcessor.processAndInvoke("greet");
        commandProcessor.processAndInvoke("greet", "special");
        commandProcessor.processAndInvoke("greet", "special", "fan");

    }

    // Sub commands

    //Defines command named greet
    @Command
    public void greet() {
        System.out.println("Hi!");
    }
    // Create a sub command named special for greet command
    @SubCommand(value = "greet", commandSpec = @Command(isOptional = true))
    public void special() {
        System.out.println("Hi <3");
    }
    // Also you can create a Sub command for a sub command.
    @SubCommand(value = {"greet", "special"}, commandSpec = @Command(isOptional = true))
    public void fan() {
        System.out.println(" ❤ ");
    }

}
