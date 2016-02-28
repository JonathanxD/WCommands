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

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.exceptions.ArgumentError;
import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.enums.EnumPredicate;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.enums.EnumTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;

public class TestCMD {

    public static void main(String[] args) throws ArgumentProcessingError {

        TestCMD testAnnotations = new TestCMD();

        WCommandCommon commandCommon = ReflectionAPI.createWCommand(testAnnotations);

        commandCommon.processAndInvoke("give", "xp", "14");

    }

    @Command
    public void give(@Argument(predicate = EnumPredicate.class, translator = EnumTranslator.class) GiveType giveType,
                     @Argument(predicate = EnumPredicate.class, translator = EnumTranslator.class, isOptional = true) Items what,
                     @Argument int amount) {

        if (giveType == GiveType.ITEM) {
            System.out.println("Giving item name: '" + what + "', amount: " + amount);
        } else if (giveType == GiveType.XP) {
            System.out.println("Giving xp, amount: " + amount);
        }


    }

    public enum GiveType {
        ITEM,
        XP
    }

    public enum Items {
        DIAMOND,
        GOLD,
        IRON
    }

    public static class MyErrorHandler implements ErrorHandler {

        @Override
        public boolean handle(ArgumentProcessingError error) {

            return error.getType().getExceptionType() != ArgumentError.Type.ERROR;

        }
    }
}
