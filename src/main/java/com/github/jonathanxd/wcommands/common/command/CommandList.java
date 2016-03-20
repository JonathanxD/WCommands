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

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * Created by jonathan on 23/02/16.
 */
public class CommandList implements List<CommandSpec> {

    public static final InfoId COMMANDLIST_INFOID = new InfoId("CommandList", CommandList.class);

    private final List<CommandSpec> commandSpecs;

    public CommandList() {
        this(new ArrayList<>());
    }

    public static CommandList singleton(CommandSpec commandSpec) {
        return new CommandList(Collections.singletonList(commandSpec));
    }

    public CommandList(List<CommandSpec> list) {
        this.commandSpecs = list;
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
        for(CommandSpec commandSpec : this) {
            if(commandSpec.matches(match))
                return commandSpec;
        }

        return null;
    }

    public CommandList toUnmodifiable() {
        return new CommandList(Collections.unmodifiableList(commandSpecs));
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
    @Override
    public boolean add(CommandSpec commandSpec) {
        int len = commandSpecs.size();
        filter(Collections.singleton(commandSpec)).forEach(commandSpecs::add);

        return commandSpecs.size() != len;
    }

    // FILTER
    @Override
    public boolean addAll(Collection<? extends CommandSpec> c) {
        return commandSpecs.addAll(filter(c));

    }

    // FILTER
    @Override
    public boolean addAll(int index, Collection<? extends CommandSpec> c) {
        return commandSpecs.addAll(index, filter(c));
    }

    // FILTER
    @Override
    public CommandSpec set(int index, CommandSpec element) {
        if (filter(Collections.singleton(element)).size() == 1) {
            return commandSpecs.set(index, element);
        }

        return element;
    }

    // FILTER
    @Override
    public void add(int index, CommandSpec element) {
        if (filter(Collections.singleton(element)).size() == 1) {
            commandSpecs.add(index, element);
        }
    }

    private Collection<? extends CommandSpec> filter(Collection<? extends CommandSpec> commands) {

        Collection<CommandSpec> commandSpecCollection = new ArrayList<>();

        for (CommandSpec commandSpec : commands) {

            Optional<CommandSpec> commandOptional = getCommandOf(commandSpec.allTexts());
            if (!commandOptional.isPresent()) {
                commandSpecCollection.add(commandSpec);
            }
        }

        if (commandSpecCollection.size() == 0)
            throw new IllegalArgumentException("Already in list! : " + commands);

        return commandSpecCollection;
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
        CommandList list = new CommandList();
        list.addAll(this);
        return list;
    }

}
