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

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 11/03/16.
 */
public class SubCmdTestReflection {

    @Test
    public void reflectionTest() {
        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new SubCmdTestReflection());

        processor.registerRegistrationHandler((registrationHandleResults, targetList, manager, ticket) -> {
            System.out.println("Process List: "+registrationHandleResults);

            return RegistrationHandleResult.accept();
        });

        processor.processAndInvoke("say", "hello", "special");

        Optional<CommandSpec> cmdOpt = processor.getCommand("say");

        cmdOpt.ifPresent(cmd -> System.out.println("Nome: " + cmd.getName() + ". Descrição: " + cmd.getDescription()));

    }

    @SubCommand(value = {"say", "hello"}, commandSpec = @Command(isOptional = true))
    public void special() {
        System.out.println("Hello <3");
    }

    @SubCommand(value = "say", commandSpec = @Command(isOptional = true))
    public void hello() {
        System.out.println("Hello :D");
    }

    @Command(desc = "Diz Olá :D")
    public void say() {
        System.out.println("Say...?");
    }


}
