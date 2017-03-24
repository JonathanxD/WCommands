/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands;

import com.github.jonathanxd.iutils.annotation.Immutable;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.commandstring.CommandStringParser;
import com.github.jonathanxd.wcommands.commandstring.CommonCommandStringParser;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.handler.CommandRegistrationListener;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.InvokeInterceptor;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.result.Results;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WCommand<T> {

    public static final InfoId WCOMMAND_INFOID = new InfoId(new String[]{"Manager", "WCommand"}, WCommand.class);
    protected final CommandList commands;
    private final Processor<T> processor;
    private final ErrorHandler<T> errorHandler;
    private final Interceptors interceptors = new Interceptors();
    private final CommandStringParser commandStringParser;
    private final Set<CommandRegistrationListener> commandRegistrationListeners = new HashSet<>();

    /**
     * Create WCommand with specified Processor and ErrorHandler
     *
     * @param processor           Processor
     * @param errorHandler        ErrorHandler
     * @param commandStringParser Command String parser
     * @see Processor
     * @see ErrorHandler
     */
    public WCommand(Processor<T> processor, ErrorHandler<T> errorHandler, CommandStringParser commandStringParser) {
        this.processor = processor;
        this.errorHandler = errorHandler;
        this.commandStringParser = commandStringParser;
        this.commands = new CommandList(this, this);
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
        this.commands = new CommandList(this, this);
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
     * Unregister command specification
     *
     * @param commandSpec Command Specification
     * @return True if element is removed, false otherwise.
     */
    public boolean unregisterCommand(CommandSpec commandSpec) {
        return commands.remove(commandSpec);
    }

    /**
     * Register command registration handler
     *
     * @param commandRegistrationListener Handler
     * @see CommandRegistrationListener
     */
    public void registerRegistrationHandler(CommandRegistrationListener commandRegistrationListener) {
        commandRegistrationListeners.add(commandRegistrationListener);
    }

    /**
     * Unregister command registration handler
     *
     * @param commandRegistrationListener Handler
     * @return False if element is not present in list, or true if element is removed
     * @see CommandRegistrationListener
     */
    public boolean unregisterRegistrationHandler(CommandRegistrationListener commandRegistrationListener) {
        return commandRegistrationListeners.remove(commandRegistrationListener);
    }

    /**
     * Process and invoke
     *
     * @param argumentToBeParsed Argument to parse
     * @see InformationRegister
     * @see #process(List, Requirements, InformationRegister)
     * @see #invoke(Object, Requirements, InformationRegister)
     */
    public Results processAndInvoke(String argumentToBeParsed) {
        return processAndInvoke(this.commandStringParser.parseSingle(argumentToBeParsed), null, null);
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
        return processAndInvoke(this.commandStringParser.parse(arguments), null, null);
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
        return processAndInvoke(this.commandStringParser.parse(arguments), null, informationRegister);
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
        return processAndInvoke(this.commandStringParser.parse(arguments), requirements, informationRegister);
    }

    /**
     * Process and invoke
     *
     * @param parsedArguments     List of parsedArguments
     * @param informationRegister Information register
     * @see InformationRegister
     * @see #process(List, Requirements, InformationRegister)
     * @see #invoke(Object, Requirements, InformationRegister)
     */
    public Results processAndInvoke(List<String> parsedArguments, Requirements requirements, InformationRegister informationRegister) {
        return invoke(process(parsedArguments, requirements, informationRegister), requirements, informationRegister);
    }

    /**
     * Process the argument list
     *
     * @param arguments Argument list
     * @return Mapped Commands
     */
    public T process(List<String> arguments, Requirements requirements, InformationRegister informationRegister) {
        try {
            return processor.process(arguments, commands, errorHandler, requirements, informationRegister);
        } catch (Throwable e) {
            if (errorHandler.handle(new ProcessingError(e, ErrorType.FAIL), this.getCommandList(), null, null, requirements, informationRegister) == ProcessAction.STOP) {
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

        return commands.getCommand(name);
    }

    /**
     * Get command by path
     *
     * @param path Path, first is main command and others is the sub commands.
     * @return {@link Optional} of {@link CommandSpec} if find, or {@link Optional#empty()}
     */
    public Optional<CommandSpec> getCommand(String[] path) {
        return commands.getCommand(path);
    }

    /**
     * Invoke mapped commands
     *
     * @param object              Mapped Commands
     * @param informationRegister InformationRegister
     */
    public Results invoke(T object, Requirements requirements, InformationRegister informationRegister) {
        try {
            return processor.invokeCommands(object, interceptors, requirements, informationRegister);
        } catch (Throwable e) {
            if (errorHandler.handle(new ProcessingError(e, ErrorType.FAIL), this.getCommandList(), null, null, requirements, informationRegister) == ProcessAction.STOP) {
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

    @Immutable
    public Set<CommandRegistrationListener> getCommandRegistrationListeners() {
        return Collections.unmodifiableSet(commandRegistrationListeners);
    }

    public <V> Register<V> getRegister(RegistrationTicket<V> registrationTicket) {
        return new Register<>(this, registrationTicket);
    }

    /**
     * Handle command registration to ticket
     *
     * @param commandSpec CommandSpec
     * @param targetList  Target list
     * @param ticket      Ticket to receive commands
     */
    @SuppressWarnings("Duplicates")
    public Optional<CommandSpec> handleRegistrationToTicket(CommandSpec commandSpec, CommandList targetList, RegistrationTicket<?> ticket) {

        if (!ticket.isOpenRegistration()) {
            return Optional.of(commandSpec);
        }

        targetList = targetList.toUnmodifiable();

        List<RegistrationHandleResult> results = new ArrayList<>();

        RegistrationHandleResult main = RegistrationHandleResult.newInstance(commandSpec, null, RegistrationHandleResult.Action.ACCEPT);

        for (CommandRegistrationListener handler : ticket.getListeners()) {

            RegistrationHandleResult result;

            if (results.isEmpty()) {
                result = handler.handle(Collections.singletonList(main), targetList, this, ticket);
            } else {
                result = handler.handle(Collections.unmodifiableList(results), targetList, this, ticket);
            }

            if (result != null) {
                result = RegistrationHandleResult.applyTo(commandSpec, result);
                results.add(result);
                main = null;
            } else {
                if (results.isEmpty() && main != null) {
                    results.add(main);
                    main = null;
                }
            }
        }

        if (results.isEmpty()) {
            return Optional.of(commandSpec);
        }

        RegistrationHandleResult result = results.get(0);

        switch (result.getAction()) {
            case ACCEPT: {
                return Optional.of(commandSpec);
            }
            case CANCEL: {
                return Optional.empty();
            }
            case MODIFY: {

                if (!result.getModifiedCommandSpec().isPresent()) {
                    throw new RuntimeException("RegistrationHandleResult with Action 'MODIFY' has empty modified command spec");
                }

                return Optional.of(result.getModifiedCommandSpec().get());
            }
        }

        return Optional.of(commandSpec);
    }


}
