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
package com.github.jonathanxd.wcommands.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.StringJoiner;

public class ToString {

    public static String toString(Object object) {

        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");

        Class<?> clazz = object.getClass();

        for (Field f : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers()))
                continue;

            f.setAccessible(true);
            try {
                Object o = f.get(object);

                if (o != null && object.getClass().isAssignableFrom(o.getClass())) {
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
