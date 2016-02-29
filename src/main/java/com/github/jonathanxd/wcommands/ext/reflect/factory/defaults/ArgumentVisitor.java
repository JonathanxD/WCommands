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
package com.github.jonathanxd.wcommands.ext.reflect.factory.defaults;

import com.github.jonathanxd.iutils.data.DataProvider;
import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.IsOptional;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.factory.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.factory.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.factory.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.factory.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.factory.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.factory.ArgumentBuilder;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.util.Require;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

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
                Reference.class,
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
    public void visitElementArguments(Argument annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge) {
        visitElementAnnotation(annotation, current, last, bridge);
    }

    @Override
    public void visitElementAnnotation(Argument annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge) {
        if (last.isPresent()) {

            String name = annotation.id().trim().isEmpty() ? bridge.getName() : annotation.id();

            if (annotation.isOptional()) {

                if (bridge.getType() == Optional.class && (annotation.type() == null || annotation.type() == Argument.PR.class) && bridge.directReference() == null) {
                    throw new RuntimeException("Cannot handle Optional, impossible to determine the Type, use: 'Argument.type()'!");
                } else {
                    if (bridge.directReference() == null) {
                        if (annotation.type() != Argument.PR.class) {
                            bridge = new ElementBridge(bridge.getMember(), Reference.aEnd(annotation.type()));
                        }
                    }
                }
            }

            TreeNamedContainer treeNamedContainer = Require.require(last.get(), TreeNamedContainer.class);

            treeNamedContainer.getArgumentContainers().add(new SingleNamedContainer(name, annotation, bridge));
        }
    }


    @Override
    public ArgumentSpec<?, ?> process(SingleNamedContainer argumentContainer, InstanceContainer instance, AnnotationVisitorSupport support, WCommandCommon common, Optional<NamedContainer> parent) {
        Argument argument = (Argument) argumentContainer.get();

        ArgumentBuilder<String, Object> argumentBuilder = ArgumentBuilder.builder();

        argumentBuilder.withId(argumentContainer.getName());


        ExtraData data = new ExtraData();

        Translator<?> translator = null;
        try {
            data.registerData((TranslatorSupport) common);

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

        ArgumentSpec argumentSpec1 = argumentBuilder.build();

        if (argument.setFinal()) {
            argumentSpec1.getData().registerData(ReflectionCommandProcessor.Set.FINAL);
        }

        return argumentSpec1;

    }

    @Override
    public Order priority() {
        return Order.SEVENTH;
    }
}
