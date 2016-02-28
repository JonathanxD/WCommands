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

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.InvokeInterceptor;
import com.github.jonathanxd.wcommands.processor.Processor;

import java.util.Arrays;
import java.util.List;

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

    public void addCommand(CommandSpec... commandSpec) {
        if (commandSpec.length == 1) {
            commands.add(commandSpec[0]);
        } else {
            commands.addAll(Arrays.asList(commandSpec));
        }
    }

    public void processAndInvoke(String... arguments) throws ArgumentProcessingError {
        processAndInvoke(Arrays.asList(arguments));
    }

    public void processAndInvoke(List<String> arguments) throws ArgumentProcessingError {
        processor.invokeCommands(processor.process(arguments, commands, errorHandler), interceptors);
    }

    public Handler<T> createHandler(Handler<T> handler) {
        return handler;
    }

    public Processor<T> getProcessor() {
        return processor;
    }
}
