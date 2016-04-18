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
