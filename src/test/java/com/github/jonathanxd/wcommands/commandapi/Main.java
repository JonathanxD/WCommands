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

import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.arguments.Argument;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.Command;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws ArgumentProcessingError {
        WCommand<List<CommandData<CommandHolder>>> wCommand = CommonProcessor.newWCommand((e) -> true);

        CommonProcessor.CommonHandler handler = data -> {
            System.out.println("|Command|: " + data.getCommand());
            data.getCommand().eachArguments().plain(arg -> {
                System.out.println("Argument: " + arg);
            });
            CommandHolder command = data.getCommand();

            Optional<Text> xPArgOpt = command.getPlainArgument(ArgumentIDs.XP_ARGUMENT);
            xPArgOpt.ifPresent(System.out::println);

            if(data.getParent() != null) {
                System.out.println("PARENT: " + data.getParent().getPlainArgument(ArgumentIDs.REGION_NAME));
            }
        };

        Argument argument = CommandFactory.create(ArgumentIDs.ITEM_ARGUMENT, () -> Text.of("(STONE)|(GOLD)|(AXE)", true, true));
        Argument argumentXP = CommandFactory.create(ArgumentIDs.XP_ARGUMENT, Matchable::acceptAny);
        Argument argumentName = CommandFactory.create(ArgumentIDs.REGION_NAME, Matchable::acceptAny);
        Argument argumentRegionName = CommandFactory.createOptional(ArgumentIDs.REGION_NAME, () -> s -> !s.equals("allow"));

        wCommand.addCommand(
                CommandFactory.create("give", handler)
                        .addSub(CommandFactory.create("xp", Arguments.of(argumentXP), handler))
                        .addSub(CommandFactory.create("item", Arguments.of(argument), handler))
        );

        wCommand.addCommand(
                CommandFactory.create("region", Arguments.of(argumentRegionName), handler)
                        .addSub(
                                CommandFactory.createOptional("allow", Arguments.of(argumentName), handler)
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
