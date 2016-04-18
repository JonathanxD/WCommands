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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Created by jonathan on 18/04/16.
 */
public class RemovingIterator<E> implements Iterator<E> {

    private final Supplier<Iterator<E>> iteratorSupplier;
    private final List<E> elementList = new ArrayList<>();
    private Iterator<E> currentIter;
    private E current = null;

    public RemovingIterator(Collection<E> collection) {
        this.elementList.addAll(collection);
        this.iteratorSupplier = elementList::iterator;
        this.currentIter = iteratorSupplier.get();
    }

    @Override
    public boolean hasNext() {
        if(currentIter.hasNext())
            return currentIter.hasNext();
        else
            if (!elementList.isEmpty()) {
                currentIter = iteratorSupplier.get();
                return true;
            }

        return currentIter.hasNext();
    }

    public boolean originalHasNext() {
        return currentIter.hasNext();
    }

    public E realNext() {
        return (current = currentIter.next());
    }

    @Override
    public E next() {

        if(hasNext())
            return (current = currentIter.next());

        throw new NoSuchElementException("No more elements!");
    }

    public E getCurrent() {
        return current;
    }

    public int countRemainingElements() {
        return elementList.size();
    }

    public List<E> getElementList() {
        return elementList;
    }

    @Override
    public void remove() {
        currentIter.remove();
        hasNext();
    }

}
