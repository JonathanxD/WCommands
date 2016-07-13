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
package com.github.jonathanxd.wcommands.infos;

import com.github.jonathanxd.iutils.object.GenericRepresentation;

/**
 * Created by jonathan on 12/03/16.
 */
public class Information<T> implements Cloneable {

    private static final Information<?> EMPTY = new Information<>(null, null, null);

    private final InfoId id;
    private final GenericRepresentation<?> representation;
    private final T info;
    private final Description description;

    public Information(InfoId id, T info, GenericRepresentation<?> representation) {
        this(id, representation, info, (Description) null);
    }

    protected Information(InfoId id, GenericRepresentation<?> representation, T info, Description description) {
        this.id = id;
        this.representation = representation;
        this.info = info;
        if (description == null)
            this.description = new Description(null);
        else
            this.description = description;
    }

    public Information(InfoId id, T info, String description, GenericRepresentation<?> representation) {
        this(id, representation, info, new Description(description));
    }

    @SuppressWarnings("unchecked")
    public static <T> Information<T> empty() {
        return (Information<T>) EMPTY;
    }

    public GenericRepresentation<?> getRepresentation() {
        return representation;
    }

    @SuppressWarnings("unchecked")
    public InfoId getId() {
        return id;
    }

    public Description getDescription() {
        return description;
    }

    public T get() {
        return info;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Information<T> clone() {

        return new Information<>(this.getId(), representation, this.get(), this.getDescription().clone());
    }

    public boolean isPresent() {
        return (this.id != null && this.info != null) || (this != EMPTY);
    }

    @Override
    public String toString() {

        return "id=(" + getId() + ")";
    }
}
