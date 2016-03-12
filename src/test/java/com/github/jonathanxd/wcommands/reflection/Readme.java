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

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;

import java.util.Optional;

/**
 * Created by jonathan on 28/02/16.
 */
public class Readme {

    public static void main(String[] args) throws ArgumentProcessingError {
        Readme readme = new Readme();
        ReflectionCommandProcessor commandProcessor = ReflectionAPI.createWCommand(readme);

        commandProcessor.processAndInvoke("say");

        System.out.println("[SAY] "+readme.say);

        commandProcessor.processAndInvoke("say", "foo bar");

        System.out.println("[SAY] "+readme.say);

        commandProcessor.processAndInvoke("say2");

        commandProcessor.processAndInvoke("say2", "foo bar");

        subCommandTest(commandProcessor);

    }

    @Command
    @Argument(isOptional = true)
    private String say = "foo";

    @Command
    public void say2(@Argument(isOptional = true) Optional<String> text) {
        System.out.println("[SAY2] "+ text.orElse("foo"));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    public static void subCommandTest(WCommandCommon commandProcessor) throws ArgumentProcessingError {
        commandProcessor.processAndInvoke("greet");
        commandProcessor.processAndInvoke("greet", "special");
        commandProcessor.processAndInvoke("greet", "special", "fan");

    }

    // Sub commands

    //Defines command named greet
    @Command
    public void greet() {
        System.out.println("Hi!");
    }
    // Create a sub command named special for greet command
    @SubCommand(value = "greet", commandSpec = @Command(isOptional = true))
    public void special() {
        System.out.println("Hi <3");
    }
    // Also you can create a Sub command for a sub command.
    @SubCommand(value = {"greet", "special"}, commandSpec = @Command(isOptional = true))
    public void fan() {
        System.out.println(" ❤ ");
    }

}
