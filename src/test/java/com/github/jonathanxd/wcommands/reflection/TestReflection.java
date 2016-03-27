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
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;

import org.junit.Test;

import java.util.Optional;

/**
 * Created by jonathan on 11/03/16.
 */
public class TestReflection {

    @Test
    public void reflectionTest() {
        ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new TestReflection());

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
