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
package com.github.jonathanxd.wcommands.ext.reflect.processor;

import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.ArgumentContainer;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.CommandContainer;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.factory.ArgumentBuilder;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 27/02/16.
 */
public class ReflectionCommandProcessor extends WCommandCommon {
    public ReflectionCommandProcessor() {
        super();
    }

    public ReflectionCommandProcessor(ErrorHandler errorHandler) {
        super(errorHandler);
    }

    public ReflectionCommandProcessor(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler handler) {
        super(processor, handler);
    }

    public void addCommands(Object instance, Class<?> clazz) {
        List<CommandContainer> commandContainers = new LinkedList<>();

        for (Field field : clazz.getDeclaredFields()) {
            commandContainers.add(processWrapper(new ElementBridge(field)));
        }

        for (Method method : clazz.getDeclaredMethods()) {
            commandContainers.add(processWrapper(new ElementBridge(method)));
        }

        commandContainers = commandContainers.stream().filter(d -> d != null).collect(Collectors.toList());

        commandContainers.forEach(commandContainer -> {
            com.github.jonathanxd.wcommands.command.Command command = this.processCommand(commandContainer, new InstanceContainer(instance));
            this.addCommand(command);
        });
    }

    private CommandContainer processWrapper(ElementBridge bridge) {
        Annotation[] annotations = bridge.getDeclaredAnnotations();

        CommandContainer commandContainer = null;
        CommandContainer last = null;

        for (Annotation annotation : annotations) {
            if (annotation instanceof Command) {
                if (commandContainer == null) {
                    last = (commandContainer = new CommandContainer((Command) annotation, bridge));
                } else {
                    last = new CommandContainer((Command) annotation, bridge);
                    commandContainer.getChild().add(last);
                }
            }
            processAnnotation(last, annotation, bridge);
        }

        if (bridge.isMethod()) {
            Method executable = (Method) bridge.getMember();

            for(AnnotatedType annotatedType : executable.getAnnotatedParameterTypes()) {

                ElementBridge annotatedTypeBridge = new ElementBridge(annotatedType);

                for(Annotation annotation : annotatedType.getDeclaredAnnotations()) {
                    processAnnotation(last, annotation, annotatedTypeBridge);
                }
            }

        }

        return commandContainer;
    }

    private void processAnnotation(CommandContainer container, Annotation annotation, ElementBridge bridge) {
        if (annotation instanceof Argument) {
            Argument argument = (Argument) annotation;
            if (container != null) {
                container.getArgumentContainers().add(new ArgumentContainer(argument, bridge));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private com.github.jonathanxd.wcommands.command.Command processCommand(CommandContainer command, InstanceContainer instance) {

        Command commandAnnotation = command.get();
        List<ArgumentContainer> arguments = command.getArgumentContainers();


        CommandBuilder<CommandHolder> commandBuilder = CommandBuilder.builder();

        commandBuilder.withName(Text.of(command.getName()));
        commandBuilder.withPrefix(commandAnnotation.prefix());
        commandBuilder.withSuffix(commandAnnotation.suffix());

        Handler<CommandHolder> handler = null;

        try {
            ExtraData data = new ExtraData();

            data.registerData(command);
            data.registerData(command.getBridge());
            data.registerData(command.getName());

            data.registerData(instance);

            handler = (Handler<CommandHolder>) data.construct(commandAnnotation.handler());
        } catch (Throwable ignore) {
        }

        commandBuilder.withHandler(handler);

        for (ArgumentContainer argumentContainer : arguments) {
            // ARGUMENTS
            Argument argument = argumentContainer.get();

            ArgumentBuilder<String, Object> argumentBuilder = ArgumentBuilder.builder();

            argumentBuilder.withId(argumentContainer.getName());

            Predicate<Matchable<String>> predicate = null;

            ExtraData data = new ExtraData();

            try {
                data.registerData(argumentContainer);
                data.registerData(argumentContainer.getName());
                data.registerData(argumentContainer.getType());

                data.registerData(instance);

                predicate = (Predicate<Matchable<String>>) data.construct(argument.predicate());
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }

            if(predicate != null) {
                data.registerData(predicate);
            }

            argumentBuilder.withPredicate(predicate);

            argumentBuilder.withOptional(argument.isOptional());

            Translator<?> translator = null;

            try {
                translator = (Translator<?>) data.construct(argument.translator());
            } catch (Throwable ignored) {
            }

            if (translator != null) {
                argumentBuilder.withConverter(translator::translate);
            }
            com.github.jonathanxd.wcommands.arguments.Argument argument1 = argumentBuilder.build();

            if (argument.setFinal()) {
                argument1.getData().registerData(Set.FINAL);
            }

            commandBuilder.withArgument(argument1);
        }

        command.getChild().forEach(commandContainer -> commandBuilder.withChild(processCommand(commandContainer, instance)));

        com.github.jonathanxd.wcommands.command.Command cmd = commandBuilder.build();

        return cmd;


    }

    public enum Set {
        FINAL
    }
}
