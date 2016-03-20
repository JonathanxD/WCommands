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
package com.github.jonathanxd.wcommands.arguments.holder;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.util.reflection.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jonathan on 26/02/16.
 */

/**
 * ArgumentHolder
 *
 * Holds a Parsed ArgumentSpec.
 *
 * @param <ID> ID of argumentSpec
 * @param <T>  T type of argumentSpec value
 */
public class ArgumentHolder<ID, T> {

    /**
     * String representation of argumentSpec input value.
     */
    private final List<String> values;

    /**
     * ArgumentSpec instance
     */
    private final ArgumentSpec<ID, T> argumentSpec;

    public ArgumentHolder(List<String> values, ArgumentSpec<ID, T> argumentSpec) {
        this.values = values == null ? null : new ArrayList<>(values);
        this.argumentSpec = argumentSpec;
    }

    /**
     * Get ArgumentSpec input string
     *
     * @return Text representation of input string
     */
    public String getFirstValue() {
        return !values.isEmpty() ? values.get(0) : null;
    }

    public List<String> getValues() {
        return values;
    }

    /**
     * Set ArgumentSpec values (input strings)
     * @param values Values
     */
    public void setValues(String... values) {
        this.values.clear();
        this.values.addAll(Arrays.asList(values));
    }

    public void setValues(List<String> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    /**
     * Involved argumentSpec
     *
     * @return Involved
     * @see ArgumentSpec
     */
    public ArgumentSpec<ID, T> getArgumentSpec() {
        return argumentSpec;
    }

    /**
     * Convert from Text List representation {@link #getValues()} to Object representation
     *
     * @return Object representation
     */
    public T convertValue() {
        return argumentSpec.getConverter().apply(getValues());
    }

    /**
     * True if the argumentSpec is present, else otherwise
     *
     * @return True if the argumentSpec is present, false otherwise
     */
    public boolean isPresent() {
        return values != null && !values.isEmpty();
    }

    @Override
    public String toString() {
        return "ArgumentHolder["+ToString.toString(this)+"]";
        //return "ArgumentHolder[values={" + values + "}, argumentSpec=" + argumentSpec + ", isPresent=" + isPresent() + "]";
    }
}
