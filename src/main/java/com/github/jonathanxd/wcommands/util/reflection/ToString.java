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
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * Created by jonathan on 19/03/16.
 */
public class ToString {

    public static String toString(Object object) {

        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");

        Class<?> clazz = object.getClass();

        for (Field f : clazz.getDeclaredFields()) {
            if(Modifier.isStatic(f.getModifiers()))
                continue;

            f.setAccessible(true);
            try {
                Object o = f.get(object);

                if(o != null && object.getClass().isAssignableFrom(o.getClass())) {
                    stringJoiner.add(f.getName() + " = { ? }");
                    continue;
                }

                if (o instanceof Collection) {
                    stringJoiner.add(f.getName() + " = {" + f.get(object) + "}");
                    continue;
                } else if (o != null) {
                    if (o.getClass().isArray()) {
                        StringJoiner joiner = new StringJoiner(", ", "(", ")");

                        for (Object oth : (Object[]) o) {
                            joiner.add(String.valueOf(oth));
                        }

                        stringJoiner.add(f.getName() + " = " + joiner.toString());
                        continue;
                    }

                    if (o instanceof Class) {
                        stringJoiner.add(f.getName() + " = '" + ((Class) o).getSimpleName() + "'");
                        continue;
                    }
                }
                stringJoiner.add(f.getName() + " = '" + o + "'");

            } catch (IllegalAccessException ignored) {
            }
        }

        return stringJoiner.toString();
    }

}
