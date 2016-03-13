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
package com.github.jonathanxd.wcommands.ext.help;

import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.Extension;
import com.github.jonathanxd.wcommands.ext.help.printer.Printer;

/**
 * Created by jonathan on 12/03/16.
 */
public class HelperAPI extends Extension {

    public static void help(WCommand<?> command, Printer printer) {
        help(command.getCommandList(), printer);
    }

    public static void help(CommandList commandSpecs, Printer printer) {
        printer.printCommands(commandSpecs);
    }

    public static void help(CommandSpec command, Printer printer) {
        printer.printCommands(CommandList.singleton(command));
    }
}