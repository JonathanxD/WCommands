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
package com.github.jonathanxd.wcommands.infos;

/**
 * Created by jonathan on 12/03/16.
 */
public class Information<T> implements Cloneable {

    private static final Information<?> EMPTY = new Information<>(null, null);

    private final InfoId id;
    private final T info;
    private final Description description;

    public Information(InfoId id, T info) {
        this(id, info, (Description) null);
    }

    protected Information(InfoId id, T info, Description description) {
        this.id = id;
        this.info = info;
        if (description == null)
            this.description = new Description(null);
        else
            this.description = description;
    }

    public Information(InfoId id, T info, String description) {
        this(id, info, new Description(description));
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

        return new Information<>(this.getId(), this.get(), this.getDescription().clone());
    }

    public boolean isPresent() {
        return (this.id != null && this.info != null) || (this != EMPTY);
    }

    @SuppressWarnings("unchecked")
    public static <T> Information<T> empty() {
        return (Information<T>) EMPTY;
    }
}
