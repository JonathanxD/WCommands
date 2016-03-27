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
    public void high(@Argument(id = "high", isInfinite = true) List<String> highs,
                     @Info(type = Fixy.class) Fixy fixy,
                     @Info(type = Fixy2.class) Fixy2 fixy2) {

        System.out.println("Highs: "+highs);
        System.out.println("Fixy: "+fixy);
        System.out.println("Fixy2: "+fixy2);
    }

    @Command(prefix = "--")
    public void low(@Argument(id = "low", isInfinite = true) List<String> lows,
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
