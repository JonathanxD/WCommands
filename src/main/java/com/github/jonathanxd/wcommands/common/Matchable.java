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
