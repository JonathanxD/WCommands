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
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeHead;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 29/02/16.
 */
public class SubCommandVisitor extends AnnotationVisitor<SubCommand, TreeNamedContainer, CommandSpec> {

    public SubCommandVisitor(Class<SubCommand> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void visitElementAnnotation(SubCommand annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket) {
        Command commandAnnotation = annotation.commandSpec();

        String name = commandAnnotation.name().trim().isEmpty() ? bridge.getName() : commandAnnotation.name();

        CommandVisitor.Common.visit(name, annotation, current, last, bridge, location, treeHead);

        /*if (!current.isPresent()) {

            current.set(new TreeNamedContainer(name, annotation, bridge));
            last.set(current.get());

        } else {
            last.set(new TreeNamedContainer(name, annotation, bridge));

            TreeNamedContainer container = Require.require(current.get(), TreeNamedContainer.class);
            TreeNamedContainer lastTree = Require.require(last.get(), TreeNamedContainer.class);

            container.getChild().add(lastTree);
        }*/

    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandSpec process(TreeNamedContainer command, InstanceContainer instance, AnnotationVisitorSupport support, CommandList common, TranslatorSupport translatorSupport, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket, Optional<NamedContainer> parent) {
        SubCommand subCommandAnnotation = (SubCommand) command.get();
        Command commandAnnotation = subCommandAnnotation.commandSpec();


        List<SingleNamedContainer> arguments = command.getArgumentContainers();

        Optional<CommandSpec> mainOpt = common.getCommand(subCommandAnnotation.value());

        if (mainOpt.isPresent()) {
            CommandSpec main = mainOpt.get();

            TreeNamedContainer tree = command.recreate(commandAnnotation);

            AnnotationVisitor<Command, TreeNamedContainer, CommandSpec> visitor = com.github.jonathanxd.iutils.optional.Require.require(support.<Command, TreeNamedContainer, CommandSpec>getVisitorFor(Command.class), "Cannot get @Command Visitor");

            CommandSpec spec = visitor.process(tree, instance, support, common, translatorSupport, location, treeHead, ticket, parent);

            main.addSub(spec, ticket);


            return CommandSpec.empty();
        } else {
            throw new RuntimeException("Cannot find command '" + Arrays.toString(subCommandAnnotation.value()) + "'");
        }
    }

    @Override
    public boolean dependencyCheck(TreeNamedContainer container, InstanceContainer instance, AnnotationVisitorSupport support, CommandList commandList, TranslatorSupport translatorSupport, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket, Optional<NamedContainer> parent) {
        SubCommand subCommandAnnotation = (SubCommand) container.get();

        return commandList.getCommand(subCommandAnnotation.value()).isPresent();
    }

    @Override
    public Order priority() {
        return Order.FIFTH;
    }
}
