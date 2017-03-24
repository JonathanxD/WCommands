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
package com.github.jonathanxd.wcommands.logi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class EqualTest {

    @Test
    public void commandSpecEqualTest() {

        CommandSpec commandSpec = CommandFactory
                .commandBuilder()
                .withName("call")
                .withCommonHandler((commandData, requirements, informationRegister) -> {
                    System.out.println("Call master!");
                    return null;
                })
                .build();

        WCommandCommon wCommandCommon = new WCommandCommon();

        wCommandCommon.getRegister(new CommonTicket<>(this)).registerCommand(commandSpec);

        Optional<CommandSpec> callCommand = wCommandCommon.getCommand("call");


        if (callCommand.isPresent()) {
            int mySpecIdentity = System.identityHashCode(commandSpec);
            int registeredIdentity = System.identityHashCode(callCommand.get());

            System.out.printf("MySpec: %d%n", mySpecIdentity);
            System.out.printf("Registered: %d%n", registeredIdentity);

            // Will NOT throw an exception
            Assert.assertNotEquals(mySpecIdentity, registeredIdentity);

            // The System creates a clone of CommandSpec
            System.out.printf("MySpec sub-command list: %s%n", commandSpec.getSubCommands().hashCode());
            System.out.printf("Registered sub-command list: %s%n", commandSpec.getSubCommands().hashCode());

            /**
             *  When you create a command, a sub-command list will be created inside CommandSpec,
             *  but the sub-command list doesn't known about the RegistrationHandlers
             *  Then, when you register command in a processor (WCommand extensors),
             *  the processor will create another identical CommandSpec with WCommand instance to handle command registrations.
             *  You can unregister commands because 'equals' method test the equality based on name and aliases!
             */

        }

        // Will works
        wCommandCommon.unregisterCommand(commandSpec);

        callCommand = wCommandCommon.getCommand("call");

        System.out.printf("CallCommand: %s%n", callCommand);

        Assert.assertFalse(callCommand.isPresent());

    }

}
