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
package com.github.jonathanxd.wcommands.common.command;

import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.exceptions.CommandAlreadyRegisteredException;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Created by jonathan on 23/02/16.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CommandList implements List<CommandSpec> {

    public static final InfoId COMMANDLIST_INFOID = new InfoId("CommandList", CommandList.class);

    private final List<CommandSpec> commandSpecs;
    private final Optional<WCommand<?>> wCommandOptional;
    private final Object holdingObject;

    public CommandList(@Nonnull Object holdingObject) {
        this(new ArrayList<>(), holdingObject);
    }

    public CommandList(WCommand<?> wCommand, @Nonnull Object holdingObject) {
        this(new ArrayList<>(), Optional.ofNullable(wCommand), holdingObject);
    }

    public CommandList(List<CommandSpec> list, @Nonnull Object holdingObject) {
        this(list, Optional.empty(), holdingObject);
    }

    public CommandList(List<CommandSpec> list, Optional<WCommand<?>> wCommandOptional, @Nonnull Object holdingObject) {
        this.commandSpecs = list;
        this.wCommandOptional = wCommandOptional;
        this.holdingObject = Objects.requireNonNull(holdingObject, "Please don't create a CommandList with empty holdingObject");
    }

    public static CommandList singleton(CommandSpec commandSpec, Object holdingObject) {
        return new CommandList(Collections.singletonList(commandSpec), holdingObject);
    }

    public static Collection<CommandSpec> findParentsIn(CommandList commandList, CommandSpec parentTo) {

        Collection<CommandSpec> commandSpecs = new HashSet<>();

        for (CommandSpec spec : commandList) {
            if (spec.equals(parentTo)) {
                commandSpecs.add(spec);
            }
            commandSpecs.addAll(findParentsIn(spec.getSubCommands(), parentTo));
        }

        return commandSpecs;
    }

    public Optional<CommandSpec> getCommandOf(Collection<Text> namesAndAliases) {
        for (CommandSpec commandSpec : this.commandSpecs) {

            Optional<Text> textOptional = Functions.return_(namesAndAliases, nameOrAlias -> commandSpec.getName().compareTo(nameOrAlias) == 0 || commandSpec.getAliases().contains(nameOrAlias));

            if (textOptional.isPresent())
                return Optional.of(commandSpec);

        }

        return Optional.empty();
    }

    public Optional<CommandSpec> getCommandOf(Text nameOrAlias) {

        for (CommandSpec commandSpec : this.commandSpecs) {
            if (commandSpec.getName().compareTo(nameOrAlias) == 0 || commandSpec.getAliases().contains(nameOrAlias)) {
                return Optional.of(commandSpec);
            }
        }

        return Optional.empty();
    }

    public Optional<CommandSpec> getCommandByAliasOrName(Text name, Collection<Text> aliases) {
        return this.stream().filter(command -> {
            if (name != null && command.getName().equals(name)) {
                return true;
            } else if (aliases != null) {
                return aliases.stream().filter(alias -> command.getAliases().contains(alias)).findAny().isPresent();
            }
            return false;
        }).findFirst();
    }

    public Optional<CommandSpec> getCommandByAlias(Text alias) {
        return this.stream().filter(command -> command.getAliases().contains(alias)).findFirst();
    }

    public CommandSpec getAnyMatching(String match) {
        for (CommandSpec commandSpec : this) {
            if (commandSpec.matches(match))
                return commandSpec;
        }

        return null;
    }

    /**
     * Commonly gets the source (likely parent) was created this CommandList, normally it can be a
     * WCommand instance or CommandSpec (parent)
     *
     * @return Commonly object that hold this instance.
     */
    public Optional<Object> getHoldingObject() {
        return Optional.ofNullable(holdingObject);
    }

    public CommandList toUnmodifiable() {
        return new CommandList(Collections.unmodifiableList(commandSpecs), this.holdingObject);
    }

    public Optional<WCommand<?>> getwCommandOptional() {
        return wCommandOptional;
    }

    @Override
    public int size() {
        return commandSpecs.size();
    }

    @Override
    public boolean isEmpty() {
        return commandSpecs.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return commandSpecs.contains(o);
    }

    @Override
    public Iterator<CommandSpec> iterator() {
        return commandSpecs.iterator();
    }

    @Override
    public Object[] toArray() {
        return commandSpecs.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return commandSpecs.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return commandSpecs.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return commandSpecs.containsAll(c);
    }

    // FILTER
    // HANDLE
    @Deprecated
    @Override
    public boolean add(CommandSpec commandSpec) {
        System.err.println("Deprecated !!! -> " + Arrays.toString(Thread.currentThread().getStackTrace()));
        throw new UnsupportedOperationException();
    }

    public boolean add(CommandSpec commandSpec, RegistrationTicket<?> ticket) {
        int len = commandSpecs.size();
        handle(filter(Collections.singleton(commandSpec), ticket), ticket).forEach(commandSpecs::add);
        return commandSpecs.size() != len;
    }

    // FILTER
    // HANDLE
    @Deprecated
    @Override
    public boolean addAll(Collection<? extends CommandSpec> c) {
        System.err.println("Deprecated !!! -> " + Arrays.toString(Thread.currentThread().getStackTrace()));
        //return commandSpecs.addAll(handle(filter(c)));
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends CommandSpec> c, RegistrationTicket<?> ticket) {
        return commandSpecs.addAll(handle(filter(c, ticket), ticket));
    }

    // FILTER
    // HANDLE
    @Deprecated
    @Override
    public boolean addAll(int index, Collection<? extends CommandSpec> c) {
        System.err.println("Deprecated !!! -> " + Arrays.toString(Thread.currentThread().getStackTrace()));
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends CommandSpec> c, RegistrationTicket<?> ticket) {
        return commandSpecs.addAll(index, handle(filter(c, ticket), ticket));
    }

    // FILTER
    // HANDLE
    @Deprecated
    @Override
    public CommandSpec set(int index, CommandSpec element) {
        System.err.println("Deprecated !!! -> " + Arrays.toString(Thread.currentThread().getStackTrace()));
        throw new UnsupportedOperationException();
    }

    public CommandSpec set(int index, CommandSpec element, RegistrationTicket<?> ticket) {
        Collection<? extends CommandSpec> coll = filter(Collections.singleton(element), ticket);

        if (coll.size() == 1) {

            for (CommandSpec spec : handle(coll, 1, ticket)) {
                return commandSpecs.set(index, spec);
            }

        }

        return element;
    }

    // FILTER
    // HANDLE
    @Override
    public void add(int index, CommandSpec element) {
        System.err.println("Deprecated !!! -> " + Arrays.toString(Thread.currentThread().getStackTrace()));
        //return commandSpecs.addAll(index, handle(filter(c)));
        throw new UnsupportedOperationException();
    }

    public void add(int index, CommandSpec element, RegistrationTicket<?> ticket) {
        Collection<? extends CommandSpec> coll = filter(Collections.singleton(element), ticket);

        if (coll.size() == 1) {

            for (CommandSpec spec : handle(coll, 1, ticket)) {
                commandSpecs.add(index, spec);
            }

        }
    }

    private Collection<? extends CommandSpec> handle(Collection<? extends CommandSpec> collection, RegistrationTicket<?> ticket) {
        return handle(collection, -1, ticket);
    }

    private Collection<? extends CommandSpec> handle(Collection<? extends CommandSpec> collection, int maxHandle, RegistrationTicket<?> ticket) {

        Collection<CommandSpec> commandSpecCollection = new ArrayList<>();

        if (this.getwCommandOptional().isPresent()) {

            int x = 0;
            for (CommandSpec commandSpec : collection) {

                if (maxHandle > -1 && x >= maxHandle)
                    break;

                Optional<CommandSpec> specOptional = this.getwCommandOptional().get().handleRegistrationToTicket(commandSpec, this, ticket);


                if (specOptional.isPresent()) {
                    specOptional = this.getwCommandOptional().get().handleRegistration(commandSpec, this, ticket);

                    if (specOptional.isPresent()) {
                        commandSpecCollection.add(specOptional.get());
                    }
                }

                ++x;

            }
        } else {
            commandSpecCollection.addAll(collection);
        }

        return commandSpecCollection;
    }

    private Collection<? extends CommandSpec> filter(Collection<? extends CommandSpec> commands, RegistrationTicket<?> ticket) {

        Collection<CommandSpec> commandSpecCollection = new ArrayList<>();

        for (CommandSpec commandSpec : commands) {

            Optional<CommandSpec> commandOptional = getCommandOf(commandSpec.allTexts());
            if (!commandOptional.isPresent()) {
                // CONVERT
                commandSpecCollection.add(convert(commandSpec, ticket));
            }
        }

        if (commandSpecCollection.size() == 0)
            throw new CommandAlreadyRegisteredException("Command '" + commands + "' already registered!");

        return commandSpecCollection;
    }

    private CommandSpec convert(CommandSpec commandSpec, RegistrationTicket<?> ticket) {
        if (this.getwCommandOptional().isPresent()
                && (commandSpec.getSubCommands().isEmpty() || !commandSpec.getSubCommands().getwCommandOptional().isPresent())) {
            commandSpec = CommandSpec.withHandledCommandList(this.getwCommandOptional().get(), commandSpec, ticket);
        }

        return commandSpec;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return commandSpecs.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return commandSpecs.retainAll(c);
    }

    @Override
    public void clear() {
        commandSpecs.clear();
    }

    @Override
    public CommandSpec get(int index) {
        return commandSpecs.get(index);
    }

    @Override
    public CommandSpec remove(int index) {
        return commandSpecs.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return commandSpecs.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return commandSpecs.lastIndexOf(o);
    }

    @Override
    public ListIterator<CommandSpec> listIterator() {
        return commandSpecs.listIterator();
    }

    @Override
    public ListIterator<CommandSpec> listIterator(int index) {
        return commandSpecs.listIterator(index);
    }

    @Override
    public List<CommandSpec> subList(int fromIndex, int toIndex) {
        return commandSpecs.subList(fromIndex, toIndex);
    }

    public CommandList copy() {
        CommandList list = new CommandList(this, this.holdingObject);
        list.addAll(this, new CommonTicket<>(this));
        return list;
    }

    @Override
    public String toString() {
        return commandSpecs.toString();
    }
}
