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
package com.github.jonathanxd.wcommands.commandapi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.factory.ArgumentBuilder;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.Optional;

/**
 * Created by jonathan on 28/02/16.
 */
public class Readme {

    public static void main(String[] args) throws ArgumentProcessingError {
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

                    //System.out.println("[SAY] "+str.orElse("FOO"));

                })
                .withArgument(
                        ArgumentBuilder
                                .<ID, String>builder()
                                .withId(ID.TEXT)
                                .withTextPredicate(text->true)
                                .withConverter(String::toString)
                                .setOptional(true)
                                .build()
                ).build();

        manager.registerCommand(spec);

        manager.processAndInvoke("say", "foo bar");

        // Intercept handler

        manager.addInterceptor((commandData, handler) -> {
            Optional<ArgumentHolder<ID, Object>> argumentHolder = commandData.getCommand().getArgument(ID.TEXT);
            argumentHolder.ifPresent(v -> v.setValue("ad"));
        });

        manager.processAndInvoke("say", "foo bar");
    }

    public enum ID {
        TEXT
    }

}
