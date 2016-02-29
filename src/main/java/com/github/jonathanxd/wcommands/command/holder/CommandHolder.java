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

import com.github.jonathanxd.iutils.extra.IMutableContainer;
import com.github.jonathanxd.iutils.extra.MutableContainer;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.CommandSpec;
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
 * {@link CommandSpec} commandSpec holder
 *
 * Represents a Parsed CommandSpec.
 */
public class CommandHolder implements Matchable<String> {

    /**
     * Related commandSpec
     *
     * @see CommandSpec
     */
    private final CommandSpec commandSpec;

    /**
     * Arguments
     *
     * @see ArgumentsHolder
     */
    private final ArgumentsHolder argumentsHolder;

    /**
     * Is CommandSpec present (always true)
     */
    @Deprecated
    private final boolean isPresent;

    /**
     * Last matching (indicates a last commandSpec matched for the group)
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

    public CommandHolder(CommandSpec commandSpec, ArgumentsHolder arguments) {
        this(commandSpec, false, arguments);
    }

    public CommandHolder(CommandSpec commandSpec, boolean isPresent, ArgumentsHolder arguments) {
        this(commandSpec, arguments, isPresent, false);
    }

    public CommandHolder(CommandSpec commandSpec, ArgumentsHolder arguments, boolean isPresent, boolean lastMatching) {
        this.commandSpec = commandSpec;
        this.argumentsHolder = arguments;
        this.isPresent = isPresent;
        this.lastMatching = lastMatching;
    }

    /**
     * To String
     *
     * @param holder CommandSpec Holder
     * @return String Representation
     */
    public static String toString(CommandHolder holder) {
        return toString(holder, new StringBuilder());
    }

    /**
     * To Appendable, performance improvement
     *
     * @param holder     CommandSpec Holder
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
     * @param holder     CommandSpec Holder
     * @param appendable Appendable
     * @return Appendable
     */
    public static Appendable toAppendable(CommandHolder holder, Appendable appendable) throws IOException {

        appendable.append(holder.getClass().getSimpleName()).append('{')
                .append("commandSpec=");
        appendable.append(holder.getCommandSpec().getName().getPlainString());
        //CommandSpec.toAppendable(holder.getCommandSpec(), appendable);
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
     * Recursive loop commandSpec holder and child commandSpec holder &amp; collect filtered CommandHolders
     *
     * @param main            Main commandSpec holder
     * @param holderPredicate Holder predicate
     * @param commandHolders  List of commandSpec Holders (Nullable)
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
     * Get related commandSpec
     *
     * @return related commandSpec
     */
    public CommandSpec getCommandSpec() {
        return commandSpec;
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
    public <ID, R> Optional<ArgumentHolder<ID, R>> getArgument(ID id, boolean acceptNotPresent) {

        IMutableContainer<ArgumentHolder<ID, R>> holder = new MutableContainer<>();

        eachArguments().holder(c -> {
            if (holder.isPresent()) {
                return;
            }
            if (c.getArgumentSpec().getId().equals(id) && (acceptNotPresent || c.isPresent())) {
                holder.set(c);
            }
        });

        return Optional.ofNullable(holder.get());
    }

    public <ID, R> Optional<ArgumentHolder<ID, R>> getArgument(ID id) {
        return getArgument(id, false);
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
     * @deprecated Normally the List of commandSpec only contains Present commands
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
     * @param commandSpec             CommandSpec
     * @param argumentHolders ArgumentSpec holder
     * @param isPresent Is present?
     * @param lastMatching Last matching? (of the group)
     * @return new CommandHolder
     * @deprecated Old system part, no more child commands!
     */
    @Deprecated
    public CommandHolder newWith(CommandSpec commandSpec, ArgumentsHolder argumentHolders, boolean isPresent, boolean lastMatching) {
        CommandHolder holder = new CommandHolder(commandSpec, argumentHolders, isPresent, lastMatching);

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
        return commandSpec.matches(other);
    }

    @Override
    public boolean matchesIgnoreCase(String other) {
        return commandSpec.matchesIgnoreCase(other);
    }

    /**
     * Each arguments is a simple class to analise argument list
     */
    public class EachArguments {
        /**
         * Consume all arguments as Plain ArgumentSpec
         * @param argumentConsumer ArgumentSpec consumer
         */
        public void plain(Consumer<Text> argumentConsumer) {
            holder(holder -> argumentConsumer.accept(holder.getValue()));
        }

        /**
         * Consume all ArgumentSpec holder
         * @param argumentConsumer ArgumentHolder consumer
         */
        public void holder(Consumer<ArgumentHolder> argumentConsumer) {
            for (ArgumentHolder argumentHolder : argumentsHolder) {
                argumentConsumer.accept(argumentHolder);
            }
        }
    }
}
