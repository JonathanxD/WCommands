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
package com.github.jonathanxd.wcommands.reflection.list;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.infos.require.Require;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.ProvidedRequirement;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.result.Result;
import com.github.jonathanxd.wcommands.result.Results;

import org.junit.Test;

import java.util.List;

/**
 * Created by jonathan on 18/03/16.
 */
public class TestList {

    @Test
    public void listTest() {
        ProvidedRequirement providedRequirement = (data, reg) -> {
            Sender sender = reg.<Sender>getOptional(Sender.class).get();
            return sender.hasPerm(data);
        };

        InformationRegister informationRegister = InformationRegister.blankBuilder().with(Sender.class, new Sender()).build();

        Requirements requirements = new Requirements();
        requirements.add(Permission.class, providedRequirement);


        WCommandCommon wCommandCommon = ReflectionAPI.createWCommand(new TestList());

        Results results = wCommandCommon.processAndInvoke(requirements, informationRegister, "show", "list", "a", "b", "c", "named", "Xy");

        System.out.println("Results: "+results);

        Results results2 = wCommandCommon.processAndInvoke(requirements, informationRegister, "myList", "A", "B", "C");

    }

    static class Sender{
        boolean hasPerm(String perm) {
            return perm.equals("dup");
        }
    }

    class Permission {}

    enum EN {
        A,
        B,
        C
    }

    @Command
    public void myList(@Argument(isInfinite = true) List<EN> ens) {
        System.out.println("En: "+ens);
        EN en = ens.get(0);
    }

    @Command
    @Require(type = Permission.class, data = "dup")
    public String show() {
        return "AAB";
    }

    @SubCommand({"show"})
    public Result<List<String>> list(@Argument(isInfinite = true) List<String> stringList) {
        System.out.println("A List "+stringList);
        return new Result<>(stringList, IDs.DATA);
    }

    @SubCommand({"show"})
    public String named(@Argument String name,
                        @Info Results results) {
        System.out.println("Results: "+results.find(IDs.DATA));
        return "WM";
    }

    enum IDs {
        DATA
    }
}
