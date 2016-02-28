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
     * Text representation of argumentSpec input value.
     */
    private final Text value;

    /**
     * ArgumentSpec instance
     */
    private final ArgumentSpec<ID, T> argumentSpec;

    /**
     * ArgumentSpec is present? (if Text.plain != null)
     */
    private final boolean present;

    public ArgumentHolder(Text value, ArgumentSpec<ID, T> argumentSpec) {
        this.value = value;
        this.argumentSpec = argumentSpec;
        this.present = value.getPlainString() != null;
    }

    /**
     * Get ArgumentSpec input string
     *
     * @return Text representation of input string
     */
    public Text getValue() {
        return value;
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
        return argumentSpec.getConverter().apply(value);
    }

    /**
     * True if the argumentSpec is present, else otherwise
     *
     * @return True if the argumentSpec is present, false otherwise
     */
    public boolean isPresent() {
        return present;
    }

    @Override
    public String toString() {
        return "ArgumentHolder[value=" + value + ", argumentSpec=" + argumentSpec + ", isPresent=" + present + "]";
    }
}
