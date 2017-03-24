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

import com.github.jonathanxd.wcommands.CommonHandler;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.defaults.argument.BooleanArgumentSpec;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.factory.CommandBuilder;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Test;

import java.util.Optional;

public class TestLT {

    // --allowUpper false --daemon --rail true


    @Test
    public void firstTest() throws ProcessingError {
        WCommandCommon wCommandCommon = new WCommandCommon(new CommonProcessor());
        /*wCommandCommon.registerCommand(CommandVisitor.create("allowUpper",
                new BooleanArgumentSpec<IDs>(IDs.ALLOW_UPPER, false),
                false,
                "--"));*/
        wCommandCommon.getRegister(new CommonTicket<>(this)).registerCommand(CommandBuilder.builder()
                .withPrefix("--")
                .withName(Text.of("allowUpper"))
                .withArgument(new BooleanArgumentSpec<>(IDs.ALLOW_UPPER, false, false))
                .withCommonHandler((commandData, requirements, ref) -> {
                    CommandHolder holder = commandData.getCommand();

                    Optional<Boolean> isAllowUpper = holder.getArgValue(IDs.ALLOW_UPPER);

                    if (isAllowUpper.isPresent()) {
                        if (isAllowUpper.get()) {
                            System.out.println("Allowed upper case");
                        } else {
                            System.out.println("Disallowed upper case");
                        }
                    }
                    return null;
                })
                .build()
        );
        wCommandCommon.getRegister(new CommonTicket<>(this)).registerCommand(CommandBuilder.builder()
                .withPrefix("--")
                .withName(Text.of("daemon"))
                .withCommonHandler((commandData, requirements, ref) -> {System.out.println("Start Daemon"); return null;})
                .build());

        wCommandCommon.getRegister(new CommonTicket<>(this)).registerCommand(CommandBuilder.builder()
                .withPrefix("--")
                .withName(Text.of("rail"))
                .withArgument(new BooleanArgumentSpec<>(IDs.RAIL, false, false))
                .withValueHandler(new CommonHandler.Value<IDs, Boolean>(IDs.RAIL) {
                    @Override
                    public Object handle(Boolean value) {
                        if (value) {
                            System.out.println("Allowed rail");
                        } else {
                            System.out.println("Disallowed rail");
                        }

                        return null;
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
        public ProcessAction handle(ProcessingError error, CommandList commandSpecs, CommandSpec current, Object processed, Requirements requirements, InformationRegister register) {
            return (error.getType().getExceptionType() != ErrorType.Type.ERROR) ? ProcessAction.CONTINUE : ProcessAction.STOP;
        }
    }
}
