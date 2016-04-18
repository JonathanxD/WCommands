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
package com.github.jonathanxd.wcommands.reflection;

import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Test;

/**
 * Created by jonathan on 19/03/16.
 */
public class Fixy {

    @Test
    public void fixyTest() {
        ReflectionCommandProcessor wCommandCommon = ReflectionAPI.createWCommand((error, commandSpecs, currentCommand, processed, requirements, informationRegister) -> {
            error.printStackTrace();
            return ProcessAction.STOP;
        }, new Fixy.Vim());

        wCommandCommon.registerRegistrationHandler((registrationHandleResults, targetList, manager, ticket) -> {
            System.out.println("Received: "+registrationHandleResults + " -from> "+targetList.getHoldingObject());
            return RegistrationHandleResult.accept();
        });

        wCommandCommon.getRegister(new CommonTicket<>(this)).addCommands(new Fixy.DD());


        wCommandCommon.processAndInvoke("vim", "p", "ads", "dld");

    }

    @Command(name = "vim")
    public static final class Vim {
        @Command
        public void p(@Argument String a, @Argument String b) {
            System.out.println("VSimple + "+a+" -> "+b);
        }

    }


    public static final class DD {
        @Command
        public void simple() {
            System.out.println("Simple");
        }
    }

}
