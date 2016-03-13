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

/**
 * Created by jonathan on 11/03/16.
 */
public class TestHelper {

    public static void main(String[] args) {

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
        HelperAPI.help(commandSpecs, CommonPrinter.TO_SYS_OUT);
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
