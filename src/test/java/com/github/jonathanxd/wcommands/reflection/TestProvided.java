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

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
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

        informationRegister.register(new InfoId("name", String.class), "Name", Reference.aEnd(String.class));

        informationRegister.register((requestId, requestingType) -> {
            if(requestingType.compareTo(Reference.aEnd(String.class)) == 0) {
                return Optional.of(new Information<>(requestId, "Alt", Reference.aEnd(String.class)));
            }
            if(requestingType.compareToAssignable(Reference.a(Collection.class).of(String.class).build()) == 0) {
                return Optional.of(new Information<Collection<String>>(requestId, Arrays.asList("A", "D", "B"), Reference.a(Collection.class).of(String.class).build()));
            }

            return Optional.empty();
        });

        reflectionCommandProcessor.processAndInvoke(informationRegister, "provided");

    }

    @Command
    public void provided(@Info(staticFirst = false) String name,
                         @Info Information<Collection<String>> gamers) {
        System.out.println("Name = "+name);
    }

}
