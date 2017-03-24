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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;

public class NumberTranslator implements Translator<Number> {
    public static final Pattern NUMBER_REGEX = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");

    private static Number numberConversion(String data) {

        if (data.contains(".")) {
            try {
                return Long.valueOf(data);
            } catch (Throwable ignored) {
            }
            try {
                return Double.valueOf(data);
            } catch (Throwable ignored) {
            }
            return new BigDecimal(data);
        }

        try {
            return Byte.valueOf(data);
        } catch (Throwable ignored) {
        }

        try {
            return Short.valueOf(data);
        } catch (Throwable ignored) {
        }
        try {
            return Integer.valueOf(data);
        } catch (Throwable ignored) {
        }
        try {
            return Long.valueOf(data);
        } catch (Throwable ignored) {
        }

        try {
            return new BigInteger(data);
        } catch (Throwable ignored) {
        }

        throw new IllegalArgumentException("Cannot convert '" + data + "' to Number Representation!");
    }

    @Override
    public boolean isAcceptable(List<String> text) {
        return !text.isEmpty() && NUMBER_REGEX.matcher(text.get(0)).matches();
    }

    @Override
    public Number translate(List<String> text) {
        return numberConversion(text.get(0));
    }

}
