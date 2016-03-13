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

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.enums.EnumTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TypeTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.BooleanTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.NumberTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.StringTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.processor.exception.InvalidDependency;
import com.github.jonathanxd.wcommands.ext.reflect.processor.exception.PossibleCyclicDependencies;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitors;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults.ArgumentVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults.CommandVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults.SubCommandVisitor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.util.ListUtils;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 27/02/16.
 */

public class ReflectionCommandProcessor extends WCommandCommon implements TranslatorSupport, AnnotationVisitorSupport {

    private final TranslatorList translatorList = new TranslatorList();
    private final AnnotationVisitors annotationVisitors = new AnnotationVisitors();

    public ReflectionCommandProcessor() {
        super();
        initBasics();
    }

    public ReflectionCommandProcessor(ErrorHandler<List<CommandData<CommandHolder>>> errorHandler) {
        super(errorHandler);
        initBasics();
    }

    public ReflectionCommandProcessor(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler) {
        super(processor, handler);
        initBasics();
    }

    private void initBasics() {
        addGlobalTranslator(Reference.aEnd(Boolean.class), BooleanTranslator.class);
        addGlobalTranslator(Reference.aEnd(Number.class), NumberTranslator.class);
        addGlobalTranslator(Reference.aEnd(String.class), StringTranslator.class);
        addGlobalTranslator(Reference.aEnd(Enum.class), EnumTranslator.class, Order.SEVENTH);

        registerVisitor(new CommandVisitor(Command.class));
        registerVisitor(new ArgumentVisitor(Argument.class));
        registerVisitor(new SubCommandVisitor(SubCommand.class));

    }

    @Override
    public <T> void addGlobalTranslator(Reference<T> type, Class<? extends Translator<?>> translator, Order order) {
        translatorList.add(new TypeTranslator<>(type, translator, order));
    }

    @Override
    public <T> void removeGlobalTranslator(Reference<T> type) {
        translatorList.removeIf(t -> t.getType() == type);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void forEach(BiConsumer<Reference<?>, Class<? extends Translator<?>>> consumer) {
        translatorList.forEach(t -> consumer.accept(t.getType(), t.getTranslator()));
    }

    public void addCommands(Object instance, Class<?> clazz) {
        List<NamedContainer> namedContainers = new LinkedList<>();

        java.util.Set<ElementBridge> bridgeSet = new TreeSet<>(new PriorityComparator(annotationVisitors));

        for (Field field : clazz.getDeclaredFields()) {
            bridgeSet.add(new ElementBridge(field));
        }

        for (Method method : clazz.getDeclaredMethods()) {
            bridgeSet.add(new ElementBridge(method));
        }

        for (ElementBridge bridge : bridgeSet) {
            namedContainers.add(processWrapper(bridge));
        }

        namedContainers = namedContainers.stream().filter(d -> d != null).collect(Collectors.toList());

        Postpone postpone = new Postpone();

        for (NamedContainer namedContainer : namedContainers) {
            CommandSpec commandSpec = this.processCommand(namedContainer, new InstanceContainer(instance), postpone);
            if (commandSpec != null)
                this.registerCommand(commandSpec);
        }

        // Postpone process

        if (!postpone.hasNextInMain() && postpone.hasPostpone()) {
            postpone.updateToPostpone();
        }

        while (postpone.hasNextInMain()) {
            NamedContainer container = postpone.next();

            CommandSpec commandSpec = this.processCommand(container, new InstanceContainer(instance), postpone);
            if (commandSpec != null)
                this.registerCommand(commandSpec);

            if (!postpone.hasNextInMain() && postpone.hasPostpone()) {
                postpone.updateToPostpone();
            }
        }

    }

    private NamedContainer processWrapper(ElementBridge bridge) {
        Annotation[] annotations = bridge.getDeclaredAnnotations();

        Container<NamedContainer> theContainer = new Container<>(null);
        Container<NamedContainer> last = new Container<>(null);

        for (Annotation annotation : annotations) {

            Optional<AnnotationVisitor<Annotation, NamedContainer, Object>> factory = annotationVisitors.<Annotation, NamedContainer, Object>getFor(annotation.annotationType());

            if (factory.isPresent()) {
                factory.get().visitElementAnnotation(annotation, theContainer, last, bridge);
            }
        }

        if (bridge.isMethod()) {
            Method executable = (Method) bridge.getMember();

            for (AnnotatedType annotatedType : executable.getAnnotatedParameterTypes()) {

                ElementBridge annotatedTypeBridge = new ElementBridge(annotatedType);

                for (Annotation annotation : annotatedType.getDeclaredAnnotations()) {

                    Optional<AnnotationVisitor<Annotation, NamedContainer, Object>> factory = annotationVisitors.<Annotation, NamedContainer, Object>getFor(annotation.annotationType());

                    if (factory.isPresent()) {
                        factory.get().visitElementAnnotation(annotation, theContainer, last, annotatedTypeBridge);
                    }

                }
            }

        }

        return theContainer.get();
    }

    @SuppressWarnings("unchecked")
    private <C extends NamedContainer, T> T helpTo(AnnotationVisitor<?, C, T> t, NamedContainer named, InstanceContainer instance) {
        return t.process((C) named, instance, this, this, Optional.empty());
    }

    @SuppressWarnings("unchecked")
    private <C extends NamedContainer, T> boolean helpToCheck(AnnotationVisitor<?, C, T> t, NamedContainer named, InstanceContainer instance) {
        return t.dependencyCheck((C) named, instance, this, this, Optional.empty());
    }

    @SuppressWarnings("unchecked")
    private CommandSpec processCommand(NamedContainer namedContainer, InstanceContainer instance, Postpone postpone) {

        Optional<AnnotationVisitor<Annotation, NamedContainer, CommandSpec>> visitorOptional = annotationVisitors.<Annotation, NamedContainer, CommandSpec>getFor(namedContainer.get().annotationType());

        if (visitorOptional.isPresent()) {

            if (!helpToCheck(visitorOptional.get(), namedContainer, instance)) {
                postpone.postpone(namedContainer);

                return null;
            } else {
                CommandSpec spec = helpTo(visitorOptional.get(), namedContainer, instance);

                return spec != null && !spec.isEmpty() ? spec : null;
            }
        }

        return null;


    }

    @Override
    public boolean registerVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor) {
        if (annotationVisitors.contains(annotationVisitor))
            return false;
        annotationVisitors.add(annotationVisitor);
        return true;
    }

