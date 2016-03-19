/*
 * 	WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *     Copyright (C) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *
 * 	GNU GPLv3
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jonathanxd.wcommands.commandapi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.arguments.converters.CollectionConverter;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 12/03/16.
 */
public class TestInformation {

    public static void main(String[] args) {
        // Create a list of persons
        List<Person> persons = new ArrayList<>();
        // Add "Maria" to list
        persons.add(new Person("Maria", 19));
        // Add "Jose" to list
        persons.add(new Person("Jose", 35));

        // Create Manager
        WCommandCommon wCommandCommon = new WCommandCommon();

        // Register command
        wCommandCommon.registerCommand(CommandFactory
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
                        .withConverter(new CollectionConverter<>(persons, (p, in) -> p.getName().equalsIgnoreCase(in)))
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
