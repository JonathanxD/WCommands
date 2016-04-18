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

import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class Main {

    @Test
    public void mainTest() throws ProcessingError {
        WCommand<List<CommandData<CommandHolder>>> wCommand = CommonProcessor.newWCommand((e, d, l, v, r, t) -> ProcessAction.STOP);

        CommonProcessor.CommonHandler handler = (data, req, ref) -> {
            System.out.println("|CommandSpec|: " + data.getCommand());
            data.getCommand().eachArguments().plain(arg -> {
                System.out.println("ArgumentSpec: " + arg);
            });
            CommandHolder command = data.getCommand();

            Optional<List<String>> xPArgOpt = command.getPlainArgument(ArgumentIDs.XP_ARGUMENT);
            xPArgOpt.ifPresent(System.out::println);

            if(data.getParent() != null) {
                System.out.println("PARENT: " + data.getParent().getPlainArgument(ArgumentIDs.REGION_NAME));
            }
            return null;
        };

        ArgumentSpec argumentSpec = CommandFactory.create(ArgumentIDs.ITEM_ARGUMENT, () -> Text.of("(STONE)|(GOLD)|(AXE)", true, true));
        ArgumentSpec argumentSpecXP = CommandFactory.create(ArgumentIDs.XP_ARGUMENT, Matchable::acceptAny);
        ArgumentSpec argumentSpecName = CommandFactory.create(ArgumentIDs.REGION_NAME, Matchable::acceptAny);
        ArgumentSpec argumentSpecRegionName = CommandFactory.createOptional(ArgumentIDs.REGION_NAME, () -> s -> !s.equals("allow"));

        wCommand.getRegister(new CommonTicket<>(this)).registerCommand(
                CommandFactory.create("give", handler)
                        .addSub(CommandFactory.create("xp", Arguments.of(argumentSpecXP), handler))
                        .addSub(CommandFactory.create("item", Arguments.of(argumentSpec), handler))
        );

        wCommand.getRegister(new CommonTicket<>(this)).registerCommand(
                CommandFactory.create("region", Arguments.of(argumentSpecRegionName), handler)
                        .addSub(
                                CommandFactory.createOptional("allow", Arguments.of(argumentSpecName), handler)
                        )
        );

        wCommand.processAndInvoke("give", "xp", "12", "region", "ynt", "allow", "trb");
    }

    // /region [name] allow [user]
    // /region allow [user]

    enum ArgumentIDs {
        XP_ARGUMENT,
        ITEM_ARGUMENT,
        NAME_ARG,
        REGION_NAME
    }
}
