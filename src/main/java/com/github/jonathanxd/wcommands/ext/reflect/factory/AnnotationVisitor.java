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
package com.github.jonathanxd.wcommands.ext.reflect.factory;

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.ext.reflect.factory.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.handler.InstanceContainer;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * Created by jonathan on 29/02/16.
 */
public abstract class AnnotationVisitor<T extends Annotation, C extends NamedContainer, V> implements Comparable<AnnotationVisitor<?, ?, ?>> {

    protected final Class<T> annotationClass;

    protected AnnotationVisitor(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void visitElementAnnotation(T annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge) {

    }

    public void visitElementArguments(T annotation, Container<NamedContainer> current, Container<NamedContainer> last, ElementBridge bridge) {

    }


    public abstract V process(C container,
                              InstanceContainer instance,
                              AnnotationVisitorSupport support,
                              WCommandCommon common,
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
