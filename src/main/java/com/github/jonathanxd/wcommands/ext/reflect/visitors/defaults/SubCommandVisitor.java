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

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.CommandSpec;
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
import com.github.jonathanxd.wcommands.util.Require;
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
    public void visitElementAnnotation(SubCommand annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, TreeHead treeHead) {
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
    public CommandSpec process(TreeNamedContainer command, InstanceContainer instance, AnnotationVisitorSupport support, WCommandCommon common, ElementType location, TreeHead treeHead, Optional<NamedContainer> parent) {
        SubCommand subCommandAnnotation = (SubCommand) command.get();
        Command commandAnnotation = subCommandAnnotation.commandSpec();


        List<SingleNamedContainer> arguments = command.getArgumentContainers();

        Optional<CommandSpec> mainOpt = common.getCommand(subCommandAnnotation.value());

        if (mainOpt.isPresent()) {
            CommandSpec main = mainOpt.get();

            TreeNamedContainer tree = command.recreate(commandAnnotation);

            AnnotationVisitor<Command, TreeNamedContainer, CommandSpec> visitor = com.github.jonathanxd.iutils.optional.Require.require(support.<Command, TreeNamedContainer, CommandSpec>getVisitorFor(Command.class), "Cannot get @Command Visitor");

            CommandSpec spec = visitor.process(tree, instance, support, common, location, treeHead, parent);

            main.addSub(spec);


            return CommandSpec.empty();
        } else {
            throw new RuntimeException("Cannot find command '" + Arrays.toString(subCommandAnnotation.value()) + "'");
        }
    }

    @Override
    public boolean dependencyCheck(TreeNamedContainer container, InstanceContainer instance, AnnotationVisitorSupport support, WCommandCommon common, ElementType location, TreeHead treeHead, Optional<NamedContainer> parent) {
        SubCommand subCommandAnnotation = (SubCommand) container.get();

        return common.getCommand(subCommandAnnotation.value()).isPresent();
    }

    @Override
    public Order priority() {
        return Order.FIFTH;
    }
}
