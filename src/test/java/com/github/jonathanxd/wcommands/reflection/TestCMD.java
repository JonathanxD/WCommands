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
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;

import org.junit.Test;

public class TestCMD {

    @Test
    public void testCommand() throws ProcessingError {

        TestCMD testAnnotations = new TestCMD();

        ReflectionCommandProcessor commandCommon = ReflectionAPI.createWCommand(testAnnotations);

        commandCommon.processAndInvoke("give", "xp", "14");

    }

    @Command
    public void give(@Argument GiveType giveType,
                     @Argument(isOptional = true) Items what,
                     @Argument(id = "INT") int amount) {

        if (giveType == GiveType.ITEM) {
            System.out.println("Giving item name: '" + what + "', amount: " + amount);
        } else if (giveType == GiveType.XP) {
            System.out.println("Giving xp, amount: " + amount);
        }


    }

    public enum GiveType {
        ITEM,
        XP
    }

    public enum Items {
        DIAMOND,
        GOLD,
        IRON
    }

    public static class MyErrorHandler implements ErrorHandler {

        @Override
        public ProcessAction handle(ProcessingError error, CommandList commandSpecs, CommandSpec current, Object processed, Requirements requirements, InformationRegister informationRegister) {
            return (error.getType().getExceptionType() != ErrorType.Type.ERROR) ? ProcessAction.CONTINUE : ProcessAction.STOP;
        }
    }
}
