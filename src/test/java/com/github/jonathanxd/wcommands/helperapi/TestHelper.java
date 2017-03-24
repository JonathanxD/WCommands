/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands.helperapi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.help.HelperAPI;
import com.github.jonathanxd.wcommands.ext.help.error.HelperErrorHandler;
import com.github.jonathanxd.wcommands.ext.help.printer.CommonPrinter;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import org.junit.Test;

public class TestHelper {

    @Test
    public void testHelper() {

        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new HelperErrorHandler(CommonPrinter.TO_SYS_OUT), new TestHelper());

        InformationRegister register = InformationRegister
                // Create information builder
                .builderWithList(processor)
                // Define the Sender as Sender "User"
                .with(Sender.class, new Sender("User"))
                // Build
                .build();

        processor.processAndInvoke(register);

        processor.processAndInvoke(register, "say");
    }

    @SubCommand(value = {"say", "hello"}, commandSpec = @Command(isOptional = true))
    public void special(@Info(description = "Message sender") Information<Sender> sender) {
        System.out.println(String.format("Hello %s <3", sender.get().name));
        System.out.println("Description: "+sender.getDescription().getProvidedByUnknownSource());
    }

    @SubCommand(value = "say", commandSpec = @Command(isOptional = true))
    public void hello() {
        System.out.println("Hello :D");
    }

    @Command(desc = "Diz Olá :D")
    public void say(@Argument(id = "Text") String text) {
        System.out.println(text);
    }

    @SubCommand(value = "say", commandSpec = @Command(desc = "Say Hi"))
    public void hi()  {
        System.out.println("Hi");
    }

    @SubCommand(value = {"say", "hi"}, commandSpec = @Command(desc = "Say Ultra"))
    public void ultra()  {
        System.out.println("Ultra");
    }

    @Command(desc = "Help command")
    public void help(CommandList commandSpecs) {
        CommonPrinter.TO_SYS_OUT.printString("<---> Help <--->");
        HelperAPI.help(commandSpecs, new InformationRegister(), CommonPrinter.TO_SYS_OUT);
    }

    /**
     * Command Sender
     */
    static class Sender {
        final String name;

        Sender(String name) {
            this.name = name;
        }
    }


}
