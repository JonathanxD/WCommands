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

import com.github.jonathanxd.wcommands.command.Command;
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
public class CommandList implements List<Command> {

    private final List<Command> commands = new ArrayList<>();

    public Optional<Command> getCommandOf(Collection<Text> namesAndAliases) {
        for (Command command : this.commands) {

            Optional<Text> textOptional = Functions.return_(namesAndAliases, nameOrAlias -> command.getName().compareTo(nameOrAlias) == 0 || command.getAliases().contains(nameOrAlias));

            if (textOptional.isPresent())
                return Optional.of(command);

        }

        return Optional.empty();
    }

    public Optional<Command> getCommandOf(Text nameOrAlias) {

        for (Command command : this.commands) {
            if (command.getName().compareTo(nameOrAlias) == 0 || command.getAliases().contains(nameOrAlias)) {
                return Optional.of(command);
            }
        }

        return Optional.empty();
    }

    public Optional<Command> getCommandByAliasOrName(Text name, Collection<Text> aliases) {
        return this.stream().filter(command -> {
            if (name != null && command.getName().equals(name)) {
                return true;
            } else if (aliases != null) {
                return aliases.stream().filter(alias -> command.getAliases().contains(alias)).findAny().isPresent();
            }
            return false;
        }).findFirst();
    }

    public Optional<Command> getCommandByAlias(Text alias) {
        return this.stream().filter(command -> command.getAliases().contains(alias)).findFirst();
    }

    @Override
    public int size() {
        return commands.size();
    }

    @Override
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return commands.contains(o);
    }

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    @Override
    public Object[] toArray() {
        return commands.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return commands.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return commands.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return commands.containsAll(c);
    }

    // FILTER
    @Override
    public boolean add(Command command) {
        int len = commands.size();
        filter(Collections.singleton(command)).forEach(commands::add);

        return commands.size() != len;
    }

    // FILTER
    @Override
    public boolean addAll(Collection<? extends Command> c) {
        return commands.addAll(filter(c));

    }

    // FILTER
    @Override
    public boolean addAll(int index, Collection<? extends Command> c) {
        return commands.addAll(index, filter(c));
    }

    // FILTER
    @Override
    public Command set(int index, Command element) {
        if (filter(Collections.singleton(element)).size() == 1) {
            return commands.set(index, element);
        }

        return element;
    }

    // FILTER
    @Override
    public void add(int index, Command element) {
        if (filter(Collections.singleton(element)).size() == 1) {
            commands.add(index, element);
        }
    }

    private Collection<? extends Command> filter(Collection<? extends Command> commands) {

        Collection<Command> commandCollection = new ArrayList<>();

        for (Command command : commands) {

            Optional<Command> commandOptional = getCommandOf(command.allTexts());
            if (!commandOptional.isPresent()) {
                commandCollection.add(command);
            }
        }

        if (commandCollection.size() == 0)
            throw new IllegalArgumentException("Already in list! : " + commands);

        return commandCollection;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        return commands.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return commands.retainAll(c);
    }

    @Override
    public void clear() {
        commands.clear();
    }

    @Override
    public Command get(int index) {
        return commands.get(index);
    }

    @Override
    public Command remove(int index) {
        return commands.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return commands.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return commands.lastIndexOf(o);
    }

    @Override
    public ListIterator<Command> listIterator() {
        return commands.listIterator();
    }

    @Override
    public ListIterator<Command> listIterator(int index) {
        return commands.listIterator(index);
    }

    @Override
    public List<Command> subList(int fromIndex, int toIndex) {
        return commands.subList(fromIndex, toIndex);
    }

    public CommandList copy() {
        CommandList list = new CommandList();
        list.addAll(this);
        return list;
    }

}
