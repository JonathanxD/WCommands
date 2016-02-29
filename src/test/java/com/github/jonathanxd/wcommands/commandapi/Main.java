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
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws ArgumentProcessingError {
        WCommand<List<CommandData<CommandHolder>>> wCommand = CommonProcessor.newWCommand((e) -> true);

        CommonProcessor.CommonHandler handler = data -> {
            System.out.println("|CommandSpec|: " + data.getCommand());
            data.getCommand().eachArguments().plain(arg -> {
                System.out.println("ArgumentSpec: " + arg);
            });
            CommandHolder command = data.getCommand();

            Optional<String> xPArgOpt = command.getPlainArgument(ArgumentIDs.XP_ARGUMENT);
            xPArgOpt.ifPresent(System.out::println);

            if(data.getParent() != null) {
                System.out.println("PARENT: " + data.getParent().getPlainArgument(ArgumentIDs.REGION_NAME));
            }
        };

        ArgumentSpec argumentSpec = CommandFactory.create(ArgumentIDs.ITEM_ARGUMENT, () -> Text.of("(STONE)|(GOLD)|(AXE)", true, true));
        ArgumentSpec argumentSpecXP = CommandFactory.create(ArgumentIDs.XP_ARGUMENT, Matchable::acceptAny);
        ArgumentSpec argumentSpecName = CommandFactory.create(ArgumentIDs.REGION_NAME, Matchable::acceptAny);
        ArgumentSpec argumentSpecRegionName = CommandFactory.createOptional(ArgumentIDs.REGION_NAME, () -> s -> !s.equals("allow"));

        wCommand.registerCommand(
                CommandFactory.create("give", handler)
                        .addSub(CommandFactory.create("xp", Arguments.of(argumentSpecXP), handler))
                        .addSub(CommandFactory.create("item", Arguments.of(argumentSpec), handler))
        );

        wCommand.registerCommand(
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
