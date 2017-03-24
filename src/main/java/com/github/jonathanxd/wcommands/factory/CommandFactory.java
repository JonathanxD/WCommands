/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands.factory;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CommandFactory {


    @Deprecated
    public static CommandSpec create(String name) {
        return create(Text.of(name), "", new Arguments(), false, "", "", null);
    }

    public static <H> CommandSpec create(String name, Handler<H> handler) {
        return create(Text.of(name), "", new Arguments(), false, "", "", handler);
    }

    public static <H> CommandSpec create(String name, String description, Handler<H> handler) {
        return create(Text.of(name), description, new Arguments(), false, "", "", handler);
    }

    public static <H> CommandSpec create(String name, Arguments arguments, Handler<H> handler) {
        return create(Text.of(name), "", arguments, false, "", "", handler);
    }

    public static <H> CommandSpec create(String name, String description, Arguments arguments, Handler<H> handler) {
        return create(Text.of(name), description, arguments, false, "", "", handler);
    }

    @Deprecated
    public static CommandSpec createOptional(String name) {
        return create(Text.of(name), "", new Arguments(), true, "", "", null);
    }

    public static <H> CommandSpec createOptional(String name, Handler<H> handler) {
        return create(Text.of(name), "", new Arguments(), true, "", "", handler);
    }

    public static <H> CommandSpec createOptional(String name, String description, Handler<H> handler) {
        return create(Text.of(name), description, new Arguments(), true, "", "", handler);
    }

    public static <H> CommandSpec createOptional(String name, Arguments arguments, Handler<H> handler) {
        return create(Text.of(name), "", arguments, true, "", "", handler);
    }

    public static <H> CommandSpec createOptional(String name, String description, Arguments arguments, Handler<H> handler) {
        return create(Text.of(name), description, arguments, true, "", "", handler);
    }


    public static <H> CommandSpec create(Text name, String description, Arguments arguments, boolean optional, String prefix, String suffix, Handler<H> handler) {

        return new CommandSpec(
                Objects.requireNonNull(name, "Name cannot be null"),
                // Primitives cannot be null
                description, arguments, optional,
                Objects.requireNonNull(prefix, "Prefix cannot be null. Must be empty or have a value"),
                Objects.requireNonNull(suffix, "Suffix cannot be null. Must be empty or have a value"),
                // Nullable
                handler);
    }

    public static <ID> ArgumentSpec<ID, String> create(ID id, Supplier<Matchable<String>> checker) {
        return create(id, checker, t -> true, false, false, new All());
    }

    public static <ID> ArgumentSpec<ID, String> create(ID id, boolean infinite, Supplier<Matchable<String>> checker) {
        return create(id, checker, t -> true, false, infinite, new All());
    }

    public static <ID> ArgumentSpec<ID, String> createOptional(ID id, Supplier<Matchable<String>> checker) {
        return create(id, checker, t -> true, true, false, new All());
    }

    public static <ID> ArgumentSpec<ID, String> createOptional(ID id, boolean infinite, Supplier<Matchable<String>> checker) {
        return create(id, checker, t -> true, true, infinite, new All());
    }

    public static <ID> ArgumentSpec<ID, String> create(ID id, Supplier<Matchable<String>> checker, Predicate<List<String>> postCheck) {
        return create(id, checker, postCheck, false, false, new All());
    }

    public static <ID> ArgumentSpec<ID, String> create(ID id, boolean infinite, Supplier<Matchable<String>> checker, Predicate<List<String>> postCheck) {
        return create(id, checker, postCheck, false, infinite, new All());
    }

    public static <ID> ArgumentSpec<ID, String> createOptional(ID id, Supplier<Matchable<String>> checker, Predicate<List<String>> postCheck) {
        return create(id, checker, postCheck, true, false, new All());
    }

    public static <ID> ArgumentSpec<ID, String> createOptional(ID id, boolean infinite, Supplier<Matchable<String>> checker, Predicate<List<String>> postCheck) {
        return create(id, checker, postCheck, true, infinite, new All());
    }

    public static <ID, T> ArgumentSpec<ID, T> create(ID id, Supplier<Matchable<String>> checker, Predicate<List<String>> predicate, boolean optional, boolean infinite, Function<List<String>, T> converter) {
        return new ArgumentSpec<>(id, infinite, checker, predicate, optional, converter);
    }

    public static <H> CommandBuilder<H> commandBuilder() {
        return CommandBuilder.builder();
    }

    public static <ID, T> ArgumentBuilder<ID, T> argumentBuilder() {
        return ArgumentBuilder.builder();
    }

    private static class All implements Function<List<String>, String> {
        @Override
        public String apply(List<String> text) {
            return text.isEmpty() ? null : text.get(0);
        }
    }

}