    @Override
    public boolean overrideVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor) {
        if (!annotationVisitors.contains(annotationVisitor))
            return false;
        annotationVisitors.removeIf(a -> a.getAnnotationClass() == annotationVisitor.getAnnotationClass());
        annotationVisitors.add(annotationVisitor);
        return true;
    }

    @Override
    public boolean removeVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor) {
        return annotationVisitors.remove(annotationVisitor);
    }

    @Override
    public boolean removeVisitor(Class<?> annotationType) {
        return annotationVisitors.removeIf(a -> a.getAnnotationClass() == annotationType);
    }

    @Override
    public <T extends Annotation, C extends NamedContainer, R> Optional<AnnotationVisitor<T, C, R>> getVisitorFor(Class<? extends Annotation> clazz) {
        return annotationVisitors.<T, C, R>getFor(clazz);
    }

    public enum PropSet {
        FINAL
    }


    private final static class Postpone {
        private final List<NamedContainer> original = new ArrayList<>();
        private final List<NamedContainer> list = new ArrayList<>();
        private final List<NamedContainer> postpone = new ArrayList<>();

        public void postpone(NamedContainer container) {
            postpone.add(container);
        }

        public boolean hasNextInMain() {
            return !list.isEmpty();
        }

        public NamedContainer next() {
            if (!hasNextInMain())
                throw new RuntimeException();

            return list.remove(0);
        }

        public boolean hasPostpone() {
            return !postpone.isEmpty();
        }

        public void updateToPostpone() {
            if (!hasPostpone())
                throw new RuntimeException();

            if (!original.isEmpty() && ListUtils.equals(this.original, postpone)) {
                StringJoiner sj = new StringJoiner(", ", "[", "]");

                for(NamedContainer container : this.original) {
                    sj.add(container.getName());
                }


                if(this.original.size() == 1) {

                    StringJoiner dependencies = new StringJoiner(", ", "[", "]");

                    Annotation annotation;

                    if((annotation = this.original.get(0).get()) instanceof SubCommand) {
                        SubCommand subCommand = (SubCommand) annotation;
                        Arrays.stream(subCommand.value()).forEach(dependencies::add);
                    }

                    throw new InvalidDependency("Possible invalid dependencies! Involved elements: '"+sj.toString()+"'. Involved dependencies "+dependencies.toString()+". Postpone List: '"+postpone+"'. Current List: '"+original);

                } else {
                    throw new PossibleCyclicDependencies("Possible cyclic dependencies! Involved elements: '"+sj.toString()+"'. Postpone List: '"+postpone+"'. Current List: '"+original);
                }

            }

            this.list.clear();
            this.list.addAll(postpone);

            this.original.clear();
            this.original.addAll(postpone);

            this.postpone.clear();
        }
    }
}
