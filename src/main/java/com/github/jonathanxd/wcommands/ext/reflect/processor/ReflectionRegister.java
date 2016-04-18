/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands.ext.reflect.processor;

import com.github.jonathanxd.iutils.annotations.NotNull;
import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.Register;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.processor.exception.InvalidDependency;
import com.github.jonathanxd.wcommands.ext.reflect.processor.exception.PossibleCyclicDependencies;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeHead;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.ListUtils;
import com.github.jonathanxd.wcommands.util.reflection.ClassExplorer;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Created by jonathan on 26/03/16.
 */
public class ReflectionRegister<T> extends Register<T> {
    private final ReflectionCommandProcessor wCommand;

    public ReflectionRegister(ReflectionCommandProcessor wCommand, RegistrationTicket<T> ticket) {
        super(wCommand, ticket);
        this.wCommand = wCommand;
    }

    /**
     * Instance only will be added when {@link ReflectionCommandProcessor#process(List, Requirements, InformationRegister)} is
     * called!
     *
     * @param o     Instance
     * @param clazz Commands Clazz
     */
    public void addAsFuture(Object o, Class<?> clazz) {
        wCommand.getWaitingQueue().offer(() -> addCommands(o, clazz));
    }

    public void addCommands(Object instance) {
        addCommands(instance, instance.getClass());
    }

    public void addCommandFromClass(Class<?> clazz, Function<Class<?>, @NotNull Object> instanceCreator) {
        ClassExplorer.explore(clazz, aClass -> addCommands(instanceCreator.apply(aClass)));
    }

    public void addCommands(Object instance, Class<?> clazz) {
        objectToCommandList(instance, clazz).forEach(this::registerCommand);
    }

    @SuppressWarnings("Duplicates")
    public List<CommandSpec> objectToCommandList(Object instance, Class<?> clazz) {

        List<CommandSpec> commandSpecs = new ArrayList<>();

        Objects.requireNonNull(instance);
        Objects.requireNonNull(clazz);

        List<NamedContainer> namedContainers = new ArrayList<>();

        java.util.Set<ElementBridge> bridgeSet = new TreeSet<>(new PriorityComparator(wCommand.getAnnotationVisitors()));

        /*************** HEAD PROCESSING SINCE 18/03/2016 (03/18/2016) ***************/

        TreeHead treeHead = new TreeHead();

        bridgeSet.add(new ElementBridge(clazz, ElementType.TYPE, Order.FIRST));

        /*************** HEAD PROCESSING SINCE 18/03/2016 (03/18/2016) ***************/

        for (Field field : clazz.getDeclaredFields()) {
            bridgeSet.add(new ElementBridge(field, ElementType.FIELD));
        }

        for (Method method : clazz.getDeclaredMethods()) {
            bridgeSet.add(new ElementBridge(method, ElementType.METHOD));
        }

        for (ElementBridge bridge : bridgeSet) {
            NamedContainer processed = processWrapper(bridge, treeHead);

            if (processed != null) {
                addToList(treeHead.getLast(), namedContainers, processed);
            }
        }

        namedContainers = Collections.unmodifiableList(namedContainers);

        Postpone postpone = new Postpone();

        List<CommandSpec> current = new ArrayList<>(this.wCommand.getCommandList());
        current.addAll(commandSpecs);

        CommandList currentCommands = new CommandList(current, this.wCommand.getCommandList().getHoldingObject());

        for (NamedContainer namedContainer : namedContainers) {
            CommandSpec commandSpec = this.processCommand(namedContainer, new InstanceContainer(instance), postpone, treeHead, currentCommands);
            if (commandSpec != null)
                alloc(commandSpecs, current, commandSpec);
        }

        // Postpone process

        if (!postpone.hasNextInMain() && postpone.hasPostpone()) {
            postpone.updateToPostpone();
        }

        while (postpone.hasNextInMain()) {
            NamedContainer container = postpone.next();

            CommandSpec commandSpec = this.processCommand(container, new InstanceContainer(instance), postpone, treeHead, currentCommands);
            if (commandSpec != null)
                alloc(commandSpecs, current, commandSpec);

            if (!postpone.hasNextInMain() && postpone.hasPostpone()) {
                postpone.updateToPostpone();
            }
        }

        return commandSpecs;
    }

    private void alloc(List<CommandSpec> toAlloc, List<CommandSpec> linked, CommandSpec commandSpec) {
        toAlloc.add(commandSpec);
        linked.add(commandSpec);
    }

