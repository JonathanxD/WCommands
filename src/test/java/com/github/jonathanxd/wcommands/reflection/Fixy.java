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
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;

/**
 * Created by jonathan on 19/03/16.
 */
public class Fixy {

    public static void main(String[] args) {
        ReflectionCommandProcessor wCommandCommon = ReflectionAPI.createWCommand((error, commandSpecs, currentCommand, processed, requirements, informationRegister) -> {
            error.printStackTrace();
            return false;
        }, new Fixy.Vim());

        wCommandCommon.addCommands(new Fixy.DD());


        wCommandCommon.processAndInvoke("simple");

    }

    @Command(name = "vim")
    public static final class Vim {
        @Command
        public void p(@Argument String a, @Argument String b) {
            System.out.println("VSimple");
        }

    }


    public static final class DD {
        @Command
        public void simple() {
            System.out.println("Simple");
        }
    }

}
