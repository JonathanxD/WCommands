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
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Created by jonathan on 11/03/16.
 */
public class TestNewInformationAPI {

    public static void main(String[] args) {
        new TestNewInformationAPI().newInformationAPITest();
    }

    @Test
    public void newInformationAPITest() {
        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new TestNewInformationAPI());

        processor.registerRegistrationHandler((registrationHandleResults, targetList, manager, ticket) -> {
            System.out.println("Results: " + registrationHandleResults + " Manager: " + manager);

            RegistrationHandleResult result = registrationHandleResults.getLast();
            Optional<CommandSpec> commandSpecOpt = result.getResult();

            if (commandSpecOpt.isPresent()) {
                CommandSpec commandSpec = commandSpecOpt.get();
                System.out.println("Command spec = " + commandSpec + " -> target = "+targetList.getHoldingObject());
            }

            return RegistrationHandleResult.accept();
        });

        InformationRegister information = InformationRegister
                // Create information builder
                .builderWithList(processor)
                // Define the receiver as User2
                .with(Receiver.class, (Entity) () -> "User2")
                // Define the Sender as Sender "User"
                .with(Sender.class, new Sender("User"))
                // Build
                .build();

        Instant instant = Instant.now();
        processor.processAndInvoke(information,
                // Pass arguments, not needed to pass "User" as argument
                "pm", "Hi :D");
        Duration duration = Duration.between(instant, Instant.now());

        System.out.println("Time to process: "+duration.toMillis());
    }

    @Command(desc = "Send a private message!")
    public void pm(@Argument String message,
                   @Info(type = Sender.class, description = "etc") Entity en,
                   @Info(type = Receiver.class) Entity receiver) {
        System.out.println(en.getName() + " send message " + message + " to " + receiver.getName());
    }

    @SubCommand(value = "pm", commandSpec = @Command(name = "player"))
    public void pmPlayer() {
        System.out.println("Pm Player");
    }

    /**
     * Entity
     */

    interface Entity {
        String getName();
    }

    static class Receiver {
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
