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

import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Created by jonathan on 29/02/16.
 */
public class AnnotationVisitors extends TreeSet<AnnotationVisitor<?, ?, ?>> {

    public AnnotationVisitors() {
        super((o1, o2) -> {
            if (o1.getAnnotationClass() == o2.getAnnotationClass()) {
                return 0;
            } else {
                int compare = o1.priority().compareTo(o2.priority());
                return compare != 0 ? compare : compare + 1;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation, C extends NamedContainer, R> Optional<AnnotationVisitor<T, C, R>> getFor(Class<? extends Annotation> clazz) {
        Optional<AnnotationVisitor<?, ?, ?>> factory = this.stream().filter(c -> c.getAnnotationClass() == clazz).findFirst();
        if (factory.isPresent()) {
            return Optional.of((AnnotationVisitor<T, C, R>) factory.get());
        } else
            return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public Optional<AnnotationVisitor<?, ?, ?>> getGenericFor(Class<? extends Annotation> clazz) {
        return this.stream().filter(c -> c.getAnnotationClass() == clazz).findFirst();
    }

}
