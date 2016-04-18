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
package com.github.jonathanxd.wcommands.ext.reflect;

/**
 * Created by jonathan on 28/02/16.
 */

import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.Extension;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import java.util.List;

/**
 * Reflection API is explained in GitHub
 *
 * Reflection fetches Annotations and Create Commands and Arguments
 *
 * @see Extension
 */
public class ReflectionAPI extends Extension {

    public static ReflectionCommandProcessor createWCommand() {
        return createWCommand((Object) null, (Class<?>) null);
    }

    public static ReflectionCommandProcessor createWCommand(Object instance) {
        return createWCommand(instance, instance.getClass());
    }

    public static ReflectionCommandProcessor createWCommand(Object instance, Class<?> commandClass) {
        return createWCommand(instance, commandClass, RegistrationTicket.empty(instance));
    }

    public static ReflectionCommandProcessor createWCommand(Object instance, Class<?> commandClass, RegistrationTicket<?> ticket) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor();

        if (instance != null)
            commandProcessor.getRegister(ticket).addAsFuture(instance, commandClass);

        return commandProcessor;
    }


    public static ReflectionCommandProcessor createWCommand(ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance) {
       return createWCommand(handler, instance, RegistrationTicket.empty(instance));
    }

    public static ReflectionCommandProcessor createWCommand(ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance, RegistrationTicket<?> ticket) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(handler);

        if (instance != null)
            commandProcessor.getRegister(ticket).addAsFuture(instance, instance.getClass());

        return commandProcessor;
    }

    public static ReflectionCommandProcessor createWCommand(ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance, Class<?> commandClass) {
       return createWCommand(handler, instance, commandClass, RegistrationTicket.empty(instance));
    }

    public static ReflectionCommandProcessor createWCommand(ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance, Class<?> commandClass, RegistrationTicket<?> ticket) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(handler);

        if (instance != null)
            commandProcessor.getRegister(ticket).addAsFuture(instance, commandClass);

        return commandProcessor;
    }

    public static ReflectionCommandProcessor createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance) {
        return createWCommand(processor, handler, instance, RegistrationTicket.empty(instance));
    }

    public static ReflectionCommandProcessor createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance, RegistrationTicket<?> ticket) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(processor, handler);

        if (instance != null)
            commandProcessor.getRegister(ticket).addAsFuture(instance, instance.getClass());

        return commandProcessor;
    }


    public static ReflectionCommandProcessor createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance, Class<?> commandClass) {
        return createWCommand(processor, handler, instance, commandClass, RegistrationTicket.empty(instance));
    }
    public static ReflectionCommandProcessor createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler, Object instance, Class<?> commandClass, RegistrationTicket<?> ticket    ) {
        ReflectionCommandProcessor commandProcessor = new ReflectionCommandProcessor(processor, handler);

        if (instance != null)
            commandProcessor.getRegister(ticket).addAsFuture(instance, commandClass);

        return commandProcessor;
    }

    /***************** NEW METHODS *****************/

    public static ReflectionCommandProcessor createWCommand(ErrorHandler<List<CommandData<CommandHolder>>> handler) {
        return new ReflectionCommandProcessor(handler);
    }

    public static ReflectionCommandProcessor createWCommand(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler) {
        return new ReflectionCommandProcessor(processor, handler);
    }

}
