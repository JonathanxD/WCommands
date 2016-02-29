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
import com.github.jonathanxd.wcommands.text.Text;

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
    private String value;

    /**
     * ArgumentSpec instance
     */
    private final ArgumentSpec<ID, T> argumentSpec;

    public ArgumentHolder(String value, ArgumentSpec<ID, T> argumentSpec) {
        this.value = value;
        this.argumentSpec = argumentSpec;
    }

    /**
     * Get ArgumentSpec input string
     *
     * @return Text representation of input string
     */
    public String getValue() {
        return value;
    }


    /**
     * Set ArgumentSpec value (input string)
     * @param value Value
     */
    public void setValue(String value) {
        this.value = value;
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
     * Convert from Text representation {@link #getValue()} to Object representation
     *
     * @return Object representation
     */
    public T convertValue() {
        return argumentSpec.getConverter().apply(getValue());
    }

    /**
     * True if the argumentSpec is present, else otherwise
     *
     * @return True if the argumentSpec is present, false otherwise
     */
    public boolean isPresent() {
        return value != null;
    }

    @Override
    public String toString() {
        return "ArgumentHolder[value=" + value + ", argumentSpec=" + argumentSpec + ", isPresent=" + isPresent() + "]";
    }
}
