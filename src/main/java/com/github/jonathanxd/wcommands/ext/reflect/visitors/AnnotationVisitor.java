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
package com.github.jonathanxd.wcommands.ext.reflect.visitors;

import com.github.jonathanxd.iutils.containers.Container;
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
     * @param container   Container
     * @param instance    Instance of the source
     * @param support     Visitor Support
     * @param commandList Command List with all registered commands, modifying this list will not
     *                    affect the WCommandCommon registration
     * @param parent      Parent caller
     * @return True to accept and call {@link #process(NamedContainer, InstanceContainer,
     * AnnotationVisitorSupport, CommandList, TranslatorSupport, ElementType, TreeHead,
     * RegistrationTicket, Optional)}. or False to postpone the process.
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
