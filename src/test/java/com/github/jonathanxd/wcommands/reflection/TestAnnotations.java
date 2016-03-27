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
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Assert;

import org.junit.Test;

public class TestAnnotations {

    // --allowUpper false --daemon --rail true

    @Test
    public void testAnnotations() throws ProcessingError {

        ReflectionCommandProcessor commandCommon = new ReflectionCommandProcessor(new CommonProcessor(), new MyErrorHandler());

        TestAnnotations testAnnotations = new TestAnnotations();

        commandCommon.getRegister(new CommonTicket<>(this)).addCommands(testAnnotations, TestAnnotations.class);

        commandCommon.processAndInvoke("--allowUpper", "true", "--daemon", "--rail", "false");

        System.out.println("Allow Upper? "+testAnnotations.allowUpper);
        System.out.println("Is Daemon? "+testAnnotations.daemon);
        System.out.println("Rail? "+testAnnotations.rail);

        Assert.assertEquals(testAnnotations.allowUpper, true);
        Assert.assertEquals(testAnnotations.daemon, true);
        Assert.assertEquals(testAnnotations.rail, false);
    }

    @Command(prefix = "--")
    @Argument
    private boolean allowUpper = false;

    @Command(prefix = "--")
    @Argument(isOptional = true)
    private boolean daemon = false;

    @Command(prefix = "--")
    @Argument
    public boolean rail = true;

    public static class MyErrorHandler implements ErrorHandler {

        @Override
        public ProcessAction handle(ProcessingError error, CommandList commandSpecs, CommandSpec current, Object processed, Requirements requirements, InformationRegister informationRegister) {
            error.printStackTrace();
            return (error.getType().getExceptionType() != ErrorType.Type.ERROR) ? ProcessAction.CONTINUE : ProcessAction.STOP;
        }
    }
}
