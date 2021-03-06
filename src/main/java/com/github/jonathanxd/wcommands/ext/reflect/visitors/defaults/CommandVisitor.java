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
import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeHead;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.Require;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 29/02/16.
 */
public class CommandVisitor extends AnnotationVisitor<Command, TreeNamedContainer, CommandSpec> {

    public CommandVisitor(Class<Command> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void visitElementAnnotation(Command annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket) {
        String name = annotation.name().trim().isEmpty() ? bridge.getName() : annotation.name();
        Common.visit(name, annotation, current, last, bridge, location, treeHead);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandSpec process(TreeNamedContainer command, InstanceContainer instance, AnnotationVisitorSupport support, CommandList common, TranslatorSupport translatorSupport, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket, Optional<NamedContainer> parent) {
        Command commandAnnotation = (Command) command.get();
        List<SingleNamedContainer> arguments = command.getArgumentContainers();


        CommandBuilder<CommandHolder> commandBuilder = CommandBuilder.builder();

        commandBuilder.withName(Text.of(command.getName()));
        commandBuilder.withPrefix(commandAnnotation.prefix());
        commandBuilder.withSuffix(commandAnnotation.suffix());
        commandBuilder.withDescription(commandAnnotation.desc());
        commandBuilder.withIsOptional(commandAnnotation.isOptional());

        Handler<CommandHolder> handler = null;

        try {
            ExtraData data = new ExtraData();

            data.addData(null, command);
            data.addData(null, command.getBridge());
            data.addData(null, command.getName());

            data.addData(null, instance);

            handler = (Handler<CommandHolder>) data.construct(commandAnnotation.handler());
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }

        commandBuilder.withHandler(handler);

        for (SingleNamedContainer argumentContainer : arguments) {

            // ARGUMENTS

            Class<? extends Annotation> cla = argumentContainer.get().annotationType();

            Optional<AnnotationVisitor<Annotation, SingleNamedContainer, ArgumentSpec<?, ?>>> visitorOpt = support.<Annotation, SingleNamedContainer, ArgumentSpec<?, ?>>getVisitorFor(cla);

            if (visitorOpt.isPresent()) {

                // ?
                ArgumentSpec argSpec = visitorOpt.get().process(argumentContainer, instance, support, common, translatorSupport, location, treeHead, ticket, Optional.of(command));
                commandBuilder.withArgument(argSpec);
            }

        }

        command.getChild().forEach(commandContainer -> commandBuilder.withChild(process(commandContainer, instance, support, common, translatorSupport, location, treeHead, ticket, parent)));

        CommandSpec cmd = commandBuilder.build();

        return cmd;
    }

    public static final class Common {
        public static void visit(String nameResolved, Annotation annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, TreeHead treeHead) {

            TreeNamedContainer treeNamedContainer;

            if (!current.isPresent()) {

                current.set((treeNamedContainer = new TreeNamedContainer(nameResolved, annotation, bridge)));
                last.set(current.get());

            } else {
                last.set((treeNamedContainer = new TreeNamedContainer(nameResolved, annotation, bridge)));

                TreeNamedContainer container = Require.require(current.get(), TreeNamedContainer.class);
                TreeNamedContainer lastTree = Require.require(last.get(), TreeNamedContainer.class);

                container.getChild().add(lastTree);
            }

            if (location == ElementType.TYPE) {
                treeHead.addHead(treeNamedContainer);
            }
        }
    }

}
