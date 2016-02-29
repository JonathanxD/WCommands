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

import com.github.jonathanxd.wcommands.CommonHandler;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by jonathan on 27/02/16.
 */
public class CommandBuilder<H> {
    private Text name;
    private String prefix = "";
    private String suffix = "";
    private boolean isOptional = false;
    private Handler<H> handler;
    private Arguments arguments = new Arguments();
    private List<CommandSpec> preChilds = new ArrayList<>();

    private CommandBuilder() {
    }

    public static <H> CommandBuilder<H> builder() {
        return new CommandBuilder<>();
    }

    public CommandBuilder<H> withName(Text name) {
        this.name = name;
        return this;
    }

    public CommandBuilder<H> withName(String name) {
        this.name = Text.of(name);
        return this;
    }

    public CommandBuilder<H> withRegexName(String name) {
        this.name = Text.ofRegex(name);
        return this;
    }

    public CommandBuilder<H> withIgnoreCaseName(String name) {
        this.name = Text.ofIgnoreCase(name);
        return this;
    }

    public CommandBuilder<H> withRegexIgnoreCaseName(String name) {
        this.name = Text.ofRegexIgnoreCase(name);
        return this;
    }

    public CommandBuilder<H> withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandBuilder<H> withSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public CommandBuilder<H> withIsOptional(boolean isOptional) {
        this.isOptional = isOptional;
        return this;
    }

    public CommandBuilder<H> withHandler(Handler<H> defaultHandler) {
        this.handler = defaultHandler;
        return this;
    }

    @SuppressWarnings("unchecked")
    public CommandBuilder<H> withCommonHandler(CommonHandler commonHandler) {
        this.handler = (Handler<H>) commonHandler;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <ID, T> CommandBuilder<H> withValueHandler(CommonHandler.Value<ID, T> commonHandler) {
        this.handler = (Handler<H>) commonHandler;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <ID> CommandBuilder<H> withAnyValueHandler(CommonHandler.AnyValue<ID> commonHandler) {
        this.handler = (Handler<H>) commonHandler;
        return this;
    }


    public CommandBuilder<H> withArguments(Arguments arguments) {
        this.arguments = arguments;
        return this;
    }

    public CommandBuilder<H> withArgument(ArgumentSpec<?, ?> argumentSpec) {
        this.arguments.add(argumentSpec);
        return this;
    }

    public CommandBuilder<H> withChild(CommandSpec child) {
        this.preChilds.add(child);
        return this;
    }


    public CommandSpec build() {
        return new CommandSpec(Objects.requireNonNull(name),
                Objects.requireNonNull(arguments),
                isOptional,
                Objects.requireNonNull(prefix),
                Objects.requireNonNull(suffix),
                Objects.requireNonNull(handler)).addSubs(preChilds);
    }
}
