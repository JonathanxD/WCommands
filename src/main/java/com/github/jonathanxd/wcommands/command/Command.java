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
package com.github.jonathanxd.wcommands.command;

import com.github.jonathanxd.iutils.annotations.Immutable;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.common.alias.AliasList;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 26/02/16.
 */

/**
 * Command Specification
 *
 * A command have Child Commands and {@link Arguments} specification
 */
public class Command implements Matchable<String> {

    /**
     * Command Name
     */
    private final Text name;
    /**
     * Command prefix
     */
    private final String prefix;
    /**
     * Command suffix
     */
    private final String suffix;
    /**
     * Is Optional Command?
     *
     * @deprecated OPTIONAL COMMAND???
     */
    @Deprecated
    private final boolean isOptional;
    /**
     * Command Handler
     */
    private final Handler defaultHandler;
    /**
     * Argument specification list
     *
     * @see Arguments
     */
    private final Arguments arguments;
    /**
     * Alias list
     *
     * @see AliasList
     */
    private final AliasList aliasList = new AliasList();
    /**
     * List of commands
     */
    private final CommandList subCommands = new CommandList();

    /**
     * Create a new Command instance
     *
     * @param name           Name of command
     * @param arguments      Arguments
     * @param isOptional     Is Optional??
     * @param prefix         Prefix
     * @param suffix         Suffix
     * @param defaultHandler Command handler
     */
    public Command(Text name, Arguments arguments, @Deprecated boolean isOptional, String prefix, String suffix, Handler defaultHandler) {
        this.name = name;
        this.arguments = arguments;
        this.isOptional = isOptional;
        this.prefix = prefix;
        this.suffix = suffix;
        this.defaultHandler = defaultHandler;
    }

    public static String toString(Command command) {
        try {
            return toString(command, new StringBuffer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Command command, Appendable append) throws IOException {
        return toAppendable(command, append).toString();
    }

    public static Appendable toAppendable(Command command, Appendable append) throws IOException {
        append.append(command.getClass().getSimpleName()).append("[")
                .append("name=").append(command.getName().getPlainString())
                .append(",")
                .append("isOptional=").append(String.valueOf(command.isOptional()))
                .append(",")
                .append("prefix=").append(command.getPrefix())
                .append(",")
                .append("suffix=").append(command.getSuffix())
                .append(",")
                .append("alias=").append(command.aliasList.toString())
                .append(",")
                .append("subCommands={");
        for (Object subCommand : command.getSubCommands()) {
            toAppendable((Command) subCommand, append);
        }
        append.append("}")
                .append("]");
        return append;
    }

    public static boolean matches(Command command, String other, boolean ignoreCase) {
        String withoutPAS = other;

        if (command.prefix != null) {
            if (!other.startsWith(command.prefix))
                return false;
            else
                withoutPAS = withoutPAS.substring(command.prefix.length());
        }

        if (command.suffix != null) {
            if (!other.endsWith(command.suffix))
                return false;
            else
                withoutPAS = withoutPAS.substring(0, withoutPAS.length() - command.suffix.length());
        }

        return Text.matches(command.getName(), withoutPAS, ignoreCase) || command.getAliases().anyMatches(withoutPAS, ignoreCase);
    }

    /**
     * Add child command
     *
     * @param command Command
     * @return This
     */
    public Command addSub(Command command) {
        this.subCommands.add(command);
        return this;
    }

    /**
     * Add child commands*
     *
     * @param commands Commands*
     * @return This
     */
    public Command subCommands(Command... commands) {
        addSubs(commands);
        return this;
    }

    /**
     * Add List of child commands
     *
     * @param commands Commands
     * @return This
     */
    public Command addSubs(List<Command> commands) {
        if (commands.size() > 0)
            this.subCommands.addAll(commands);
        return this;
    }

    /**
     * Add child commands*
     *
     * @param commands Child commands
     * @return This
     */
    public Command addSubs(Command... commands) {
        this.subCommands.addAll(Arrays.asList(commands));
        return this;
    }

    /**
     * Remove child command
     *
     * @param command Command
     * @return This
     */
    public Command removeSub(Command command) {
        this.subCommands.remove(command);
        return this;
    }

    /**
     * Add command Alias
     *
     * @param alias Alias
     * @return This
     */
    public Command addAlias(Text alias) {
        this.aliasList.add(alias);
        return this;
    }

    /**
     * Add aliases*
     *
     * @param aliases Aliases
     * @return This
     */
    public Command addAliases(Text... aliases) {
        this.aliasList.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * Remove alias
     *
     * @param alias Alias
     * @return This
     */
    public Command removeAlias(Text alias) {
        this.aliasList.remove(alias);
        return this;
    }

    /**
     * Get all names (including aliases)
     *
     * @return all names (including aliases)
     */
    public Collection<Text> allTexts() {
        List<Text> texts = new ArrayList<>();
        texts.add(name);
        texts.addAll(getAliases());

        return texts;
    }

    /**
     * Immutable sub commands
     *
     * @return Sub Commands
     */
    @Immutable
    public List<Command> getSubCommands() {
        return Collections.unmodifiableList(subCommands);
    }

    /**
     * Get not optional sub commands
     *
     * @return Not optional sub commands
     */
    @Immutable
    public List<Command> getNotOptionalSubCommands() {

        List<Command> notOptionalSubCommands;

        notOptionalSubCommands = subCommands.stream().filter(Command::notOptional).collect(Collectors.toList());

        return Collections.unmodifiableList(notOptionalSubCommands);
    }

    /**
     * Get all aliases
     *
     * @return Aliases
     */
    @Immutable
    public AliasList getAliases() {
        return aliasList.copy();
    }

    /**
     * Get command name
     *
     * @return command name
     */
    public Text getName() {
        return name;
    }

    /**
     * True if is Optional
     *
     * @return True if is Optional command
     */
    @Deprecated
    public boolean isOptional() {
        return isOptional;
    }

    /**
     * True if is not optional
     *
     * @return True if is not optional
     */
    public boolean notOptional() {
        return !isOptional;
    }

    /**
     * Get the command prefix
     *
     * @return Command prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the Command suffix
     *
     * @return Command suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Get {@link Arguments}
     *
     * @return {@link Arguments}
     */
    public Arguments getArguments() {
        return arguments;
    }

    /**
     * Get alias list
     *
     * @return Alias list
     */
    protected List<Text> getAliasList() {
        return aliasList;
    }

    /**
     * Get default handler
     *
     * @return default handler
     */
    public Handler getDefaultHandler() {
        return defaultHandler;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * Name matches and Alias matches
     */
    @Override
    public boolean matches(String other) {
        return matches(this, other, false);
    }

    @Override
    public boolean matchesIgnoreCase(String other) {
        return matches(this, other, true);
    }


}
