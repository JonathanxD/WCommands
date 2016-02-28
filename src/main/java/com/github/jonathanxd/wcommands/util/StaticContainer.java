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

import com.github.jonathanxd.iutils.extra.BaseContainer;
import com.github.jonathanxd.iutils.extra.Container;

import java.util.function.BiFunction;

/**
 * Created by jonathan on 27/02/16.
 */
public class StaticContainer<T> extends Container<T> {

    public StaticContainer(Container<T> container) {
        super(container.get());
    }


    @Override
    public void apply(T value) {
        throw new UnsupportedOperationException("Immutable container!");
    }

    @Override
    public void set(T value) {
        throw new UnsupportedOperationException("Immutable container!");
    }

    @Override
    public void setApplier(BiFunction<BaseContainer<T>, T, T> applier) {
        throw new UnsupportedOperationException("Immutable container!");
    }

    @Override
    public void setValue(T value) {
        throw new UnsupportedOperationException("Immutable container!");
    }

    @Override
    public boolean isMutable() {
        return false;
    }
}
