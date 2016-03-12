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
package com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults;

import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.Require;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
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
    public void visitElementAnnotation(Command annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge) {
        String name = annotation.name().trim().isEmpty() ? bridge.getName() : annotation.name();

        if (!current.isPresent()) {

            current.set(new TreeNamedContainer(name, annotation, bridge));
            last.set(current.get());

        } else {
            last.set(new TreeNamedContainer(name, annotation, bridge));

            TreeNamedContainer container = Require.require(current.get(), TreeNamedContainer.class);
            TreeNamedContainer lastTree = Require.require(last.get(), TreeNamedContainer.class);

            container.getChild().add(lastTree);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandSpec process(TreeNamedContainer command, InstanceContainer instance, AnnotationVisitorSupport support, WCommandCommon common, Optional<NamedContainer> parent) {
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

            data.registerData(command);
            data.registerData(command.getBridge());
            data.registerData(command.getName());

            data.registerData(instance);

            handler = (Handler<CommandHolder>) data.construct(commandAnnotation.handler());
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }

        commandBuilder.withHandler(handler);

        for (SingleNamedContainer argumentContainer : arguments) {

            // ARGUMENTS

            Class<? extends Annotation> cla = argumentContainer.get().annotationType();

            Optional<AnnotationVisitor<Annotation, SingleNamedContainer, ArgumentSpec<?, ?>>> visitorOpt = support.<Annotation, SingleNamedContainer, ArgumentSpec<?, ?>>getVisitorFor(cla);

            if(visitorOpt.isPresent()) {

                // ?
                ArgumentSpec argSpec = visitorOpt.get().process(argumentContainer, instance, support, common, Optional.of(command));
                commandBuilder.withArgument(argSpec);
            }

        }

        command.getChild().forEach(commandContainer -> commandBuilder.withChild(process(commandContainer, instance, support, common, parent)));

        CommandSpec cmd = commandBuilder.build();

        return cmd;
    }
}
