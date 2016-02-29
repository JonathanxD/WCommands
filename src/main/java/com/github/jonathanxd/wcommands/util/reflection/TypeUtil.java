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
package com.github.jonathanxd.wcommands.util.reflection;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.iutils.object.ReferenceBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jonathan on 28/02/16.
 */
public class TypeUtil {

    public static Class<?>[] typesAsClass(Type[] parTypes) throws ClassNotFoundException {
        Class<?>[] typesClass = new Class<?>[parTypes.length];

        for(int x = 0; x < parTypes.length; ++x) {
            typesClass[x] = Class.forName(parTypes[x].getTypeName());
        }

        return typesClass;
    }

    public static <T> void deepTypes(Type[] types, Predicate<Type> continueIf, Function<Type, T> converter, Consumer<T> typeConsumer) {
        for(Type type : types) {
            if(continueIf.test(type)) {
                T converted = converter.apply(type);
                typeConsumer.accept(converted);
            }
        }
    }

    public static Reference<?> toReference(ParameterizedType param) {
        ReferenceBuilder referenceBuilder = Reference.a(from(param.getRawType()));
        for(Type type : param.getActualTypeArguments()) {
            if(!(type instanceof ParameterizedType)) {
                referenceBuilder.of(from(type));
            }else{
                ParameterizedType parameterizedType = (ParameterizedType) type;
                referenceBuilder.of(toReference(parameterizedType));
            }
        }
        return referenceBuilder.build();
    }

    public static Class<?> from(Type t) {
        try {
            return Class.forName(t.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
