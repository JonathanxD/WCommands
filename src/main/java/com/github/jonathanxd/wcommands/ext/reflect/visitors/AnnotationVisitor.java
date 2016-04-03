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
package com.github.jonathanxd.wcommands.ext.reflect.visitors;

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeHead;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Optional;

/**
 * Created by jonathan on 29/02/16.
 */
public abstract class AnnotationVisitor<T extends Annotation, C extends NamedContainer, V> implements Comparable<AnnotationVisitor<?, ?, ?>> {

    protected final Class<T> annotationClass;

    protected AnnotationVisitor(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * Called When the Annotation is found on the code
     *
     * You need to manually set the Containers!
     *
     * @param annotation Annotation
     * @param current    Current Container
     * @param last       Last Container
     * @param bridge     Element Bridge
     */
    public void visitElementAnnotation(T annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, TreeHead treeHead, RegistrationTicket<?> ticket) {

    }

    public void visitElementArguments(T annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge, ElementType location, RegistrationTicket<?> ticket) {

    }

    /**
     * Check the dependencies. Returns False to wait next register. This method is useful for
     * SubCommands because SubCommands require some command dependencies and SubCommands
     * dependencies.
     *
     * @param container Container
     * @param instance  Instance of the source
     * @param support   Visitor Support
     * @param commandList    Command List with all registered commands, modifying this list will not affect the WCommandCommon registration
     * @param parent    Parent caller
     * @return True to accept and call {@link #process(NamedContainer, InstanceContainer,
     * AnnotationVisitorSupport, CommandList, TranslatorSupport, ElementType, TreeHead, RegistrationTicket, Optional)}. or False to postpone the
     * process.
     */
    public boolean dependencyCheck(C container,
                                   InstanceContainer instance,
                                   AnnotationVisitorSupport support,
                                   CommandList commandList,
                                   TranslatorSupport translatorSupport,
                                   ElementType location,
                                   TreeHead treeHead,
                                   RegistrationTicket<?> ticket,
                                   Optional<NamedContainer> parent) {
        return true;
    }

    public abstract V process(C container,
                              InstanceContainer instance,
                              AnnotationVisitorSupport support,
                              CommandList commandList,
                              TranslatorSupport translatorSupport,
                              ElementType location,
                              TreeHead treeHead,
                              RegistrationTicket<?> ticket,
                              Optional<NamedContainer> parent);

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    @Override
    public int compareTo(AnnotationVisitor<?, ?, ?> o) {
        return this.getAnnotationClass() == o.getAnnotationClass() ? 0 : 1;
    }

    public Order priority() {
        return Order.FOURTH;
    }
}
