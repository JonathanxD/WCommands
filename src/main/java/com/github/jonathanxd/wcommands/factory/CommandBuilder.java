/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands.factory;

import com.github.jonathanxd.wcommands.CommonHandler;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.arguments.Arguments;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by jonathan on 27/02/16.
 */
public class CommandBuilder<H> {
    private Text name;
    private String description = "";
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

    public CommandBuilder<H> withDescription(String description) {
        this.description = description;
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

    @Deprecated
    public CommandSpec build() {
        return new CommandSpec(Objects.requireNonNull(name),
                description,
                Objects.requireNonNull(arguments),
                isOptional,
                Objects.requireNonNull(prefix),
                Objects.requireNonNull(suffix),
                Objects.requireNonNull(handler)).addSubs(preChilds, RegistrationTicket.empty(this));
    }

    public CommandSpec build(RegistrationTicket<?> ticket) {
        return new CommandSpec(Objects.requireNonNull(name),
                description,
                Objects.requireNonNull(arguments),
                isOptional,
                Objects.requireNonNull(prefix),
                Objects.requireNonNull(suffix),
                Objects.requireNonNull(handler)).addSubs(preChilds, ticket);
    }

}
