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
package com.github.jonathanxd.wcommands.logi;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.factory.CommandFactory;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by jonathan on 21/03/16.
 */
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
