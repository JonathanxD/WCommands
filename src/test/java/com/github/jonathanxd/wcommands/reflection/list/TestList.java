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
package com.github.jonathanxd.wcommands.reflection.list;

import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.infos.require.Require;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.ProvidedRequirement;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.result.Result;
import com.github.jonathanxd.wcommands.result.Results;

import org.junit.Test;

import java.util.List;

public class TestList {

    @Test
    public void listTest() {
        ProvidedRequirement providedRequirement = (data, parameters, commandData, informationRegister, subject) -> {
            Sender sender = (Sender) subject.get();

            return sender.hasPerm(data);
        };

        InformationRegister informationRegister = InformationRegister.blankBuilder().with(Sender.class, new Sender(), TypeInfo.of(Sender.class)).build();

        Requirements requirements = new Requirements();
        requirements.add(Permission.class, providedRequirement);


        WCommandCommon wCommandCommon = ReflectionAPI.createWCommand((error, commandSpecs, currentCommand, processed, requirements1, informationRegister1) -> {
            System.out.println("E -> " + error);
            return ProcessAction.CONTINUE;
        }, new TestList());

        wCommandCommon.registerRegistrationHandler((registrationHandleResults, targetList, manager, ticket) -> {
            return RegistrationHandleResult.accept();
        });

        //Results results = wCommandCommon.processAndInvoke(requirements, informationRegister, "show", "list", "a", "b", "c", "named", "Xy");

        wCommandCommon.processAndInvoke(requirements, informationRegister, "show", "list", "a", "b", "c", "named", "Xy", "zNamed", "Hh");

        /*wCommandCommon.processAndInvoke(requirements, informationRegister, "show", "list", "a", "b", "c", "&", "zNamed", "Xy");

        Results results3 = wCommandCommon.processAndInvoke(requirements, informationRegister, "show", "named", "Xy");

        System.out.println("Results: "+results);

        Results results2 = wCommandCommon.processAndInvoke(requirements, informationRegister, "myList", "A", "B", "C");*/

    }

    @Command
    public void myList(@Argument(isArray = true) List<EN> ens) {
        System.out.println("En: " + ens);
        EN en = ens.get(0);
    }

    @Command
    public void zNamed(@Argument String key) {
        System.out.println("zNamed = " + key);
    }

    @Command
    @Require(type = Permission.class, data = "dup")
    public String show() {
        return "AAB";
    }

    @SubCommand({"show"})
    public Result<List<String>> list(@Argument(isArray = true) List<String> stringList) {
        System.out.println("A List " + stringList);
        return new Result<>(IDs.DATA, stringList);
    }

    @SubCommand({"show"})
    public String named(@Argument String name,
                        @Info Results results) {
        System.out.println("named!");
        System.out.println("Results: " + results.find(IDs.DATA));
        return "WM";
    }

    enum EN {
        A,
        B,
        C
    }

    enum IDs {
        DATA
    }

    static class Sender {
        boolean hasPerm(String perm) {
            return perm.equals("dup");
        }
    }

    class Permission {
    }
}
