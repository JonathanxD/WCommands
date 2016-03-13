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
package com.github.jonathanxd.wcommands.ext.help.error;

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ArgumentError;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.ext.help.HelperAPI;
import com.github.jonathanxd.wcommands.ext.help.printer.Printer;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;

import java.util.List;

/**
 * Created by jonathan on 12/03/16.
 */
public class HelperErrorHandler implements ErrorHandler<List<CommandData<CommandHolder>>> {
    private final Printer printer;

    public HelperErrorHandler(Printer printer) {
        this.printer = printer;
    }

    @Override
    public boolean handle(ArgumentProcessingError error, CommandList commandSpecs, CommandSpec current, List<CommandData<CommandHolder>> processed) {
        if (error.getType() == ArgumentError.MISSING_SUB_COMMAND || error.getType() == ArgumentError.MISSING_ARGUMENT) {


            if (current != null || !processed.isEmpty()) {

                printer.printString("<---> Invalid command format. <--->");
                printer.printString("<---> See usage below. <--->");

                CommandSpec commandSpec = current != null ? current : processed.get(processed.size() - 1).getCommand().getCommandSpec();

                HelperAPI.help(commandSpec, printer);
                return false;
            }
        } else if (error.getType() == ArgumentError.NO_COMMAND_PROVIDED) {
            printer.printString("<---> No Commands Provided. <--->");
            printer.printString("<---> See all commands below. <--->");
            HelperAPI.help(commandSpecs, printer);
        } else {
            throw new RuntimeException(error);
        }
        return error.getType().getExceptionType() != ArgumentError.Type.ERROR;
    }
}
