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
package com.github.jonathanxd.wcommands.reflection;

import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import org.junit.Test;

import java.util.Optional;

/**
 * Created by jonathan on 03/04/16.
 */
public class TestProvided {

    @Test
    public void testProvided() {
        ReflectionCommandProcessor reflectionCommandProcessor = ReflectionAPI.createWCommand();

        reflectionCommandProcessor.getRegister(RegistrationTicket.empty(this)).addCommands(this);

        InformationRegister informationRegister = new InformationRegister();

        informationRegister.register(new InfoId("name", String.class), "Name");

        informationRegister.register((requestId, requestingType) -> {
            if(requestingType == String.class) {
                return Optional.of(new Information<>(requestId, "Alt"));
            }

            return Optional.empty();
        });

        reflectionCommandProcessor.processAndInvoke(informationRegister, "provided");

    }

    @Command
    public void provided(@Info(staticFirst = false) String name) {
        System.out.println("Name = "+name);
    }

}
