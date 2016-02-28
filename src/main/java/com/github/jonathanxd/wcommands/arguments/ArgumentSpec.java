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
package com.github.jonathanxd.wcommands.arguments;

import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

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
 * A Predicate, Predicate is a {@link Matchable} provider, normally Provided Matchable is {@link
 * Text}
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
     * Converter
     */
    private final Function<Text, T> converter;
    /**
     * Checker
     */
    private final Supplier<Matchable<String>> checker;
    /**
     * Predicate
     */
    private final Predicate<Text> predicate;
    /**
     * Extra Data's, AdditionalData provided by {@link com.github.jonathanxd.wcommands.ext.Extension} and 2nd/3rd APIs
     */
    private final ExtraData data = new ExtraData();

    public ArgumentSpec(ID id, Supplier<Matchable<String>> checker, Predicate<Text> predicateChecker, boolean optional, Function<Text, T> converter) {
        this.id = id;
        this.checker = checker;
        this.predicate = predicateChecker;
        this.optional = optional;
        this.converter = converter;
    }

    /**
     * Get the ID
     * @return the ID
     */
    public ID getId() {
        return id;
    }

    /**
     * Get checker
     * @return Checker
     */
    public Supplier<Matchable<String>> getChecker() {
        return checker;
    }

    /**
     * Get Predicate
     * @return Predicate
     */
    public Predicate<Text> getPredicate() {
        return predicate;
    }

    /**
     * True if is a optional ArgumentSpec, false otherwise
     * @return True if is a optional ArgumentSpec, false otherwise
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * {@link Text} representation to {@link Object} representation converter
     * @return {@link Text} representation to {@link Object} representation converter
     */
    public Function<Text, T> getConverter() {
        return converter;
    }

    /**
     * Extra Data (Additional Data)
     * @return Extra Data (Additional Data)
     */
    public ExtraData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ArgumentSpec[id="+id+", isOptional="+optional+"]";
    }
}
