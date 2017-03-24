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
package com.github.jonathanxd.wcommands.commandapi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.factory.ArgumentBuilder;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Test;

import java.util.Optional;

public class Readme {

    @Test
    public void readme() throws ProcessingError {
        WCommandCommon manager = new WCommandCommon();

        CommandSpec spec = CommandBuilder.builder()
                .withName(Text.of("say"))
                .withCommonHandler((c, req, data) -> {
                    // DO A ACTION
                    CommandHolder ch = c.getCommand();

                    Optional<String> str = ch.getArgValue(ID.TEXT);

                    if(str.isPresent()) {
                        System.out.println("[SAY] "+str.get());
                    }else{
                        System.out.println("[SAY] FOO");
                    }
                    return null;
                    //System.out.println("[SAY] "+str.orElse("FOO"));

                })
                .withArgument(
                        ArgumentBuilder
                                .<ID, String>builder()
                                .withId(ID.TEXT)
                                .withTextPredicate(text->true)
                                .withConverter(Object::toString)
                                .setOptional(true)
                                .build()
                ).build();

        manager.getRegister(new CommonTicket<>(this)).registerCommand(spec);

        manager.processAndInvoke("say", "foo bar");

        // Intercept handler

        manager.addInterceptor((commandData, handler) -> {
            Optional<ArgumentHolder<ID, Object>> argumentHolder = commandData.getCommand().getArgument(ID.TEXT);
            argumentHolder.ifPresent(v -> v.setValues("ad"));
        });

        manager.processAndInvoke("say", "foo bar");
    }

    public enum ID {
        TEXT
    }

}
