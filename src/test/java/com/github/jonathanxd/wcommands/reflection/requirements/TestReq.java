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
