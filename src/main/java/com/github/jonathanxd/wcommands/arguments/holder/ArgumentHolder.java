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

import com.github.jonathanxd.wcommands.arguments.Argument;
import com.github.jonathanxd.wcommands.text.Text;

/**
 * Created by jonathan on 26/02/16.
 */

/**
 * ArgumentHolder
 *
 * Holds a Parsed Argument.
 *
 * @param <ID> ID of argument
 * @param <T>  T type of argument value
 */
public class ArgumentHolder<ID, T> {

    /**
     * Text representation of argument input value.
     */
    private final Text value;

    /**
     * Argument instance
     */
    private final Argument<ID, T> argument;

    /**
     * Argument is present? (if Text.plain != null)
     */
    private final boolean present;

    public ArgumentHolder(Text value, Argument<ID, T> argument) {
        this.value = value;
        this.argument = argument;
        this.present = value.getPlainString() != null;
    }

    /**
     * Get Argument input string
     *
     * @return Text representation of input string
     */
    public Text getValue() {
        return value;
    }

    /**
     * Involved argument
     *
     * @return Involved
     * @see Argument
     */
    public Argument<ID, T> getArgument() {
        return argument;
    }

    /**
     * Convert from Text representation {@link #getValue()} to Object representation
     *
     * @return Object representation
     */
    public T convertValue() {
        return argument.getConverter().apply(value);
    }

    /**
     * True if the argument is present, else otherwise
     *
     * @return True if the argument is present, false otherwise
     */
    public boolean isPresent() {
        return present;
    }

    @Override
    public String toString() {
        return "ArgumentHolder[value=" + value + ", argument=" + argument + ", isPresent=" + present + "]";
    }
}
