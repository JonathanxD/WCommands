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
package com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults;

import com.github.jonathanxd.iutils.containers.Container;
import com.github.jonathanxd.iutils.data.DataProvider;
import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.iutils.object.GenericRepresentation;
import com.github.jonathanxd.iutils.object.HolderGenericRepresentation;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.IsOptional;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeHead;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.factory.ArgumentBuilder;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.Require;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.ElementType;
import java.util.Optional;

/**
 * Created by jonathan on 29/02/16.
 */
@DataProvider(
        value = {
                TranslatorSupport.class,
                CommandSpec.class,
                NamedContainer.class,
                String.class,
                GenericRepresentation.class,
                InstanceContainer.class},
        description = {
                "Can retrieve and add Translators",
                "Can inspect and Modify CommandSpec related",
                "Named container",
                "Name of Argument",
                "Reference to Types & generics",
                "Container of Object instance",
                "----------- NEED REVIEW"})

public class ArgumentVisitor extends AnnotationVisitor<Argument, SingleNamedContainer, ArgumentSpec<?, ?>> {

    public ArgumentVisitor(Class<Argument> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void visitElementAnnotation(Argument annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket) {

        String name = annotation.id().trim().isEmpty() ? bridge.getName() : annotation.id();

        if (annotation.isOptional()) {

            if (bridge.getType() == Optional.class && (annotation.type() == null || annotation.type() == Argument.PR.class) && bridge.directReference() == null) {
                throw new RuntimeException("Cannot handle Optional, impossible to determine the Type, use: 'Argument.type()'!");
            } else {
                if (bridge.directReference() == null) {
                    if (annotation.type() != Argument.PR.class) {
                        bridge = new ElementBridge(bridge.getMember(), location, GenericRepresentation.aEnd(annotation.type()));
                    }
                }
            }
        }

        SingleNamedContainer container = new SingleNamedContainer(name, annotation, bridge);

        if (last.isPresent() && last.get() instanceof TreeNamedContainer) {
            TreeNamedContainer treeNamedContainer = Require.require(last.get(), TreeNamedContainer.class);

            treeNamedContainer.getArgumentContainers().add(container);
        } else {
            if (!current.isPresent()) {
                current.set(container);
                last.set(container);
            } else {
                System.err.println("Failed to get HEAD element!");
            }
        }
    }


    @Override
    public ArgumentSpec<?, ?> process(SingleNamedContainer argumentContainer, InstanceContainer instance, AnnotationVisitorSupport support, CommandList common, TranslatorSupport translatorSupport, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket, Optional<NamedContainer> parent) {
        Argument argument = (Argument) argumentContainer.get();

        ArgumentBuilder<String, Object> argumentBuilder = ArgumentBuilder.builder();

        argumentBuilder.withId(argumentContainer.getName());


        ExtraData data = new ExtraData();

        Translator<?> translator = null;
        try {
            data.registerData((TranslatorSupport) translatorSupport);

            if (parent.isPresent()) {
                data.registerData(parent.get());
            }

            data.registerData(IsOptional.TRUE);
            data.registerData(argumentContainer);
            data.registerData(argumentContainer.getName());
            data.registerData(argumentContainer.getTypes());
            try {
                data.registerData(instance);
            } catch (Exception e) {
                if (parent.isPresent())
                    throw new RuntimeException("Parent missing!");
            }


            translator = (Translator<?>) data.construct(argument.translator());
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }


        if (translator != null) {
            argumentBuilder.withPredicate(translator::isAcceptable);
            argumentBuilder.withConverter(translator::translate);
        }


        argumentBuilder.setOptional(argument.isOptional());
        argumentBuilder.setInfinite(argument.isArray());


        ArgumentSpec argumentSpec1 = argumentBuilder.build();

        if (argument.setFinal()) {
            argumentSpec1.getData().registerData(ReflectionCommandProcessor.PropSet.FINAL);
            argumentSpec1.getReferenceData().registerData(HolderGenericRepresentation.makeHold(GenericRepresentation.a(GenericRepresentation.class).build(), argumentContainer.getTypes()));
        }

        return argumentSpec1;

    }

    @Override
    public Order priority() {
        return Order.SEVENTH;
    }
}
