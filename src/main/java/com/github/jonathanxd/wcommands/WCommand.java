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

import com.github.jonathanxd.iutils.annotations.Immutable;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.commandstring.CommandStringParser;
import com.github.jonathanxd.wcommands.commandstring.CommonCommandStringParser;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.InvokeInterceptor;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.result.Results;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 26/02/16.
 */
public class WCommand<T> {

    public static final InfoId WCOMMAND_INFOID = new InfoId(new String[]{"Manager", "WCommand"}, WCommand.class);

    private final Processor<T> processor;
    private final ErrorHandler<T> errorHandler;
    private final Interceptors interceptors = new Interceptors();
    private final CommandStringParser commandStringParser;


    private final CommandList commands = new CommandList();

    /**
     * Create WCommand with specified Processor and ErrorHandler
     *
     * @param processor    Processor
     * @param errorHandler ErrorHandler
     * @param commandStringParser Command String parser
     * @see Processor
     * @see ErrorHandler
     */
    public WCommand(Processor<T> processor, ErrorHandler<T> errorHandler, CommandStringParser commandStringParser) {
        this.processor = processor;
        this.errorHandler = errorHandler;
        this.commandStringParser = commandStringParser;
    }

    /**
     * Create WCommand with specified Processor and ErrorHandler
     *
     * @param processor    Processor
     * @param errorHandler ErrorHandler
     * @see Processor
     * @see ErrorHandler
     * @see #WCommand(Processor, ErrorHandler, CommandStringParser)
     */
    public WCommand(Processor<T> processor, ErrorHandler<T> errorHandler) {
        this.processor = processor;
        this.errorHandler = errorHandler;
        this.commandStringParser = new CommonCommandStringParser();
    }

    /**
     * Add an interceptor
     *
     * @param invokeInterceptor The Interceptor
     * @see InvokeInterceptor
     */
    public void addInterceptor(InvokeInterceptor invokeInterceptor) {
        interceptors.add(invokeInterceptor);
    }

    /**
     * Remove an interceptor
     *
     * @param invokeInterceptor Interceptor
     * @see InvokeInterceptor
     */
    public void removeInterceptor(InvokeInterceptor invokeInterceptor) {
        interceptors.remove(invokeInterceptor);
    }

    /**
     * Register command
     *
     * @param commandSpec Command Specification
     */
    public void registerCommand(CommandSpec... commandSpec) {
        if (commandSpec.length == 1) {
            commands.add(commandSpec[0]);
        } else {
            commands.addAll(Arrays.asList(commandSpec));
        }
    }

    /**
     * Register all commands from another list
     *
     * @param commands Command list
     */
    public void registerAllFrom(CommandList commands) {
        this.commands.addAll(commands);
    }

    /**
     * Process and invoke
     *
     * @param arguments Argument Array
     * @see InformationRegister
     * @see #process(List, Requirements, InformationRegister)
     * @see #invoke(Object, Requirements, InformationRegister)
     */
    public Results processAndInvoke(String... arguments) {
        return processAndInvoke(Arrays.asList(arguments), null, null);
    }

    /**
     * Process and invoke
     *
     * @param informationRegister Information register
     * @param arguments           Argument Array
     * @see InformationRegister
     * @see #process(List, Requirements, InformationRegister)
     * @see #invoke(Object, Requirements, InformationRegister)
     */
    public Results processAndInvoke(InformationRegister informationRegister, String... arguments) {
        return processAndInvoke(Arrays.asList(arguments), null, informationRegister);
    }

    /**
     * Process and invoke
     *
     * @param informationRegister Information register
     * @param arguments           Argument Array
     * @see InformationRegister
     * @see #process(List, Requirements, InformationRegister)
     * @see #invoke(Object, Requirements, InformationRegister)
     */
    public Results processAndInvoke(Requirements requirements, InformationRegister informationRegister, String... arguments) {
        return processAndInvoke(Arrays.asList(arguments), requirements, informationRegister);
    }

    /**
     * Process and invoke
     *
     * @param arguments           List of arguments
     * @param informationRegister Information register
     * @see InformationRegister
     * @see #process(List, Requirements, InformationRegister)
     * @see #invoke(Object, Requirements, InformationRegister)
     */
    public Results processAndInvoke(List<String> arguments, Requirements requirements, InformationRegister informationRegister) {
        return invoke(process(arguments, requirements, informationRegister), requirements, informationRegister);
    }

    /**
     * Process the argument list
     *
     * @param arguments Argument list
     * @return Mapped Commands
     */
    public T process(List<String> arguments, Requirements requirements, InformationRegister informationRegister) {
        try{
            return processor.process(arguments, commands, errorHandler, requirements, informationRegister);
        } catch (Throwable e) {
            if(errorHandler.handle(new ProcessingError(e, ErrorType.FAIL), this.getCommandList(), null, null, requirements, informationRegister)) {
                throw e;
            } else {
                return null;
            }
        }
    }

    /**
     * Get command by name
     *
     * @param name Command name
     * @return {@link Optional} of {@link CommandSpec} if find, or {@link Optional#empty()}
     */
    public Optional<CommandSpec> getCommand(String name) {
        for (CommandSpec spec : commands) {
            if (spec.matches(name)) {
                return Optional.of(spec);
            }
        }
        return Optional.empty();
    }

    /**
     * Get command by path
     *
     * @param path Path, first is main command and others is the sub commands.
     * @return {@link Optional} of {@link CommandSpec} if find, or {@link Optional#empty()}
     */
    public Optional<CommandSpec> getCommand(String[] path) {

        List<CommandSpec> commandSpecs = commands;

        Iterator<CommandSpec> commandSpecIterator = commandSpecs.iterator();

        int pathIndex = 0;

        while (commandSpecIterator.hasNext()) {

            CommandSpec spec = commandSpecIterator.next();

            if (spec.matches(path[pathIndex])) {

                if (pathIndex + 1 >= path.length)
                    return Optional.of(spec);

                ++pathIndex;

                commandSpecIterator = spec.getSubCommands().iterator();
            }
        }

        return Optional.empty();
    }

    /**
     * Invoke mapped commands
     *
     * @param object              Mapped Commands
     * @param informationRegister InformationRegister
     */
    public Results invoke(T object, Requirements requirements, InformationRegister informationRegister) {
        try{
            return processor.invokeCommands(object, interceptors, requirements, informationRegister);
        } catch (Throwable e) {
            if(errorHandler.handle(new ProcessingError(e, ErrorType.FAIL), this.getCommandList(), null, null, requirements, informationRegister)) {
                throw e;
            } else {
                return null;
            }
        }

    }

    /**
     * Create a handler.
     *
     * @param handler Handler
     * @return {@code handler}
     */
    public Handler<T> createHandler(Handler<T> handler) {
        return handler;
    }

    /**
     * Command processor
     *
     * @return Command Processor
     * @see Processor
     */
    public Processor<T> getProcessor() {
        return processor;
    }

    /**
     * Get immutable CommandList.
     *
     * @return Immutable CommandList.
     */
    @Immutable
    public CommandList getCommandList() {
        return commands.toUnmodifiable();
    }

    /**
     * Get mutable command list
     *
     * @return Mutable command list
     */
    protected CommandList getCommands() {
        return commands;
    }

    /**
     * Get Error Handler
     *
     * @return Error Handler
     */
    protected ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Get Invoke Interceptors
     *
     * @return Invoke Interceptors
     */
    protected Interceptors getInterceptors() {
        return interceptors;
    }
}
