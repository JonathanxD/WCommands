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
package com.github.jonathanxd.wcommands.command;

import com.github.jonathanxd.iutils.annotation.Immutable;
import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.common.alias.AliasList;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;
import com.github.jonathanxd.wcommands.util.Functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CommandSpec Specification
 *
 * A command have Child Commands and {@link Arguments} specification
 */
public class CommandSpec implements Matchable<String> {

    public static final CommandSpec EMPTY = new CommandSpec(0, null, null, null, false, null, null, null);

    /**
     * Command priority.
     */
    private final int priority;

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
     *
     * Developer note:
     *
     * Don't modify the field name, if you want to modify see method {@link
     * #withHandledCommandList(WCommand, CommandSpec, RegistrationTicket)}
     */
    private final CommandList subCommands = new CommandList(this);

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
        this(0, name, description, arguments, isOptional, prefix, suffix, defaultHandler);
    }

    /**
     * Create a new CommandSpec instance
     *
     * @param priority Command priority
     * @param name           Name of command
     * @param description    Description of command
     * @param arguments      Arguments
     * @param isOptional     Is Optional??
     * @param prefix         Prefix
     * @param suffix         Suffix
     * @param defaultHandler CommandSpec handler
     */
    public CommandSpec(int priority, Text name, String description, Arguments arguments, @Deprecated boolean isOptional, String prefix, String suffix, Handler defaultHandler) {
        this.priority = priority;
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

    public static CommandSpec withHandledCommandList(WCommand<?> wCommand, CommandSpec commandSpec, RegistrationTicket<?> ticket) {
        CommandSpec newCommandSpec = new CommandSpec(commandSpec.getPriority(), commandSpec.getName(), commandSpec.getDescription(), commandSpec.getArguments(), commandSpec.isOptional(), commandSpec.getPrefix(), commandSpec.getSuffix(), commandSpec.getDefaultHandler());

        newCommandSpec.addSubs(new CommandList(wCommand, newCommandSpec), ticket);

        if (commandSpec.getSubCommands().size() > 0) {
            for (CommandSpec childCommandSpec : commandSpec.getSubCommands()) {
                newCommandSpec.addSub(withHandledCommandList(wCommand, childCommandSpec, ticket), ticket);
            }
        }

        return newCommandSpec;
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
    public CommandSpec addSub(CommandSpec commandSpec, RegistrationTicket<?> ticket) {
        this.subCommands.add(commandSpec, ticket);
        return this;
    }

    /**
     * Add child commandSpec
     *
     * @param commandSpec CommandSpec
     * @return This
     */
    @Deprecated
    public CommandSpec addSub(CommandSpec commandSpec) {
        this.subCommands.add(commandSpec, RegistrationTicket.empty(this));
        return this;
    }

    /**
     * Add child commandSpecs*
     *
     * @param commandSpecs Commands*
     * @return This
     */
    public CommandSpec subCommands(RegistrationTicket<?> ticket, CommandSpec... commandSpecs) {
        addSubs(ticket, commandSpecs);
        return this;
    }

    /**
     * Add List of child commandSpecs
     *
     * @param commandSpecs Commands
     * @return This
     */
    public CommandSpec addSubs(List<CommandSpec> commandSpecs, RegistrationTicket<?> ticket) {
        if (commandSpecs.size() > 0)
            this.subCommands.addAll(commandSpecs, ticket);
        return this;
    }

    /**
     * Add child commandSpecs*
     *
     * @param commandSpecs Child commandSpecs
     * @return This
     */
    public CommandSpec addSubs(RegistrationTicket<?> ticket, CommandSpec... commandSpecs) {
        this.subCommands.addAll(Arrays.asList(commandSpecs), ticket);
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
    public CommandList getSubCommands() {
        return new CommandList(Collections.unmodifiableList(subCommands), this);
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
     * Gets command priority.
     * @return Command priority
     */
    public int getPriority() {
        return this.priority;
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

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CommandSpec))
            return super.equals(obj);

        CommandSpec spec = (CommandSpec) obj;

        Optional<Text> textOptional = Functions.return_(spec.allTexts(), nameOrAlias -> spec.getName().compareTo(nameOrAlias) == 0 || spec.getAliases().contains(nameOrAlias));

        return textOptional.isPresent() || super.equals(obj);

    }

}
