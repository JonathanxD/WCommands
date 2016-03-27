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
