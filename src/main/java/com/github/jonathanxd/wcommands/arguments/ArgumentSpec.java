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
package com.github.jonathanxd.wcommands.arguments;

import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.iutils.data.ReferenceData;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by jonathan on 26/02/16.
 */

/**
 * ArgumentSpec specification.
 *
 * Arguments is held in {@link com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder} and
 * stored in {@link Arguments}
 *
 * Arguments have:
 *
 * A Converter, Converter translate {@link Text} representation to {@link Object} representation.
 *
 * A Checker, Checker is a {@link Matchable} supplier, normally Supplied Matchable is {@link Text}.
 *
 * A Predicate, Predicate is a {@link String} provider
 *
 * @param <ID> ID Type of ArgumentSpec
 * @param <T>  Value Type
 */
public class ArgumentSpec<ID, T> {

    /**
     * ArgumentSpec ID to be retrieved later
     */
    private final ID id;
    /**
     * True if is an Optional ArgumentSpec (not required)
     */
    private final boolean optional;

    /**
     * Arrays arguments (List for example)
     */
    private final boolean isArray;

    /**
     * Converter
     */
    private final Function<List<String>, T> converter;
    /**
     * Checker
     */
    private final Supplier<Matchable<String>> checker;
    /**
     * Predicate
     */
    private final Predicate<List<String>> predicate;
    /**
     * Extra Data's, AdditionalData provided by {@link com.github.jonathanxd.wcommands.ext.Extension}
     * and 2nd/3rd APIs
     */
    private final ExtraData data = new ExtraData();

    /**
     * Reference Data's, AdditionalData provided by {@link com.github.jonathanxd.wcommands.ext.Extension}
     * and 2nd/3rd APIs
     */
    private final ReferenceData referenceData = new ReferenceData();

    public ArgumentSpec(ID id, boolean isArray, Supplier<Matchable<String>> checker, Predicate<List<String>> predicateChecker, boolean optional, Function<List<String>, T> converter) {
        this.id = id;
        this.isArray = isArray;
        this.checker = checker;
        this.predicate = predicateChecker;
        this.optional = optional;
        this.converter = converter;
    }

    /**
     * Get the ID
     *
     * @return the ID
     */
    public ID getId() {
        return id;
    }

    /**
     * Get checker
     *
     * @return Checker
     */
    public Supplier<Matchable<String>> getChecker() {
        return checker;
    }

    /**
     * Get Predicate
     *
     * @return Predicate
     */
    public Predicate<List<String>> getPredicate() {
        return predicate;
    }

    /**
     * True if is a optional ArgumentSpec, false otherwise
     *
     * @return True if is a optional ArgumentSpec, false otherwise
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * True if is a infinite ArgumentSpec (lists/arrays)
     *
     * @return True if is a infinite ArgumentSpec (lists/arrays)
     */
    public boolean isArray() {
        return isArray;
    }

    /**
     * {@link Text} representation to {@link Object} representation converter
     *
     * @return {@link Text} representation to {@link Object} representation converter
     */
    public Function<List<String>, T> getConverter() {
        return converter;
    }

    /**
     * Extra Data (Additional Data)
     *
     * @return Extra Data (Additional Data)
     */
    public ExtraData getData() {
        return data;
    }

    /**
     * Reference Data (Additional Data)
     *
     * @return Reference Data (Additional Data)
     */
    public ReferenceData getReferenceData() {
        return referenceData;
    }

    @Override
    public String toString() {
        return "ArgumentSpec[id=" + id + ", isOptional=" + optional + "]";
    }
}
