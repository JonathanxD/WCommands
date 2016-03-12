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

import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.infos.Information;

/**
 * Created by jonathan on 11/03/16.
 */
public class TestInformation {

    public static void main(String[] args) {
        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new TestInformation());
        try {

            processor.processAndInvoke(Information
                            // Create information builder
                            .builder()
                            // Define the Sender as Sender "User"
                            .with(Sender.class, new Sender("User"))
                            // Build
                            .build(),
                    // Pass arguments, not needed to pass "User" as argument
                    "say", "hello", "special");
        } catch (ArgumentProcessingError error) {
            error.printStackTrace();
        }
    }

    /**
     * Send hello message to {@code sender}
     *
     * At time has 1 way to get information, declaring all information parameter in order after the
     * arguments, declare information argument isn't required, but if you declare 1, you need to
     * declare all information IN ORDER of registration. It will be changed soon.
     *
     * @param sender Message sender
     */
    @SubCommand(value = {"say", "hello"}, commandSpec = @Command(isOptional = true))
    public void special(Sender sender) {
        System.out.println(String.format("Hello %s <3", sender.name));
    }

    @SubCommand(value = "say", commandSpec = @Command(isOptional = true))
    public void hello() {
        System.out.println("Hello :D");
    }

    @Command(desc = "Diz Olá :D")
    public void say() {
        System.out.println("Say...?");
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
