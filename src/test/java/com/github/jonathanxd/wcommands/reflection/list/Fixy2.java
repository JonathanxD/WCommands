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
package com.github.jonathanxd.wcommands.reflection.list;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.reflection.Fixy;
import com.github.jonathanxd.wcommands.ticket.CommonTicket;

import org.junit.Test;

import java.util.List;

/**
 * Created by jonathan on 24/03/16.
 */
public class Fixy2 {
    @Command(prefix = "--")
    public void high(@Argument(id = "high", isArray = true) List<String> highs,
                     @Info(type = Fixy.class) Fixy fixy,
                     @Info(type = Fixy2.class) Fixy2 fixy2) {

        System.out.println("Highs: "+highs);
        System.out.println("Fixy: "+fixy);
        System.out.println("Fixy2: "+fixy2);
    }

    @Command(prefix = "--")
    public void low(@Argument(id = "low", isArray = true) List<String> lows,
                    @Info(type = Fixy.class) Fixy fixy) {

        System.out.println("Lows: "+lows);
        System.out.println("Fixy: "+fixy);

    }


    @Test
    public void test() {
        ReflectionCommandProcessor processor = new ReflectionCommandProcessor();

        processor.getRegister(new CommonTicket<>(this)).addCommands(this);

        processor.processAndInvoke(InformationRegister
                .blankBuilder()
                .with(Fixy.class, new Fixy())
                .with(Fixy2.class, this).build(), "--high", "jk", "--low", "dll");
    }
}
