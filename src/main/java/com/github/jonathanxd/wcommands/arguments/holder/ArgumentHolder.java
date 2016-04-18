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
