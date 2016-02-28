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

import com.github.jonathanxd.wcommands.CommonHandler;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.defaults.argument.BooleanArgumentSpec;
import com.github.jonathanxd.wcommands.exceptions.ArgumentError;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.Optional;

public class TestLT {

    // --allowUpper false --daemon --rail true


    public static void main(String[] args) throws ArgumentProcessingError {
        WCommandCommon wCommandCommon = new WCommandCommon(new CommonProcessor(), new MyErrorHandler());
        /*wCommandCommon.addCommand(CommandFactory.create("allowUpper",
                new BooleanArgumentSpec<IDs>(IDs.ALLOW_UPPER, false),
                false,
                "--"));*/
        wCommandCommon.addCommand(CommandBuilder.builder()
                .withPrefix("--")
                .withName(Text.of("allowUpper"))
                .withArgument(new BooleanArgumentSpec<>(IDs.ALLOW_UPPER, false))
                .withCommonHandler(commandData -> {
                    CommandHolder holder = commandData.getCommand();

                    Optional<Boolean> isAllowUpper = holder.getArgValue(IDs.ALLOW_UPPER);

                    if (isAllowUpper.isPresent()) {
                        if (isAllowUpper.get()) {
                            System.out.println("Allowed upper case");
                        } else {
                            System.out.println("Disallowed upper case");
                        }
                    }

                })
                .build()
        );
        wCommandCommon.addCommand(CommandBuilder.builder()
                .withPrefix("--")
                .withName(Text.of("daemon"))
                .withCommonHandler(commandData -> System.out.println("Start Daemon"))
                .build());

        wCommandCommon.addCommand(CommandBuilder.builder()
                .withPrefix("--")
                .withName(Text.of("rail"))
                .withArgument(new BooleanArgumentSpec<>(IDs.RAIL, false))
                .withValueHandler(new CommonHandler.Value<IDs, Boolean>(IDs.RAIL) {
                    @Override
                    public void handle(Boolean value) {
                        if (value) {
                            System.out.println("Allowed rail");
                        } else {
                            System.out.println("Disallowed rail");
                        }
                    }
                })
                .build()
        );

        wCommandCommon.processAndInvoke("--allowUpper", "false", "--daemon", "--rail", "false");
    }

    public enum IDs {
        ALLOW_UPPER,
        RAIL
    }

    public static class MyErrorHandler implements ErrorHandler {

        @Override
        public boolean handle(ArgumentProcessingError error) {

            return error.getType().getExceptionType() != ArgumentError.Type.ERROR;

        }
    }
}
