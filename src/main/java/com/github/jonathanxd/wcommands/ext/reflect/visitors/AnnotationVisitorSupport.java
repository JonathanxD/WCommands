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

import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * Created by jonathan on 29/02/16.
 */
public interface AnnotationVisitorSupport {

    boolean registerVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor);

    boolean overrideVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor);

    boolean removeVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor);

    boolean removeVisitor(Class<?> annotationType);

    <T extends Annotation, C extends NamedContainer, R> Optional<AnnotationVisitor<T, C, R>> getVisitorFor(Class<? extends Annotation> clazz);

}
