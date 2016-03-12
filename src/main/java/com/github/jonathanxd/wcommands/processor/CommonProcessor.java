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
import com.github.jonathanxd.wcommands.exceptions.ArgumentError;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.exceptions.MissingArgumentException;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.interceptor.Interceptors;
import com.github.jonathanxd.wcommands.interceptor.Phase;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.StaticContainer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by jonathan on 26/02/16.
 */
// List of CommandData<CommandHolder>
public class CommonProcessor implements Processor<List<CommandData<CommandHolder>>> {

    public static WCommand<List<CommandData<CommandHolder>>> newWCommand(ErrorHandler handler) {
        return new WCommand<>(new CommonProcessor(), handler);
    }

    @Override
    public List<CommandData<CommandHolder>> process(List<String> arguments, CommandList commands, ErrorHandler handler) throws ArgumentProcessingError {
        Processing processing = new Processing(new ErrorHandler.Container(handler));
        processing.process(arguments.listIterator(), commands, null);
        return processing.commandDatas;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void invokeCommands(List<CommandData<CommandHolder>> object, Interceptors interceptors) {

        Interceptors phasePreCall = interceptors.getPhase(Phase.PRE_CALL);
        Interceptors phasePostCall = interceptors.getPhase(Phase.POST_CALL);

        for (CommandData<CommandHolder> data : object) {

            CommandHolder holder = data.getCommand();
            if (holder.isLastMatching()) {
                Handler<CommandHolder> handler = holder.getCommandSpec().getDefaultHandler();


                Container<Handler<CommandHolder>> handlerContainer = new Container<>(handler);

                // PRE CALL MODIFICATIONS
                phasePreCall.forEach(interceptor -> interceptor.intercept(data, handlerContainer));

                if (handlerContainer.get() != null) {
                    handlerContainer.get().handle(data);
                }


                StaticContainer<Handler<CommandHolder>> staticContainer = new StaticContainer<>(handlerContainer);
                // POST CALL MONITORING
                phasePostCall.forEach(interceptor -> interceptor.intercept(data, staticContainer));
            }
        }
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
        private final ErrorHandler.Container handlerContainer;
        private final List<CommandData<CommandHolder>> commandDatas = new ArrayList<>();
        private List<CommandSpec> main = null;
        private Set<CommandSpec> processed = new HashSet<>();

        private Processing(ErrorHandler.Container handlerContainer) {
            this.handlerContainer = handlerContainer;
        }

        public void process(ListIterator<String> argIter, List<CommandSpec> commandSpecs, CommandHolder parent) throws ArgumentProcessingError {
            if (main == null) {
                main = commandSpecs;
            }
            if (!argIter.hasNext()) {
                handlerContainer.handle(new ArgumentProcessingError("No more elements!", ArgumentError.POSSIBLE_BUG));
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

                        ArgumentsHolder argumentHolders = parseArguments(argIter, commandSpec);

                        CommandHolder holder = new CommandHolder(commandSpec, argumentHolders, true, last);

                        commandDatas.add(new CommandData<>(argument, holder, parent));

                        int size = commandDatas.size();

                        if (!commandSpec.getSubCommands().isEmpty()) {
                            process(argIter, commandSpec.getSubCommands(), holder);
                        }

                        if (size == commandDatas.size()) {
                            commandDatas.remove(commandDatas.size() - 1);
                            commandDatas.add(new CommandData<>(argument, new CommandHolder(commandSpec, argumentHolders, true, true), parent));
                        }
                        processed.add(commandSpec);
                        matches = true;
                    } else {
                        if (commandSpec.isOptional()) {
                            //argIter.previous();
                            continue;
                        } else {
                            if (last) {
                                handlerContainer.handle(new ArgumentProcessingError("CRITICAL NOT found to argument: '" + argument + "'", ArgumentError.POSSIBLE_BUG));
                            }
                            handlerContainer.handle(new ArgumentProcessingError("Not found to argument: '" + argument + "'", ArgumentError.POSSIBLE_BUG));
                        }
                    }
                    ////////////////////////////////
                    if (last) {
                        List<CommandSpec> notOptional = commandSpec.getNotOptionalSubCommands();
                        if (notOptional.size() > 0) {
                            if (!findAny(commandDatas, notOptional)) {
                                handlerContainer.handle(new ArgumentProcessingError("Missing sub-commandSpec for commandSpec '" + commandSpec + "'", ArgumentError.MISSING_SUB_COMMAND));
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

                if(!matches && parent == null) {
                    throw new ArgumentProcessingError("Cannot find command '"+argument+"'", ArgumentError.MISSING_ARGUMENT);
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
        private ArgumentsHolder parseArguments(ListIterator<String> argumentIter, CommandSpec commandSpec) throws ArgumentProcessingError {
            ArgumentsHolder argumentHolders = new ArgumentsHolder();
            // ArgumentSpec parsing

            for (ArgumentSpec<?, ?> argumentSpecParse : commandSpec.getArguments()) {

                if(!argumentIter.hasNext()) {
                    ArgumentHolder argument = new ArgumentHolder<>(null, argumentSpecParse);
                    argumentHolders.add(argument);
                }

                while (argumentIter.hasNext()) {
                    String sub = argumentIter.next();

                    if ((argumentSpecParse.getChecker() == null || argumentSpecParse.getChecker().get().matches(sub)) && (argumentSpecParse.getPredicate() == null || argumentSpecParse.getPredicate().test(sub))) {
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
                                    new ArgumentProcessingError(
                                            new IllegalArgumentException(String.format("Illegal argument '%s' for parser '%s'", sub, argumentSpecParse)),
                                            ArgumentError.MISSING_ARGUMENT));

                        }

                    }

                }

            }

            int pos = 0;
            for (ArgumentSpec argumentSpec : commandSpec.getArguments()) {
                if (argumentHolders.size() == 0) {
                    if (!argumentSpec.isOptional()) {
                        handlerContainer.handle(
                                new ArgumentProcessingError(
                                        new MissingArgumentException(String.format("Missing argumentSpec (id: '%s', %s). Probably is missing all arguments required...", argumentSpec.getId(), argumentSpec)),
                                        ArgumentError.MISSING_ARGUMENT));
                    } else {
                        continue;
                    }
                }
                while (pos < argumentHolders.size()) {
                    ArgumentHolder holder = argumentHolders.get(pos);

                    if (argumentSpec != holder.getArgumentSpec()) {
                        if (!argumentSpec.isOptional()) {

                            handlerContainer.handle(
                                    new ArgumentProcessingError(
                                            new MissingArgumentException(String.format("Missing argumentSpec (id: '%s', %s)", argumentSpec.getId(), argumentSpec)),
                                            ArgumentError.MISSING_ARGUMENT));
                        } else {
                            ++pos;
                            continue;
                        }
                    }else{
                        ++pos;
                        break;
                    }


                }
            }

            return argumentHolders;
        }
    }


}
