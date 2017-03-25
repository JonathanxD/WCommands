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
package com.github.jonathanxd.wcommands.reflection;

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.exceptions.ProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Assert;

import org.junit.Test;

public class TestAnnotations {

    // --allowUpper false --daemon --rail true

    @Test
    public void testAnnotations() throws ProcessingError {

        ReflectionCommandProcessor commandCommon = new ReflectionCommandProcessor(new CommonProcessor(), new MyErrorHandler());

        TestAnnotations testAnnotations = new TestAnnotations();

        commandCommon.getRegister(new CommonTicket<>(this)).addCommands(testAnnotations, TestAnnotations.class);

        commandCommon.processAndInvoke("assertOptions", "--allowUpper", "true", "--daemon", "--rail", "false");

    }

    @Command(prefix = "--")
    @Argument
    private boolean allowUpper = false;

    @Command(prefix = "--")
    @Argument(isOptional = true)
    private boolean daemon = false;

    @Command(prefix = "--")
    @Argument
    public boolean rail = true;

    @Command(priority = 1)
    public void assertOptions() {
        System.out.println("Allow Upper? "+this.allowUpper);
        System.out.println("Is Daemon? "+this.daemon);
        System.out.println("Rail? "+this.rail);

        Assert.assertEquals(this.allowUpper, true);
        Assert.assertEquals(this.daemon, true);
        Assert.assertEquals(this.rail, false);

    }

    public static class MyErrorHandler implements ErrorHandler {

        @Override
        public ProcessAction handle(ProcessingError error, CommandList commandSpecs, CommandSpec current, Object processed, Requirements requirements, InformationRegister informationRegister) {
            error.printStackTrace();
            return (error.getType().getExceptionType() != ErrorType.Type.ERROR) ? ProcessAction.CONTINUE : ProcessAction.STOP;
        }
    }
}
