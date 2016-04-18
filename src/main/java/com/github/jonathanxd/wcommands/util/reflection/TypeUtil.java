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

    public static Reference<?> toReference(Type param) {
        if(param instanceof ParameterizedType) {
            return toReference((ParameterizedType) param);
        }

        return Reference.aEnd(from(param));
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
