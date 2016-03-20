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

/**
 * Created by jonathan on 11/03/16.
 */
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
