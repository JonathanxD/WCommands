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

import java.util.List;
import java.util.ListIterator;

/**
 * Created by jonathan on 26/02/16.
 */
public class StaticListIterator<E> implements ListIterator<E> {

    private final List<E> list;
    int index = -1;

    public StaticListIterator(List<E> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return index + 1 < list.size();
    }

    @Override
    public E next() {
        return list.get(++index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean hasPrevious() {
        return index - 1 > -1;
    }

    @Override
    public E previous() {
        return list.get(--index);
    }

    @Override
    public int nextIndex() {
        return index+1;
    }

    @Override
    public int previousIndex() {
        return index-1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(E e) {
        list.set(index, e);
    }

    @Override
    public void add(E e) {
        list.add(index, e);
    }

    public StaticListIterator<E> copy() {
        StaticListIterator<E> staticListIterator = new StaticListIterator<>(list);
        staticListIterator.setIndex(this.getIndex());
        return staticListIterator;
    }
}
