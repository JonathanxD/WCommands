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

/**
 * Created by jonathan on 24/02/16.
 */
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

    private static class All implements Function<List<String>, String> {
        @Override
        public String apply(List<String> text) {
            return text.isEmpty() ? null : text.get(0);
        }
    }

    public static <H> CommandBuilder<H> commandBuilder() {
        return CommandBuilder.builder();
    }

    public static <ID, T> ArgumentBuilder<ID, T> argumentBuilder() {
        return ArgumentBuilder.builder();
    }

}