    private void addToList(TreeNamedContainer head, List<NamedContainer> namedContainerList, NamedContainer namedContainer) {
        if (head != null) {
            if (namedContainer != head) {
                if (namedContainer instanceof TreeNamedContainer) {
                    head.getChild().add((TreeNamedContainer) namedContainer);
                } else if (namedContainer instanceof SingleNamedContainer) {
                    head.getArgumentContainers().add((SingleNamedContainer) namedContainer);
                }
            }
            if (namedContainerList.isEmpty() || !namedContainerList.contains(head)) {
                namedContainerList.add(head);
            }

        } else {
            namedContainerList.add(namedContainer);
        }
    }

    private NamedContainer processWrapper(ElementBridge bridge, TreeHead treeHead) {
        Annotation[] annotations = bridge.getDeclaredAnnotations();

        Container<NamedContainer> theContainer = new Container<>(null);
        Container<NamedContainer> last = new Container<>(null);

        for (Annotation annotation : annotations) {

            Optional<AnnotationVisitor<Annotation, NamedContainer, Object>> factory = wCommand.getAnnotationVisitors().getFor(annotation.annotationType());

            if (factory.isPresent()) {
                factory.get().visitElementAnnotation(annotation, theContainer, last, bridge, bridge.getLocation(), treeHead, getTicket());
            }
        }

        if (bridge.isMethod()) {
            Method executable = (Method) bridge.getMember();

            for (AnnotatedType annotatedType : executable.getAnnotatedParameterTypes()) {

                ElementBridge annotatedTypeBridge = new ElementBridge(annotatedType, bridge.getLocation());

                for (Annotation annotation : annotatedType.getDeclaredAnnotations()) {

                    Optional<AnnotationVisitor<Annotation, NamedContainer, Object>> factory = wCommand.getAnnotationVisitors().getFor(annotation.annotationType());

                    if (factory.isPresent()) {
                        factory.get().visitElementAnnotation(annotation, theContainer, last, annotatedTypeBridge, bridge.getLocation(), treeHead, getTicket());
                    }

                }
            }

        }

        return theContainer.get();
    }

    @SuppressWarnings("unchecked")
    private <C extends NamedContainer, T> T helpTo(AnnotationVisitor<?, C, T> t, NamedContainer named, InstanceContainer instance, TreeHead treeHead, CommandList commandList) {
        return t.process((C) named, instance, wCommand, commandList, wCommand, named.getBridge().getLocation(), treeHead, getTicket(), Optional.empty());
    }

    @SuppressWarnings("unchecked")
    private <C extends NamedContainer, T> boolean helpToCheck(AnnotationVisitor<?, C, T> t, NamedContainer named, InstanceContainer instance, TreeHead treeHead, CommandList registeredCommands) {
        return t.dependencyCheck((C) named, instance, wCommand, registeredCommands, wCommand, named.getBridge().getLocation(), treeHead, getTicket(), Optional.empty());
    }

    @SuppressWarnings("unchecked")
    private CommandSpec processCommand(NamedContainer namedContainer, InstanceContainer instance, Postpone postpone, TreeHead treeHead, CommandList registeredCommands) {

        Optional<AnnotationVisitor<Annotation, NamedContainer, CommandSpec>> visitorOptional = wCommand.getAnnotationVisitors().<Annotation, NamedContainer, CommandSpec>getFor(namedContainer.get().annotationType());

        if (visitorOptional.isPresent()) {

            if (!helpToCheck(visitorOptional.get(), namedContainer, instance, treeHead, registeredCommands)) {
                postpone.postpone(namedContainer);

                return null;
            } else {
                CommandSpec spec = helpTo(visitorOptional.get(), namedContainer, instance, treeHead, registeredCommands);

                return spec != null && !spec.isEmpty() ? spec : null;
            }
        }

        return null;


    }

    private final static class Postpone {
        private static final int MAX_STACKS = 256;
        private final List<NamedContainer> original = new ArrayList<>();
        private final List<NamedContainer> list = new ArrayList<>();
        private final List<NamedContainer> postpone = new ArrayList<>();
        private int stacks = 0;

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

                for (NamedContainer container : this.original) {
                    sj.add(container.getName());
                }


                if (this.original.size() == 1) {

                    StringJoiner dependencies = new StringJoiner(", ", "[", "]");

                    Annotation annotation;

                    if ((annotation = this.original.get(0).get()) instanceof SubCommand) {
                        SubCommand subCommand = (SubCommand) annotation;
                        Arrays.stream(subCommand.value()).forEach(dependencies::add);
                    }

                    throw new InvalidDependency("Possible invalid dependencies! Involved elements: '" + sj.toString() + "'. Involved dependencies " + dependencies.toString() + ". Postpone List: '" + postpone + "'. Current List: '" + original);

                } else {
                    if(stacks < MAX_STACKS) {
                        ++stacks;
                    } else {
                        throw new PossibleCyclicDependencies("Possible cyclic dependencies! Involved elements: '" + sj.toString() + "'. Postpone List: '" + postpone + "'. Current List: '" + original);
                    }
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
