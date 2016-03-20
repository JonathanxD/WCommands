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
package com.github.jonathanxd.wcommands.reflection.intype;

import com.github.jonathanxd.wcommands.ext.help.HelperAPI;
import com.github.jonathanxd.wcommands.ext.help.printer.CommonPrinter;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.util.reflection.Creator;

import org.junit.Test;

/**
 * Created by jonathan on 18/03/16.
 */
public class TestSuperCommand {

    @Test
    public void testSuperCommand() {

        //SuperCommand commandFurious = new SuperCommand();

        ReflectionCommandProcessor commandProcessor = ReflectionAPI.createWCommand(/*commandFurious*/);

        //commandProcessor.addCommands(new SuperCommand.CommandCTRL());
        commandProcessor.addCommandFromClass(SuperCommand.class, Creator::createEmpty);

        HelperAPI.help(commandProcessor.getCommandList(), null, CommonPrinter.TO_SYS_OUT);
    }

}
