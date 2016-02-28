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
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.ArgumentContainer;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.enums.EnumTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.BooleanTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.NumberTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.StringTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TypeTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.CommandContainer;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.factory.ArgumentBuilder;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.interceptor.Priority;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 27/02/16.
 */
public class ReflectionCommandProcessor extends WCommandCommon implements TranslatorSupport {

    private final TranslatorList translatorList = new TranslatorList();

    public ReflectionCommandProcessor() {
        super();
        initBasics();
    }


    private void initBasics() {
        addGlobalTranslator(Boolean.class, BooleanTranslator.class);
        addGlobalTranslator(Number.class, NumberTranslator.class);
        addGlobalTranslator(String.class, StringTranslator.class);
        addGlobalTranslator(Enum.class, EnumTranslator.class, Priority.LAST);
    }

    public ReflectionCommandProcessor(ErrorHandler errorHandler) {
        super(errorHandler);
        initBasics();
    }

    public ReflectionCommandProcessor(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler handler) {
        super(processor, handler);
        initBasics();
    }

    @Override
    public <T>void addGlobalTranslator(Class<T> type, Class<? extends Translator<?>> translator, Priority priority) {
        translatorList.add(new TypeTranslator<>(type, translator, priority));
    }

    @Override
    public <T>void removeGlobalTranslator(Class<T> type) {
        translatorList.removeIf(t -> t.getType() == type);
    }

    @Override
    public void forEach(BiConsumer<Class<?>, Class<? extends Translator<?>>> consumer) {
        translatorList.forEach(t -> consumer.accept(t.getType(), t.getTranslator()));
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
            CommandSpec commandSpec = this.processCommand(commandContainer, new InstanceContainer(instance));
            this.addCommand(commandSpec);
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
    private CommandSpec processCommand(CommandContainer command, InstanceContainer instance) {

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


            ExtraData data = new ExtraData();

            Translator<?> translator = null;
            try {
                data.registerData((TranslatorSupport) this);
                data.registerData(command);
                data.registerData(argumentContainer);
                data.registerData(argumentContainer.getName());
                data.registerData(argumentContainer.getType());

                data.registerData(instance);

                translator = (Translator<?>) data.construct(argument.translator());
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }


            if (translator != null) {
                argumentBuilder.withPredicate(translator::isAcceptable);
                argumentBuilder.withConverter(translator::translate);
            }


            argumentBuilder.withOptional(argument.isOptional());

            ArgumentSpec argumentSpec1 = argumentBuilder.build();

            if (argument.setFinal()) {
                argumentSpec1.getData().registerData(Set.FINAL);
            }

            commandBuilder.withArgument(argumentSpec1);
        }

        command.getChild().forEach(commandContainer -> commandBuilder.withChild(processCommand(commandContainer, instance)));

        CommandSpec cmd = commandBuilder.build();

        return cmd;


    }

    public enum Set {
        FINAL
    }
}
