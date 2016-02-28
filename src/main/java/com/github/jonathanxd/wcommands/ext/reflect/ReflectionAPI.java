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
package com.github.jonathanxd.wcommands.ext.reflect;

/**
 * Created by jonathan on 28/02/16.
 */

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.Extension;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.processor.Processor;

import java.util.List;

/**
 * Reflection API is explained in GitHub
 *
 * Reflection fetches Annotations and Create Commands and Arguments
 *
 * @see Extension
 */
public class ReflectionAPI extends Extension {

    public static WCommandCommon createWCommand(Object instance) {
        return createWCommand(instance, instance.getClass());
    }


    public static WCommandCommon createWCommand(Object instance, Class<?> commandClass) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor();
        commandProcessor.addCommands(instance, commandClass);
        return commandProcessor;
    }

    public static WCommandCommon createWCommand(ErrorHandler handler, Object instance) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(handler);
        commandProcessor.addCommands(instance, instance.getClass());
        return commandProcessor;
    }

    public static WCommandCommon createWCommand(ErrorHandler handler, Object instance, Class<?> commandClass) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(handler);
        commandProcessor.addCommands(instance, commandClass);
        return commandProcessor;
    }

    public static WCommandCommon createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler handler, Object instance) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(processor, handler);
        commandProcessor.addCommands(instance, instance.getClass());
        return commandProcessor;
    }

    public static WCommandCommon createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler handler, Object instance, Class<?> commandClass) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(processor, handler);
        commandProcessor.addCommands(instance, commandClass);
        return commandProcessor;
    }

}
