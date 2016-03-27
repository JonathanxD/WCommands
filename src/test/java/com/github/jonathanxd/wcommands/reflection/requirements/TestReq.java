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
package com.github.jonathanxd.wcommands.reflection.requirements;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.infos.require.Require;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.ProvidedRequirement;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.result.Results;

import org.junit.Test;

/**
 * Created by jonathan on 18/03/16.
 */
public class TestReq {

    @Test
    public void requirementsTest() {
        ProvidedRequirement providedRequirement = (data, parameters, commandData, reg, subject) -> {
            Sender sender = (Sender) subject.get();

            return sender.hasPerm(data);
        };

        InformationRegister informationRegister = InformationRegister.blankBuilder().with(Sender.class, new Sender()).build();

        Requirements requirements = new Requirements();
        requirements.add(Permission.class, providedRequirement);


        WCommandCommon wCommandCommon = ReflectionAPI.createWCommand(new TestReq());

        Results results = wCommandCommon.processAndInvoke(requirements, informationRegister, "test1");

        System.out.println("Results: "+results);
    }

    static class Sender{
        boolean hasPerm(String perm) {
            return perm.equals("dup");
        }
    }

    class Permission {}

    @Command
    @Require(type = Permission.class, data = "dup", subject = @Info(type = Sender.class))
    public String test1() {
        System.out.println("TEST WITH PERM");
        return "AAB";
    }

}
