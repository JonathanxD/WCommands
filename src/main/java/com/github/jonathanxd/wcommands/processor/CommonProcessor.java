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
package com.github.jonathanxd.wcommands.processor;

import com.github.jonathanxd.iutils.container.ImmutableContainer;
import com.github.jonathanxd.iutils.container.MutableContainer;
import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.MissingArgumentException;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.Phase;
import com.github.jonathanxd.wcommands.result.IResult;
import com.github.jonathanxd.wcommands.result.Result;
import com.github.jonathanxd.wcommands.result.Results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * TODO: Rewrite
 */
public class CommonProcessor implements Processor<List<CommandData<CommandHolder>>> {

    @Deprecated
    public static WCommand<List<CommandData<CommandHolder>>> newWCommand(ErrorHandler<List<CommandData<CommandHolder>>> handler) {
        return new WCommand<>(new CommonProcessor(), handler);
    }

    @Override
    public List<CommandData<CommandHolder>> process(List<String> arguments, CommandList commands, ErrorHandler<List<CommandData<CommandHolder>>> handler, Requirements requirements, InformationRegister informationRegister) {

        if (arguments.size() == 0) {
            handler.handle(new ProcessingError("No commands provided", ErrorType.NO_COMMAND_PROVIDED), commands.toUnmodifiable(), null, null, requirements, informationRegister);

            return Collections.emptyList();
        }

        Processing processing = new Processing(new ErrorHandler.Container<>(handler));
        try {
            processing.process(arguments.listIterator(), commands.toUnmodifiable(), null, requirements, informationRegister);
        } catch (ProcessingError processingError) {
            handler.handle(processingError, commands.toUnmodifiable(), null, processing.commandDatas, requirements, informationRegister);
        }

        return processing.commandDatas;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Results invokeCommands(List<CommandData<CommandHolder>> object, Interceptors interceptors, Requirements requirements, InformationRegister informationRegister) {

        object = new ArrayList<>(object);

        object.sort((o1, o2) -> {
            CommandHolder o1Command = o1.getCommand();
            CommandHolder o2Command = o2.getCommand();

            if(o1Command.isMain() && o2Command.isMain() || o1.getParent().getCommandSpec() == o2.getParent().getCommandSpec()) {
                int sort = Integer.compare(o1Command.getCommandSpec().getPriority(), o2Command.getCommandSpec().getPriority());

                if(sort == 0)
                    return 1;

                return sort;
            }

            return 1;
        });


        Results results = new Results();

        Interceptors phasePreCall = interceptors.getPhase(Phase.PRE_CALL);
        Interceptors phasePostCall = interceptors.getPhase(Phase.POST_CALL);

        if (informationRegister == null)
            informationRegister = new InformationRegister();

        InfoId resultsInfoId = new InfoId("Result", Results.class);

        Optional<Object> optional = informationRegister.getOptional(resultsInfoId);

        if (!optional.isPresent() || !(optional.get() instanceof Results)) {
            informationRegister.register(resultsInfoId, results, "Command call results", TypeInfo.of(Results.class));
        }


        for (CommandData<CommandHolder> data : object) {

            CommandHolder holder = data.getCommand();
            if (holder.isLastMatching()) {

                Object result = null;

                Handler<CommandHolder> handler = holder.getCommandSpec().getDefaultHandler();


                MutableContainer<Handler<CommandHolder>> handlerContainer = new MutableContainer<>(handler);

                // PRE CALL MODIFICATIONS
                phasePreCall.forEach(interceptor -> interceptor.intercept(data, handlerContainer));

                if (handlerContainer.get() != null) {
                    result = handlerContainer.get().handle(data, requirements, informationRegister);
                }

                ImmutableContainer<Handler<CommandHolder>> staticContainer = ImmutableContainer.of(handlerContainer.get());
                // POST CALL MONITORING
                phasePostCall.forEach(interceptor -> interceptor.intercept(data, staticContainer));

                if (result instanceof IResult<?>) {
                    IResult<?> iResult = (IResult<?>) result;

                    iResult = Result.fill(iResult, null, handler, data, result);

                    results.add(iResult);
                } else {
                    results.add(IResult.create(null, handler, data, result));
                }
            }
        }

        return results;
    }

    public boolean findLastSet(List<CommandData<CommandHolder>> list) {
        if (list.isEmpty())
            return false;

        CommandData<CommandHolder> last = list.get(list.size() - 1);

        return last.getCommand().isLastMatching();
    }

    public boolean findAny(List<CommandData<CommandHolder>> list, List<CommandSpec> commandSpecs) {
        if (list.isEmpty())
            return false;

        for (CommandData<CommandHolder> c : list) {
            CommandHolder commandHolder = c.getCommand();
            if (commandSpecs.contains(commandHolder.getCommandSpec())) {
                return true;
            }
        }

        return false;
    }

    public interface CommonHandler extends Handler<CommandHolder> {
    }

    private class Processing {
        private final ErrorHandler.Container<List<CommandData<CommandHolder>>> handlerContainer;
        private final List<CommandData<CommandHolder>> commandDatas = new ArrayList<>();
        private List<CommandSpec> main = null;

        private Processing(ErrorHandler.Container<List<CommandData<CommandHolder>>> handlerContainer) {
            this.handlerContainer = handlerContainer;
        }

        public void process(ListIterator<String> argIter, CommandList commandSpecs, CommandHolder parent, Requirements requirements, InformationRegister informationRegister) throws ProcessingError {
            if (main == null) {
                main = commandSpecs;
            }
            if (!argIter.hasNext()) {
                handlerContainer.handle(new ProcessingError("No more elements!", ErrorType.POSSIBLE_BUG), commandSpecs, (parent != null ? parent.getCommandSpec() : null), commandDatas, requirements, informationRegister);
                return;
            }

            //Iterator<CommandSpec> commandSpecIterator = commandSpecs.iterator();
            Iterator<CommandSpec> commandSpecIterator = commandSpecs.iterator();

            String next = null;

            while (argIter.hasNext()) {

                String argument = argIter.next();

                if (argIter.hasNext()) {
                    next = argIter.next();
                    argIter.previous();
                } else {
                    next = null;
                }

                boolean matches = false;
                while (commandSpecIterator.hasNext()) {
                    CommandSpec commandSpec = commandSpecIterator.next();

                    boolean last = !argIter.hasNext();

                    if (commandSpec.matches(argument)) {

                        ArgumentsHolder argumentHolders = parseArguments(argIter, commandSpec, parent != null ? parent.getCommandSpec() : null, commandSpecs, requirements, informationRegister);

                        CommandHolder holder = new CommandHolder(commandSpec, parent, argumentHolders, last);

                        commandDatas.add(new CommandData<>(argument, holder, parent));

                        int size = commandDatas.size();

                        if (!commandSpec.getSubCommands().isEmpty()) {
                            if (argIter.hasNext())
                                process(argIter, commandSpec.getSubCommands(), holder, requirements, informationRegister);
                        }

                        if (size == commandDatas.size()) {
                            commandDatas.remove(commandDatas.size() - 1);
                            commandDatas.add(new CommandData<>(argument, new CommandHolder(commandSpec, parent, argumentHolders, true), parent));
                        }

                        commandSpecIterator = commandSpecs.iterator();
                        matches = true;
                    } else {
                        if (commandSpecIterator.hasNext() || parent == null) {
                            continue;
                        } else {
                            argIter.previous();
                            return;
                        }
                        //continue;
                    }

                    ////////////////////////////////
                    if (last) {
                        List<CommandSpec> notOptional = commandSpec.getNotOptionalSubCommands();
                        if (notOptional.size() > 0) {
                            if (!findAny(commandDatas, notOptional)) {
                                handlerContainer.handle(new ProcessingError("Missing sub-commandSpec for commandSpec '" + commandSpec + "'", ErrorType.MISSING_SUB_COMMAND), commandSpecs, commandSpec, commandDatas, requirements, informationRegister);
                            }
                        }
                    }

                    break;

                }

                if (!matches && parent == null) {
                    handlerContainer.handle(new ProcessingError("Cannot find command '" + argument + "'", ErrorType.MISSING_COMMAND), commandSpecs, null, commandDatas, requirements, informationRegister);
                }
            }
        }

        private ArgumentsHolder parseArguments(ListIterator<String> argumentIter, CommandSpec commandSpec, CommandSpec parent, CommandList commandSpecs, Requirements requirements, InformationRegister informationRegister) throws ProcessingError {
            ArgumentsHolder argumentHolders = new ArgumentsHolder();

            // ArgumentSpec parsing
            CommandList parentSubCommands = parent != null ? parent.getSubCommands() : new CommandList(this);

            // Check if any sub command matches this name, if matches, will return the 'empty arguments holder'.

            if (argumentIter.hasNext()) {
                String name = argumentIter.next();
                argumentIter.previous();

                CommandList subCommands = commandSpec.getSubCommands();


                if (!subCommands.isEmpty()) {
                    for (CommandSpec subSpec : subCommands) {
                        if (subSpec.matches(name)) {
                            return argumentHolders;
                        }
                    }
                }
            }

            Iterator<ArgumentSpec<?, ?>> iterator = commandSpec.getArguments().iterator();

            List<String> matchedList = new ArrayList<>();

            boolean next = true;

            ArgumentSpec<?, ?> argumentSpecParse = null;

            while (iterator.hasNext() || !next) {

                if (next /*|| argumentSpecParse == null*/)
                    argumentSpecParse = iterator.next();

                if (!argumentIter.hasNext()) {
                    ArgumentHolder argument = new ArgumentHolder<>(matchedList, argumentSpecParse);
                    argumentHolders.add(argument);

                    next = true;
                    matchedList.clear();
                }

                while (argumentIter.hasNext()) {

                    String sub = argumentIter.next();
                    if (commandSpecs.getAnyMatching(sub) != null) {

                        argumentIter.previous();

                        ArgumentHolder argument = new ArgumentHolder<>(null, argumentSpecParse);
                        argumentHolders.add(argument);

                        break;
                    }

                    String nextArg = null;

                    if (argumentIter.hasNext()) {
                        nextArg = argumentIter.next();
                        argumentIter.previous();
                    }

                    matchedList.add(sub);

                    Boolean anyMatches = null;

                    if (argumentSpecParse.getChecker() != null) {
                        anyMatches = argumentSpecParse.getChecker().get().matches(sub);
                    }

                    if (argumentSpecParse.getPredicate() != null) {
                        boolean result = argumentSpecParse.getPredicate().test(matchedList);

                        if (anyMatches == null)
                            anyMatches = result;
                        else
                            anyMatches = anyMatches && result;
                    }


                    if (anyMatches == null && argumentSpecParse.getConverter() != null) {
                        boolean result = false;

                        try {
                            result = argumentSpecParse.getConverter().apply(matchedList) != null;
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        anyMatches = result;
                    }

                    if ((anyMatches == null || !anyMatches) && !matchedList.isEmpty()) {
                        matchedList.remove(matchedList.size() - 1);
                    }

                    if (anyMatches != null && anyMatches) {

                        if (argumentSpecParse.isArray() && argumentSpecParse.isArray() && (nextArg == null || commandSpecs.getAnyMatching(nextArg) == null)) {
                            next = false;
                        } else {
                            ArgumentHolder argument = new ArgumentHolder<>(matchedList, argumentSpecParse);
                            argumentHolders.add(argument);
                            matchedList.clear();
                            next = true;
                        }

                        break;
                    } else {
                        if (argumentSpecParse.isOptional() || (argumentSpecParse.isArray() && !matchedList.isEmpty())) {
                            argumentIter.previous();
                            ArgumentHolder argument = new ArgumentHolder<>(matchedList, argumentSpecParse);
                            argumentHolders.add(argument);

                            matchedList.clear();
                            next = true;

                            break;
                        } else {
                            handlerContainer.handle(
                                    new ProcessingError(
                                            new IllegalArgumentException(String.format("Illegal argument '%s' for parser '%s'", sub, argumentSpecParse)),
                                            ErrorType.MISSING_ARGUMENT), commandSpecs, commandSpec, commandDatas, requirements, informationRegister);

                        }

                    }

                }

            }

            int pos = 0;
            for (ArgumentSpec argumentSpec : commandSpec.getArguments()) {
                if (argumentHolders.size() == 0) {
                    if (!argumentSpec.isOptional()) {
                        handlerContainer.handle(
                                new ProcessingError(
                                        new MissingArgumentException(String.format("Missing argumentSpec (id: '%s', %s). Probably is missing all arguments required...", argumentSpec.getId(), argumentSpec)),
                                        ErrorType.MISSING_ARGUMENT), commandSpecs, commandSpec, commandDatas, requirements, informationRegister);
                    } else {
                        continue;
                    }
                }
                while (pos < argumentHolders.size()) {
                    ArgumentHolder holder = argumentHolders.get(pos);

                    if (argumentSpec != holder.getArgumentSpec() || !holder.isPresent()) {
                        if (!argumentSpec.isOptional()) {

                            handlerContainer.handle(
                                    new ProcessingError(
                                            new MissingArgumentException(String.format("Missing argumentSpec (id: '%s', %s)", argumentSpec.getId(), argumentSpec)),
                                            ErrorType.MISSING_ARGUMENT), commandSpecs, commandSpec, commandDatas, requirements, informationRegister);
                        } else {
                            ++pos;
                            continue;
                        }
                    } else {
                        ++pos;
                        break;
                    }


                }
            }

            return argumentHolders;
        }
    }


}
