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
        for(T element : c) {
            if(predicate.test(element)) {
                return Optional.of(map.apply(element));
            }
        }

        return Optional.empty();
    }

    public static <T> Optional<T> return_(Collection<? extends T> c, Predicate<T> predicate) {
        for(T element : c) {
            if(predicate.test(element)) {
                return Optional.of(element);
            }
        }

        return Optional.empty();
    }

}
