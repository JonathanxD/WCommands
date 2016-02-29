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
package com.github.jonathanxd.wcommands.factory;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by jonathan on 27/02/16.
 */
public class ArgumentBuilder<ID, T> {
    private ID id;
    private boolean optional = false;
    @SuppressWarnings("unchecked")
    private Function<String, T> converter = new All();
    private Supplier<Matchable<String>> checker = null;
    private Predicate<String> predicate = null;

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

    public ArgumentBuilder<ID, T> withConverter(Function<String, T> converter) {
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
    public ArgumentBuilder<ID, T> withTextPredicate(Predicate<Text> predicate) {
        this.predicate = t -> predicate.test(Text.of(t));
        return this;
    }

    public ArgumentBuilder<ID, T> withPredicate(Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }



    public ArgumentSpec<ID, T> build() {
        return new ArgumentSpec<>(id, checker, predicate, optional, converter);
    }

    private static class All<T> implements Function<Text, Object> {
        @Override
        public Object apply(Text text) {
            return text.getPlainString();
        }
    }
}
