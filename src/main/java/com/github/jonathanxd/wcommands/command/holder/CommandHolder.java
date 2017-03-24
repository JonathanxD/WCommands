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
package com.github.jonathanxd.wcommands.command.holder;

import com.github.jonathanxd.iutils.container.IMutableContainer;
import com.github.jonathanxd.iutils.container.MutableContainer;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
     * Parent command holder
     *
     * @see CommandSpec
     */
    private final CommandHolder parent;

    /**
     * Arguments
     *
     * @see ArgumentsHolder
     */
    private final ArgumentsHolder argumentsHolder;

    /**
     * Last matching (indicates a last commandSpec matched for the group)
     */
    private final boolean lastMatching;

    /**
     * Each argument utility
     *
     * @see EachArguments
     */
    private final EachArguments eachArguments = new EachArguments();

    public CommandHolder(CommandSpec commandSpec, ArgumentsHolder arguments, CommandHolder parent) {
        this(commandSpec, parent, arguments, false);
    }

    public CommandHolder(CommandSpec commandSpec, CommandHolder parent, ArgumentsHolder arguments, boolean lastMatching) {
        this.commandSpec = commandSpec;
        this.parent = parent;
        this.argumentsHolder = arguments;
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
        if (holder.getParent() != null) {
            appendable.append(',').append("parent=");
            toAppendable(holder.getParent(), appendable);
        }
        appendable.append(',')
                .append("lastMatching=").append(String.valueOf(holder.isLastMatching()))
                .append(',')
                .append('}')
                .append(',');

        return appendable;
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
    public <ID, R> Optional<List<String>> getPlainArgument(ID id) {
        Optional<ArgumentHolder<ID, R>> argumentHolder = getArgument(id);

        if (argumentHolder.isPresent()) {
            return Optional.of(argumentHolder.get().getValues());
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

    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * @return {@link EachArguments}
     * @see EachArguments
     */
    public EachArguments eachArguments() {
        return eachArguments;
    }

    /**
     * Return true if is main command
     *
     * @return true if is main command
     */
    public boolean isMain() {
        return getParent() == null;
    }

    /**
     * Return parent command holder
     *
     * @return Parent command holder
     */
    public CommandHolder getParent() {
        return parent;
    }

    @Override
    public boolean matches(String other) {
        return commandSpec.matches(other);
    }

    @Override
    public boolean matchesIgnoreCase(String other) {
        return commandSpec.matchesIgnoreCase(other);
    }

    @Override
    public int hashCode() {
        return getCommandSpec().hashCode();
    }

    /**
     * Each arguments is a simple class to analise argument list
     */
    public class EachArguments {
        /**
         * Consume all arguments as Plain ArgumentSpec
         *
         * @param argumentConsumer ArgumentSpec consumer
         */
        public void plain(Consumer<List<String>> argumentConsumer) {
            holder(holder -> argumentConsumer.accept(((ArgumentHolder<?, ?>) holder).getValues()));
        }

        /**
         * Consume all ArgumentSpec holder
         *
         * @param argumentConsumer ArgumentHolder consumer
         */
        public void holder(Consumer<ArgumentHolder> argumentConsumer) {
            for (ArgumentHolder argumentHolder : argumentsHolder) {
                argumentConsumer.accept(argumentHolder);
            }
        }
    }
}
