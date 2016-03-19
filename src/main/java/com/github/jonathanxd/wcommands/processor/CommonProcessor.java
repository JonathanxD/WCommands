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
package com.github.jonathanxd.wcommands.processor;

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.exceptions.MissingArgumentException;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.Phase;
import com.github.jonathanxd.wcommands.result.Result;
import com.github.jonathanxd.wcommands.result.Results;
import com.github.jonathanxd.wcommands.util.StaticContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by jonathan on 26/02/16.
 */
// List of CommandData<CommandHolder>
public class CommonProcessor implements Processor<List<CommandData<CommandHolder>>> {

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

        Results results = new Results();

        Interceptors phasePreCall = interceptors.getPhase(Phase.PRE_CALL);
        Interceptors phasePostCall = interceptors.getPhase(Phase.POST_CALL);

        for (CommandData<CommandHolder> data : object) {

            CommandHolder holder = data.getCommand();
            if (holder.isLastMatching()) {

                Object result = null;

                Handler<CommandHolder> handler = holder.getCommandSpec().getDefaultHandler();


                Container<Handler<CommandHolder>> handlerContainer = new Container<>(handler);

                // PRE CALL MODIFICATIONS
                phasePreCall.forEach(interceptor -> interceptor.intercept(data, handlerContainer));

                if (handlerContainer.get() != null) {
                    result = handlerContainer.get().handle(data, requirements, informationRegister);
                }


                StaticContainer<Handler<CommandHolder>> staticContainer = new StaticContainer<>(handlerContainer);
                // POST CALL MONITORING
                phasePostCall.forEach(interceptor -> interceptor.intercept(data, staticContainer));

                results.add(new Result(handler, result));
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
        private Set<CommandSpec> processed = new HashSet<>();

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

            while (argIter.hasNext()) {

                //int index = argIter.nextIndex();

                String argument = argIter.next();

                if (commandSpecs.size() == 0) {
                    // TODO revision, see 'if(!command.getSubCommands().isEmpty())' LINE, normally this operation will never be called
                    argIter.previous();
                    return;
                }

                boolean matches = false;

                for (CommandSpec commandSpec : commandSpecs) {

                    if (processed.contains(commandSpec))
                        continue;

                    if (findLastSet(commandDatas) && (main != null && !main.contains(commandSpec))) {
                        // TODO revision, see 'if(matches && parent != null)' LINE, this operation will never be called
                        argIter.previous();
                        return;
                    }

                    boolean last = !argIter.hasNext();

                    if (commandSpec.matches(argument)) {

                        ArgumentsHolder argumentHolders = parseArguments(argIter, commandSpec, commandSpecs, requirements, informationRegister);

                        CommandHolder holder = new CommandHolder(commandSpec, parent, argumentHolders, true, last);

                        commandDatas.add(new CommandData<>(argument, holder, parent));

                        int size = commandDatas.size();

                        if (!commandSpec.getSubCommands().isEmpty()) {
                            process(argIter, commandSpec.getSubCommands(), holder, requirements, informationRegister);
                        }

                        if (size == commandDatas.size()) {
                            commandDatas.remove(commandDatas.size() - 1);
                            commandDatas.add(new CommandData<>(argument, new CommandHolder(commandSpec, parent, argumentHolders, true, true), parent));
                        }
                        processed.add(commandSpec);
                        matches = true;
                    } else {
                        if (commandSpec.isOptional() || parent == null) {
                            //argIter.previous();
                            continue;
                        } else {
                            if (last) {
                                handlerContainer.handle(new ProcessingError("CRITICAL NOT found to argument: '" + argument + "'", ErrorType.POSSIBLE_BUG), commandSpecs, commandSpec, commandDatas, requirements, informationRegister);
                            }
                            handlerContainer.handle(new ProcessingError("Not found to argument: '" + argument + "'", ErrorType.POSSIBLE_BUG), commandSpecs, commandSpec, commandDatas, requirements, informationRegister);
                        }
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

                    if (matches && parent != null) {
                        // RETURN commandSpec loop for child
                        return;
                    } else if (matches) {
                        break;
                    }
                }

                if (!matches && parent == null) {
                    handlerContainer.handle(new ProcessingError("Cannot find command '" + argument + "'", ErrorType.MISSING_ARGUMENT), commandSpecs, null, commandDatas, requirements, informationRegister);
                }
            }
        }

        // give xp [number]
        // give item [number]

        // give xp 15
        // |  | || ||
        // CMD --- ARG

        // give item STONE | jump to 15
        //
        private ArgumentsHolder parseArguments(ListIterator<String> argumentIter, CommandSpec commandSpec, CommandList commandSpecs, Requirements requirements, InformationRegister informationRegister) throws ProcessingError {
            ArgumentsHolder argumentHolders = new ArgumentsHolder();
            // ArgumentSpec parsing

            // Check if any sub command matches this name, if matches, will return the 'empty arguments holder'.
            if (argumentIter.hasNext()) {
                String name = argumentIter.next();
                argumentIter.previous();

                List<CommandSpec> sub = commandSpec.getSubCommands();

                if (!sub.isEmpty()) {
                    for (CommandSpec subSpec : sub) {
                        if (subSpec.matches(name)) {
                            return argumentHolders;
                        }
                    }
                }
            }

            for (ArgumentSpec<?, ?> argumentSpecParse : commandSpec.getArguments()) {

                if (!argumentIter.hasNext()) {
                    ArgumentHolder argument = new ArgumentHolder<>(null, argumentSpecParse);
                    argumentHolders.add(argument);
                }

                while (argumentIter.hasNext()) {
                    String sub = argumentIter.next();

                    Boolean anyMatches = null;

                    if (argumentSpecParse.getChecker() != null) {
                        anyMatches = argumentSpecParse.getChecker().get().matches(sub);
                    }

                    if (argumentSpecParse.getPredicate() != null) {
                        boolean result = argumentSpecParse.getPredicate().test(sub);

                        if (anyMatches == null)
                            anyMatches = result;
                        else
                            anyMatches = anyMatches && result;
                    }


                    if (anyMatches == null && argumentSpecParse.getConverter() != null) {
                        boolean result = false;

                        try {
                            result = argumentSpecParse.getConverter().apply(sub) != null;
                        } catch (Throwable t) {
                        }
                        anyMatches = result;
                    }


                    if (anyMatches != null && anyMatches) {
                        ArgumentHolder argument = new ArgumentHolder<>(sub, argumentSpecParse);
                        argumentHolders.add(argument);
                        break;
                    } else {
                        if (argumentSpecParse.isOptional()) {
                            argumentIter.previous();
                            ArgumentHolder argument = new ArgumentHolder<>(null, argumentSpecParse);
                            argumentHolders.add(argument);
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
