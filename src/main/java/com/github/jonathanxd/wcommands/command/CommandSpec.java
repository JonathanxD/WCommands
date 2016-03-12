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
 * CommandSpec Specification
 *
 * A command have Child Commands and {@link Arguments} specification
 */
public class CommandSpec implements Matchable<String> {

    public static final CommandSpec EMPTY = new CommandSpec(null, null, null, false, null, null, null);

    /**
     * CommandSpec Name
     */
    private final Text name;

    /**
     * CommandSpec Description
     */
    private final String description;

    /**
     * CommandSpec prefix
     */
    private final String prefix;

    /**
     * CommandSpec suffix
     */
    private final String suffix;

    /**
     * Is Optional CommandSpec (only works with sub-commands)?
     *
     *
     */
    private final boolean isOptional;

    /**
     * CommandSpec Handler
     */
    private final Handler defaultHandler;

    /**
     * ArgumentSpec specification list
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
     * Create a new CommandSpec instance
     *
     * @param name           Name of command
     * @param description    Description of command
     * @param arguments      Arguments
     * @param isOptional     Is Optional??
     * @param prefix         Prefix
     * @param suffix         Suffix
     * @param defaultHandler CommandSpec handler
     */
    public CommandSpec(Text name, String description, Arguments arguments, @Deprecated boolean isOptional, String prefix, String suffix, Handler defaultHandler) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
        this.isOptional = isOptional;
        this.prefix = prefix;
        this.suffix = suffix;
        this.defaultHandler = defaultHandler;
    }

    public static CommandSpec empty() {
        return EMPTY;
    }

    public static String toString(CommandSpec commandSpec) {
        try {
            return toString(commandSpec, new StringBuffer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(CommandSpec commandSpec, Appendable append) throws IOException {
        return toAppendable(commandSpec, append).toString();
    }

    public static Appendable toAppendable(CommandSpec commandSpec, Appendable append) throws IOException {
        append.append(commandSpec.getClass().getSimpleName()).append("[")
                .append("name=").append(commandSpec.getName().getPlainString())
                .append(",")
                .append("description=").append(commandSpec.getDescription())
                .append(",")
                .append("isOptional=").append(String.valueOf(commandSpec.isOptional()))
                .append(",")
                .append("prefix=").append(commandSpec.getPrefix())
                .append(",")
                .append("suffix=").append(commandSpec.getSuffix())
                .append(",")
                .append("alias=").append(commandSpec.aliasList.toString())
                .append(",")
                .append("subCommands={");
        for (Object subCommand : commandSpec.getSubCommands()) {
            toAppendable((CommandSpec) subCommand, append);
        }
        append.append("}")
                .append("]");
        return append;
    }

    public static boolean matches(CommandSpec commandSpec, String other, boolean ignoreCase) {
        String withoutPAS = other;

        if (commandSpec.prefix != null) {
            if (!other.startsWith(commandSpec.prefix))
                return false;
            else
                withoutPAS = withoutPAS.substring(commandSpec.prefix.length());
        }

        if (commandSpec.suffix != null) {
            if (!other.endsWith(commandSpec.suffix))
                return false;
            else
                withoutPAS = withoutPAS.substring(0, withoutPAS.length() - commandSpec.suffix.length());
        }

        return Text.matches(commandSpec.getName(), withoutPAS, ignoreCase) || commandSpec.getAliases().anyMatches(withoutPAS, ignoreCase);
    }

    public boolean isEmpty() {
        return this.name == null || this == empty();
    }

    /**
     * Add child commandSpec
     *
     * @param commandSpec CommandSpec
     * @return This
     */
    public CommandSpec addSub(CommandSpec commandSpec) {
        this.subCommands.add(commandSpec);
        return this;
    }

    /**
     * Add child commandSpecs*
     *
     * @param commandSpecs Commands*
     * @return This
     */
    public CommandSpec subCommands(CommandSpec... commandSpecs) {
        addSubs(commandSpecs);
        return this;
    }

    /**
     * Add List of child commandSpecs
     *
     * @param commandSpecs Commands
     * @return This
     */
    public CommandSpec addSubs(List<CommandSpec> commandSpecs) {
        if (commandSpecs.size() > 0)
            this.subCommands.addAll(commandSpecs);
        return this;
    }

    /**
     * Add child commandSpecs*
     *
     * @param commandSpecs Child commandSpecs
     * @return This
     */
    public CommandSpec addSubs(CommandSpec... commandSpecs) {
        this.subCommands.addAll(Arrays.asList(commandSpecs));
        return this;
    }

    /**
     * Remove child commandSpec
     *
     * @param commandSpec CommandSpec
     * @return This
     */
    public CommandSpec removeSub(CommandSpec commandSpec) {
        this.subCommands.remove(commandSpec);
        return this;
    }

    /**
     * Add command Alias
     *
     * @param alias Alias
     * @return This
     */
    public CommandSpec addAlias(Text alias) {
        this.aliasList.add(alias);
        return this;
    }

    /**
     * Add aliases*
     *
     * @param aliases Aliases
     * @return This
     */
    public CommandSpec addAliases(Text... aliases) {
        this.aliasList.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * Remove alias
     *
     * @param alias Alias
     * @return This
     */
    public CommandSpec removeAlias(Text alias) {
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
    public List<CommandSpec> getSubCommands() {
        return Collections.unmodifiableList(subCommands);
    }

    /**
     * Get not optional sub commands
     *
     * @return Not optional sub commands
     */
    @Immutable
    public List<CommandSpec> getNotOptionalSubCommands() {

        List<CommandSpec> notOptionalSubCommandSpecs;

        notOptionalSubCommandSpecs = subCommands.stream().filter(CommandSpec::notOptional).collect(Collectors.toList());

        return Collections.unmodifiableList(notOptionalSubCommandSpecs);
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
     * Command Description
     *
     * @return Command Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * True if is Optional
     *
     * !!! ONLY WORKS WITH SUB COMMANDS !!!
     *
     * @return True if is Optional command
     */
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
     * @return CommandSpec prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the CommandSpec suffix
     *
     * @return CommandSpec suffix
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
