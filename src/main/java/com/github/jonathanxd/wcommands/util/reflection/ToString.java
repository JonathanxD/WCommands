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
package com.github.jonathanxd.wcommands.util.reflection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * Created by jonathan on 19/03/16.
 */
public class ToString {

    public static String toString(Object object) {

        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");

        Class<?> clazz = object.getClass();

        for(Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                Object o = f.get(object);

                if(o instanceof Collection) {
                    stringJoiner.add(f.getName() + " = {" + f.get(object) + "}");
                }else {
                    stringJoiner.add(f.getName() + " = '" + f.get(object) + "'");
                }
            } catch (IllegalAccessException ignored) {}
        }

        return stringJoiner.toString();
    }

}
