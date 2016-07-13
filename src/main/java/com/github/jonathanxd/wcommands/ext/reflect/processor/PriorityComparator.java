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

        if (o1.getMember() == o2.getMember())
            return 0;

        if (o1.hasAlternativePriority())
            firstOrder = o1.getPriority();

        if (o2.hasAlternativePriority())
            secondOrder = o2.getPriority();

        int comp = firstOrder.compareTo(secondOrder);

        return comp != 0 ? comp : comp + 1;
    }

    private Order biggerOrder(Annotation[] annotations) {
        Order theOrder = null;

        for (Annotation annotation : annotations) {
            Optional<AnnotationVisitor<Annotation, NamedContainer, Object>> visitorOptional = visitors.getFor(annotation.annotationType());

            if (visitorOptional.isPresent()) {

                Order visitorOrder = visitorOptional.get().priority();
                if (theOrder == null || theOrder.ordinal() < visitorOrder.ordinal()) {
                    theOrder = visitorOrder;
                }
            }
        }

        return theOrder != null ? theOrder : Order.FIRST;
    }
}
