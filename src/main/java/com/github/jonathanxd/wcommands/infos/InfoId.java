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

import com.github.jonathanxd.wcommands.common.Matchable;

import java.util.Arrays;

/**
 * Created by jonathan on 14/03/16.
 */
public class InfoId implements Matchable<String> {

    private final String[] tags;
    private final Class<?> identification;

    public InfoId(String[] tags, Class<?> identification) {
        this.tags = tags;
        this.identification = identification;
    }

    public InfoId(String tags, Class<?> identification) {
        this(new String[]{tags}, identification);
    }

    public String[] getTags() {
        return tags;
    }

    public Class<?> getIdentification() {
        return identification;
    }

    public boolean matchRequirements(String[] tags, Class<?> type) {
        return (type == null || type.isAssignableFrom(this.getIdentification())) && this.matchesAny(tags);
    }

    public boolean matchRequirements(String tag, Class<?> type) {
        return (type == null || type.isAssignableFrom(this.getIdentification())) && this.matches(tag);
    }

    @Override
    public boolean matches(String other) {

        if (other == null || other.isEmpty())
            return true;

        for (String tag : tags) {
            if (tag.equals(other))
                return true;
        }

        return false;
    }

    @Override
    public boolean matchesIgnoreCase(String other) {

        if (other == null || other.isEmpty())
            return true;

        for (String tag : tags) {
            if (tag.equalsIgnoreCase(other))
                return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof InfoId))
            return false;

        InfoId other = (InfoId) obj;

        return (
                (other.getTags().length == 0 || other.getTags()[0].isEmpty() || (this.getTags() == null && other.tags == null) || Arrays.deepEquals(this.getTags(), other.getTags()))
        ) && this.getIdentification().equals(other.getIdentification());

    }

    @Override
    public int hashCode() {

        int result = 1;

        result = 31 * result + (tags == null ? 0 : Arrays.deepHashCode(tags));
        result = 31 * result + (identification == null ? 0 : identification.hashCode());

        return result;
    }

    @Override
    public String toString() {

        return "tags=(" + Arrays.toString(tags) + "), identification=(" + getIdentification().getSimpleName() + ")";

    }
}
