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
import com.github.jonathanxd.wcommands.exceptions.ArgumentError;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.Requirements;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;

public class TestAnnotations {

    // --allowUpper false --daemon --rail true


    public static void main(String[] args) throws ArgumentProcessingError {

        ReflectionCommandProcessor commandCommon = new ReflectionCommandProcessor(new CommonProcessor(), new MyErrorHandler());

        TestAnnotations testAnnotations = new TestAnnotations();

        commandCommon.addCommands(testAnnotations, TestAnnotations.class);

        commandCommon.processAndInvoke("--allowUpper", "true", "--daemon", "--rail", "false");

        System.out.println("Allow Upper? "+testAnnotations.allowUpper);
        System.out.println("Is Daemon? "+testAnnotations.daemon);
        System.out.println("Rail? "+testAnnotations.rail);
    }

    @Command(prefix = "--")
    @Argument
    private boolean allowUpper = false;

    @Command
    @Argument
    private boolean daemon = false;

    @Command(prefix = "--")
    @Argument
    public boolean rail = false;

    public static class MyErrorHandler implements ErrorHandler {

        @Override
        public boolean handle(ArgumentProcessingError error, CommandList commandSpecs, CommandSpec current, Object processed, Requirements requirements, InformationRegister informationRegister) {
            return error.getType().getExceptionType() != ArgumentError.Type.ERROR;
        }
    }
}
