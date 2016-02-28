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
package com.github.jonathanxd.wcommands.command.holder;

import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.Command;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 24/02/16.
 */

/**
 * {@link Command} command holder
 *
 * Represents a Parsed Command.
 */
public class CommandHolder implements Matchable<String> {

    /**
     * Related command
     *
     * @see Command
     */
    private final Command command;

    /**
     * Arguments
     *
     * @see ArgumentsHolder
     */
    private final ArgumentsHolder argumentsHolder;

    /**
     * Is Command present (always true)
     */
    @Deprecated
    private final boolean isPresent;

    /**
     * Last matching (indicates a last command matched for the group)
     */
    private final boolean lastMatching;

    /**
     * Child parsed commands
     *
     * @deprecated Old System part, now all commands are stored separately.
     */
    @Deprecated
    private final List<CommandHolder> subCommands = new ArrayList<>();

    /**
     * Each argument utility
     *
     * @see EachArguments
     */
    private final EachArguments eachArguments = new EachArguments();

    public CommandHolder(Command command, ArgumentsHolder arguments) {
        this(command, false, arguments);
    }

    public CommandHolder(Command command, boolean isPresent, ArgumentsHolder arguments) {
        this(command, arguments, isPresent, false);
    }

    public CommandHolder(Command command, ArgumentsHolder arguments, boolean isPresent, boolean lastMatching) {
        this.command = command;
        this.argumentsHolder = arguments;
        this.isPresent = isPresent;
        this.lastMatching = lastMatching;
    }

    /**
     * To String
     *
     * @param holder Command Holder
     * @return String Representation
     */
    public static String toString(CommandHolder holder) {
        return toString(holder, new StringBuilder());
    }

