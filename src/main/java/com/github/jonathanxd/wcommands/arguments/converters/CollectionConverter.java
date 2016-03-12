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
package com.github.jonathanxd.wcommands.arguments.converters;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Created by jonathan on 12/03/16.
 */
public class CollectionConverter<T> implements Function<String, T> {

    private final Collection<T> collection;
    private final BiPredicate<T, String> predicate;

    public CollectionConverter(Collection<T> collection, BiPredicate<T, String> predicate) {
        this.collection = collection;
        this.predicate = predicate;
    }

    @Override
    public T apply(String s) {

        for (T element : collection) {
            if (predicate.test(element, s)) {
                return element;
            }
        }

        throw new IllegalStateException("Cannot find element '" + s + "' in collection '" + collection + "'");
    }
}
