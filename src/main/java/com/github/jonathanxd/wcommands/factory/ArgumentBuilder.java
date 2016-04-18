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
package com.github.jonathanxd.wcommands.factory;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 27/02/16.
 */
public class ArgumentBuilder<ID, T> {
    private ID id;
    private boolean optional = false;
    private boolean isInfinite = false;
    @SuppressWarnings("unchecked")
    private Function<List<String>, T> converter = new All();
    private Supplier<Matchable<String>> checker = null;
    private Predicate<List<String>> predicate = null;

    private ArgumentBuilder() {
    }

    public static <ID, T> ArgumentBuilder<ID, T> builder() {
        return new ArgumentBuilder<>();
    }

    public ArgumentBuilder<ID, T> withId(ID id) {
        this.id = id;
        return this;
    }

    public ArgumentBuilder<ID, T> setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public ArgumentBuilder<ID, T> setInfinite(boolean infinite) {
        isInfinite = infinite;
        return this;
    }

    public ArgumentBuilder<ID, T> withConverter(Function<List<String>, T> converter) {
        this.converter = converter;
        return this;
    }

    public ArgumentBuilder<ID, T> withChecker(Supplier<Matchable<String>> checker) {
        this.checker = checker;
        return this;
    }

    /**
     * COMPATIBILITY
     * @param predicate COMPATIBILITY
     * @return COMPATIBILITY
     * @see #withPredicate(Predicate)
     */
    public ArgumentBuilder<ID, T> withTextPredicate(Predicate<List<Text>> predicate) {
        this.predicate = t -> predicate.test(t.stream().map(Text::of).collect(Collectors.toList()));
        return this;
    }

    public ArgumentBuilder<ID, T> withPredicate(Predicate<List<String>> predicate) {
        this.predicate = predicate;
        return this;
    }



    public ArgumentSpec<ID, T> build() {
        return new ArgumentSpec<>(id, isInfinite, checker, predicate, optional, converter);
    }

    private static class All<T> implements Function<List<String>, T> {
        @SuppressWarnings("unchecked")
        @Override
        public T apply(List<String> text) {
            return (T) text.get(0);
        }
    }
}
