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
package com.github.jonathanxd.wcommands.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jonathan on 24/02/16.
 */
public class Functions {

    public static <T, R> Collection<R> foreachAsFunction(Collection<? extends T> c, Function<T, R> function) {

        Collection<R> collection = new ArrayList<>();

        for (T element : c) {
            collection.add(function.apply(element));
        }

        return collection;
    }

    public static <T> Optional<T> first(Predicate<T> predicate, Collection<? extends T> c) {
        for (T element : c) {
            if (predicate.test(element))
                return Optional.of(element);
        }
        return Optional.empty();
    }

    public static <T, R> Optional<R> return_(Collection<? extends T> c, Predicate<T> predicate, Function<T, R> map) {
        for (T element : c) {
            if (predicate.test(element)) {
                return Optional.of(map.apply(element));
            }
        }

        return Optional.empty();
    }

    public static <T> Optional<T> return_(Collection<? extends T> c, Predicate<T> predicate) {
        for (T element : c) {
            if (predicate.test(element)) {
                return Optional.of(element);
            }
        }

        return Optional.empty();
    }

}
