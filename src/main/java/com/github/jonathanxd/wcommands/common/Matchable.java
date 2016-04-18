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
package com.github.jonathanxd.wcommands.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

/**
 * Created by jonathan on 24/02/16.
 */
public interface Matchable<T> {

    boolean matches(T other);

    default boolean matchesIgnoreCase(T other) {
        return matches(other);
    }

    default boolean matches(T other, Function<T, T> transformer) {
        return matches(transformer.apply(other));
    }

    default boolean matches(T other, Function<T, T>[] transformers) {
        for(Function<T, T> function : transformers) {
            if(matches(function.apply(other))) {
                return true;
            }
        }
        return false;
    }


    default boolean matchesAny(T[] others) {
        return matchesAny(Arrays.asList(others));
    }

    default boolean matchesAny(Collection<T> others) {
        return matchesAny(others, t -> t);
    }

    default boolean matchesAny(Collection<T> others, Function<T, T> transformer) {
        for(T t : others) {
            if(matches(transformer.apply(t))) {
                return true;
            }
        }
        return false;
    }

    Matchable<?> ACCEPTANY = new AcceptAny<>();

    @SuppressWarnings("unchecked")
    static <T> Matchable<T> acceptAny() {
        return (Matchable<T>) ACCEPTANY;
    }

    class AcceptAny<T> implements Matchable<T> {

        @Override
        public boolean matches(T other) {
            return true;
        }

        @Override
        public boolean matchesIgnoreCase(T other) {
            return true;
        }
    }
}
