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

public class Primitive {

    @SuppressWarnings("UnnecessaryUnboxing")
    public static Object castToPrimitive(Object object) {
        Class<?> type = object.getClass();

        if (Integer.class == type) {
            Integer i = (Integer) object;
            return i.intValue();
        } else if (Byte.class == type) {
            Byte b = (Byte) object;
            return b.byteValue();
        } else if (Boolean.class == type) {
            Boolean b = (Boolean) object;
            return b.booleanValue();
        } else if (Short.class == type) {
            Short s = (Short) object;
            return s.shortValue();
        } else if (Long.class == type) {
            Long l = (Long) object;
            return l.longValue();
        } else if (Float.class == type) {
            Float f = (Float) object;
            return f.floatValue();
        } else if (Double.class == type) {
            Double d = (Double) object;
            return d.doubleValue();
        } else if (Character.class == type) {
            Character c = (Character) object;
            return c.charValue();
        } else {
            return object;
        }
    }

    public static Class<?> asBoxed(Class<?> type) {
        if (int.class == type) {
            return Integer.class;
        } else if (byte.class == type) {
            return Byte.class;
        } else if (boolean.class == type) {
            return Boolean.class;
        } else if (short.class == type) {
            return Short.class;
        } else if (long.class == type) {
            return Long.class;
        } else if (float.class == type) {
            return Float.class;
        } else if (double.class == type) {
            return Double.class;
        } else if (char.class == type) {
            return Character.class;
        } else {
            return null;
        }
    }

    public static Class<?> asUnboxed(Class<?> type) {
        if (Integer.class == type) {
            return int.class;
        } else if (Byte.class == type) {
            return byte.class;
        } else if (Boolean.class == type) {
            return boolean.class;
        } else if (Short.class == type) {
            return short.class;
        } else if (Long.class == type) {
            return long.class;
        } else if (Float.class == type) {
            return float.class;
        } else if (Double.class == type) {
            return double.class;
        } else if (Character.class == type) {
            return char.class;
        } else {
            return null;
        }
    }
}
