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
package com.github.jonathanxd.wcommands.ext.reflect.processor;

import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitors;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.Optional;

/**
 * Created by jonathan on 11/03/16.
 */
public class PriorityComparator implements Comparator<ElementBridge> {

    private final AnnotationVisitors visitors;

    public PriorityComparator(AnnotationVisitors visitors) {
        this.visitors = visitors;

    }

    @Override
    public int compare(ElementBridge o1, ElementBridge o2) {

        Order firstOrder = biggerOrder(o1.getDeclaredAnnotations());
        Order secondOrder = biggerOrder(o2.getDeclaredAnnotations());

        if(o1.getMember() == o2.getMember())
            return 0;

        if(o1.hasAlternativePriority())
            firstOrder = o1.getPriority();

        if(o2.hasAlternativePriority())
            secondOrder = o2.getPriority();

        int comp = firstOrder.compareTo(secondOrder);

        return comp != 0 ? comp : comp + 1;
    }

    private Order biggerOrder(Annotation[] annotations) {
        Order theOrder = null;

        for(Annotation annotation : annotations) {
            Optional<AnnotationVisitor<Annotation, NamedContainer, Object>> visitorOptional = visitors.getFor(annotation.annotationType());

            if(visitorOptional.isPresent()) {

                Order visitorOrder = visitorOptional.get().priority();
                if(theOrder == null || theOrder.ordinal() < visitorOrder.ordinal()) {
                    theOrder = visitorOrder;
                }
            }
        }

        return theOrder != null ? theOrder : Order.FIRST;
    }
}