    /**
     * To Appendable, performance improvement
     *
     * @param holder     Command Holder
     * @param appendable Appendable
     * @return String Representation
     */
    public static String toString(CommandHolder holder, Appendable appendable) {
        try {
            return toAppendable(holder, appendable).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performance improvement
     *
     * @param holder     Command Holder
     * @param appendable Appendable
     * @return Appendable
     */
    public static Appendable toAppendable(CommandHolder holder, Appendable appendable) throws IOException {

        appendable.append(holder.getClass().getSimpleName()).append('{')
                .append("command=");
        appendable.append(holder.getCommand().getName().getPlainString());
        //Command.toAppendable(holder.getCommand(), appendable);
        appendable.append(',')
                .append("isPresent=").append(String.valueOf(holder.isPresent()))
                .append(',')
                .append("lastMatching=").append(String.valueOf(holder.isLastMatching()))
                .append(',')
                .append("child=[|");
        for (CommandHolder subCommand : holder.getSubCommands()) {
            toAppendable(subCommand, appendable);
        }
        appendable.append("|]")
                .append('}')
                .append(',');

        return appendable;
    }

    /**
     * Recursive loop command holder and child command holder &amp; collect filtered CommandHolders
     *
     * @param main            Main command holder
     * @param holderPredicate Holder predicate
     * @param commandHolders  List of command Holders (Nullable)
     * @return Filtered set of CommandHolders
     */
    public static Set<CommandHolder> recursive(CommandHolder main, Predicate<CommandHolder> holderPredicate, Set<CommandHolder> commandHolders) {
        if (commandHolders == null)
            commandHolders = new HashSet<>();

        if (holderPredicate.test(main)) {
            commandHolders.add(main);
        }

        for (CommandHolder holder : main.getSubCommands()) {
            if (holderPredicate.test(holder)) {
                commandHolders.add(holder);
            }
            recursive(holder, holderPredicate, commandHolders);

        }

        return commandHolders;
    }

    public boolean isLastMatching() {
        return lastMatching;
    }

    /**
     * Get related command
     *
     * @return related command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Get specific argument
     *
     * @param id   ID of argument
     * @param <ID> ID Type
     * @param <R>  Value Type
     * @return Optional of argument holder
     */
    @SuppressWarnings("unchecked")
    public <ID, R> Optional<ArgumentHolder<ID, R>> getArgument(ID id) {

        AtomicReference<ArgumentHolder<ID, R>> holder = new AtomicReference<>();

        eachArguments().holder(c -> {
            if (holder.get() != null) {
                return;
            }
            if (c.getArgument().getId().equals(id)) {
                holder.set(c);
            }
        });

        return Optional.ofNullable(holder.get());
    }

    /**
     * Get argument value
     *
     * @param id   ID of argument
     * @param <ID> ID type
     * @param <R>  Value type
     * @return Optional of value
     */
    public <ID, R> Optional<R> getArgValue(ID id) {
        Optional<ArgumentHolder<ID, R>> argumentHolder = getArgument(id);

        if (argumentHolder.isPresent()) {
            return Optional.of(argumentHolder.get().convertValue());
        }

        return Optional.empty();
    }

    /**
     * Get {@link Text} representation of argument
     *
     * @param id   ID of argument
     * @param <ID> ID Type
     * @param <R>  Value Type?
     * @return Optional of {@link Text} representation
     */
    public <ID, R> Optional<Text> getPlainArgument(ID id) {
        Optional<ArgumentHolder<ID, R>> argumentHolder = getArgument(id);

        if (argumentHolder.isPresent()) {
            return Optional.of(argumentHolder.get().getValue());
        }

        return Optional.empty();
    }

    /**
     * Get all arguments
     *
     * @return All arguments
     */
    public ArgumentsHolder getArguments() {
        return argumentsHolder;
    }

    /**
     * True if is present (always true)
     *
     * @return True if is present (always true)
     * @deprecated Normally the List of command only contains Present commands
     */
    @Deprecated
    public boolean isPresent() {
        return isPresent;
    }

    @Deprecated
    public List<CommandHolder> getSubCommands() {
        return subCommands;
    }

    @Deprecated
    public List<CommandHolder> allPresent() {
        return subCommands.stream().filter(CommandHolder::isPresent).collect(Collectors.toList());
    }

    @Deprecated
    public Collection<CommandHolder> allMatching() {
        return this.isLastMatching() ? Collections.singletonList(this) : recursive(this, CommandHolder::isLastMatching, null);
    }

    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * Create a new CommandHolder with current CommandHolder child commands
     * @param command             Command
     * @param argumentHolders Argument holder
     * @param isPresent Is present?
     * @param lastMatching Last matching? (of the group)
     * @return new CommandHolder
     * @deprecated Old system part, no more child commands!
     */
    @Deprecated
    public CommandHolder newWith(Command command, ArgumentsHolder argumentHolders, boolean isPresent, boolean lastMatching) {
        CommandHolder holder = new CommandHolder(command, argumentHolders, isPresent, lastMatching);

        holder.subCommands.addAll(this.subCommands);

        return holder;
    }

    /**
     * @see EachArguments
     * @return {@link EachArguments}
     */
    public EachArguments eachArguments() {
        return eachArguments;
    }


    @Override
    public boolean matches(String other) {
        return command.matches(other);
    }

    @Override
    public boolean matchesIgnoreCase(String other) {
        return command.matchesIgnoreCase(other);
    }

    /**
     * Each arguments is a simple class to analise argument list
     */
    public class EachArguments {
        /**
         * Consume all arguments as Plain Argument
         * @param argumentConsumer Argument consumer
         */
        public void plain(Consumer<Text> argumentConsumer) {
            holder(holder -> argumentConsumer.accept(holder.getValue()));
        }

        /**
         * Consume all Argument holder
         * @param argumentConsumer ArgumentHolder consumer
         */
        public void holder(Consumer<ArgumentHolder> argumentConsumer) {
            for (ArgumentHolder argumentHolder : argumentsHolder) {
                argumentConsumer.accept(argumentHolder);
            }
        }
    }
}
