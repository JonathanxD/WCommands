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

        if(other == null || other.isEmpty())
            return true;

        for (String tag : tags) {
            if (tag.equals(other))
                return true;
        }

        return false;
    }

    @Override
    public boolean matchesIgnoreCase(String other) {

        if(other == null || other.isEmpty())
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
                (this.getTags() == null && other.tags == null) || Arrays.deepEquals(this.getTags(), other.getTags())
        ) && this.getIdentification().equals(other.getIdentification());

    }

    @Override
    public int hashCode() {

        int result = 1;

        result = 31 * result + (tags == null ? 0 : Arrays.deepHashCode(tags));
        result = 31 * result + (identification == null ? 0 : identification.hashCode());

        return result;
    }
}
