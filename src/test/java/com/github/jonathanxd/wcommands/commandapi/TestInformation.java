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
package com.github.jonathanxd.wcommands.commandapi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.arguments.converters.CollectionConverter;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 12/03/16.
 */
public class TestInformation {

    @Test
    public void testInformation() {
        // Create a list of persons
        List<Person> persons = new ArrayList<>();
        // Add "Maria" to list
        persons.add(new Person("Maria", 19));
        // Add "Jose" to list
        persons.add(new Person("Jose", 35));

        // Create Manager
        WCommandCommon wCommandCommon = new WCommandCommon();

        // Register command
        wCommandCommon.getRegister(new CommonTicket<>(this)).registerCommand(CommandFactory
                .commandBuilder()
                // Create command "message"
                .withName("message")
                // Defines the description
                .withDescription("Send message to another person!")
                // Define handler
                .withCommonHandler((commandData, requirements, information /* InformationRegister Here! */) -> {

                    // Get command holder
                    CommandHolder commandHolder = commandData.getCommand();

                    // Get person argument
                    Optional<Person> personOpt = commandHolder.getArgValue(ID.PERSON);

                    // Get message argument
                    Optional<String> message = commandHolder.<ID, String>getArgValue(ID.MESSAGE);

                    // Retrieve sender from InformationRegister
                    Optional<Person> sender = information.getOptional(Sender.class);

                    // Check if optionals is present
                    if (personOpt.isPresent() && message.isPresent() && sender.isPresent()) {
                        // Print
                        // {sender name} send message '{message}' to {person}
                        System.out.println(sender.get().getName() + " send message '" + message.get() + "' to " + personOpt.get().getName());
                    } else {
                        throw new RuntimeException("Argument parsing error??");
                    }

                    return null;
                })
                // Register argument
                .withArgument(CommandFactory
                        // Create argument builder
                        .<ID, Person>argumentBuilder()
                        // Set argument id
                        .withId(ID.PERSON)
                        // Set argument converter
                        .withConverter(new CollectionConverter<>(persons, (p, in) -> in.stream().anyMatch(t -> t.equalsIgnoreCase(p.getName()))))
                        // Build argument
                        .build())
                .withArgument(CommandFactory
                        // Create argument builder
                        .argumentBuilder()
                        // Set argument id
                        .withId(ID.MESSAGE)
                        // Build argument
                        .build()
                )
                // Build command
                .build());

        // Process & Invoke the commands
        wCommandCommon.processAndInvoke(
                // Create information builder
                InformationRegister.blankBuilder()
                        // Add "Carlos" as Sender
                        .with(Sender.class, new Person("Carlos", 25))
                        // Build information
                        .build(),
                // Pass the arguments, not needed to pass and parse "Carlos".
                "message", "Maria", "Hi :D");
    }

    enum ID {
        PERSON,
        MESSAGE
    }

    static class Sender {
    }

    static class Person {
        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}
