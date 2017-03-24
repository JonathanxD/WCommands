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
package com.github.jonathanxd.wcommands.reflection;

import com.github.jonathanxd.wcommands.ext.help.HelperAPI;
import com.github.jonathanxd.wcommands.ext.help.printer.CommonPrinter;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import org.junit.Test;

public class TestInformation {

    @Test
    public void testInformation() {
        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new TestInformation());

        InformationRegister information = InformationRegister
                // Create information builder
                .builderWithList(processor)
                // Define the Sender as Sender "User"
                .with(Sender.class, new Sender("User"))
                // Build
                .build();

        processor.processAndInvoke(information,
                // Pass arguments, not needed to pass "User" as argument
                "say", "hello", "special");

        processor.processAndInvoke(information,
                // Pass arguments, not needed to pass "User" as argument
                "say", "hello", "special");

        processor.processAndInvoke(information,
                // Pass arguments, not needed to pass "User" as argument
                "say", "hello");

    }

    /**
     * Send hello message to {@code sender}
     *
     * At time has 1 way to get information, declaring all information parameter in order after the
     * arguments, declare information argument isn't required, but if you declare 1, you need to
     * declare all information IN ORDER of registration. It will be changed soon.
     *
     * @param entityInformation Message sender
     */
    @SubCommand(value = {"say", "hello"}, commandSpec = @Command(isOptional = true))
    public void special(@Info(description = "Message sender") Information<Entity> entityInformation) {
        System.out.println(String.format("Hello %s <3", entityInformation.get().getName()));
        System.out.println("Description: "+entityInformation.getDescription().getProvidedByUnknownSource());
    }

    @SubCommand(value = "say", commandSpec = @Command(isOptional = true))
    public void hello(@Info(description = "etc") Entity en) {
        System.out.println("Hello :D = "+en.getName());
    }

    @Command(desc = "Diz Olá :D")
    public void say() {
        System.out.println("Say...?");
    }

    /**
     * Entity
     */

    interface Entity {
        String getName();
    }

    /**
     * Command Sender
     */
    static class Sender implements Entity {
        final String name;

        Sender(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }


}
