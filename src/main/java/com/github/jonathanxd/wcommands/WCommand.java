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
package com.github.jonathanxd.wcommands;

import com.github.jonathanxd.iutils.data.ReferenceData;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.InvokeInterceptor;
import com.github.jonathanxd.wcommands.processor.Processor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 26/02/16.
 */
public class WCommand<T> {

    private final Processor<T> processor;
    private final ErrorHandler errorHandler;
    private final Interceptors interceptors = new Interceptors();

    private final CommandList commands = new CommandList();

    public WCommand(Processor<T> processor, ErrorHandler errorHandler) {
        this.processor = processor;
        this.errorHandler = errorHandler;
    }

    public void addInterceptor(InvokeInterceptor invokeInterceptor) {
        interceptors.add(invokeInterceptor);
    }

    public void removeInterceptor(InvokeInterceptor invokeInterceptor) {
        interceptors.remove(invokeInterceptor);
    }

    public void registerCommand(CommandSpec... commandSpec) {
        if (commandSpec.length == 1) {
            commands.add(commandSpec[0]);
        } else {
            commands.addAll(Arrays.asList(commandSpec));
        }
    }

    public void processAndInvoke(String... arguments) throws ArgumentProcessingError {
        processAndInvoke(Arrays.asList(arguments), null);
    }

    public void processAndInvoke(Information information, String... arguments) throws ArgumentProcessingError {
        processAndInvoke(Arrays.asList(arguments), information);
    }

    public void processAndInvoke(List<String> arguments, Information information) throws ArgumentProcessingError {
        invoke(process(arguments), information);
    }

    public T process(List<String> arguments) throws ArgumentProcessingError {
        return processor.process(arguments, commands, errorHandler);
    }

    public Optional<CommandSpec> getCommand(String name) {
        for(CommandSpec spec : commands) {
            if(spec.matches(name)) {
                return Optional.of(spec);
            }
        }
        return Optional.empty();
    }

    public Optional<CommandSpec> getCommand(String[] path) {

        List<CommandSpec> commandSpecs = commands;

        Iterator<CommandSpec> commandSpecIterator = commandSpecs.iterator();

        int pathIndex = 0;

        while(commandSpecIterator.hasNext()) {

            CommandSpec spec = commandSpecIterator.next();

            if(spec.matches(path[pathIndex])) {

                if(pathIndex + 1 >= path.length)
                    return Optional.of(spec);

                ++pathIndex;

                commandSpecIterator = spec.getSubCommands().iterator();
            }
        }

        return Optional.empty();
    }

    public void invoke(T object, Information information) {
        processor.invokeCommands(object, interceptors, information);
    }

    public Handler<T> createHandler(Handler<T> handler) {
        return handler;
    }

    public Processor<T> getProcessor() {
        return processor;
    }

    protected CommandList getCommands() {
        return commands;
    }

    protected ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected Interceptors getInterceptors() {
        return interceptors;
    }
}
