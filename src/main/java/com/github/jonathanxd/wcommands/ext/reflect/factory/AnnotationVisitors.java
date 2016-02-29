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

import com.github.jonathanxd.wcommands.ext.reflect.factory.containers.NamedContainer;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Created by jonathan on 29/02/16.
 */
public class AnnotationVisitors extends TreeSet<AnnotationVisitor<?, ?, ?>> {

    public AnnotationVisitors() {
        super((o1, o2) -> {
            if(o1.getAnnotationClass() == o2.getAnnotationClass()) {
                return 0;
            }else{
                int compare =  o1.priority().compareTo(o2.priority());
                return compare != 0 ? compare : compare + 1;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation, C extends NamedContainer, R> Optional<AnnotationVisitor<T, C, R>> getFor(Class<? extends Annotation> clazz) {
        Optional<AnnotationVisitor<?, ?, ?>> factory = this.stream().filter(c -> c.getAnnotationClass() == clazz).findFirst();
        if(factory.isPresent()) {
            return Optional.of((AnnotationVisitor<T, C, R>) factory.get());
        } else
            return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public Optional<AnnotationVisitor<?, ?, ?>> getGenericFor(Class<? extends Annotation> clazz) {
        return this.stream().filter(c -> c.getAnnotationClass() == clazz).findFirst();
    }

}
