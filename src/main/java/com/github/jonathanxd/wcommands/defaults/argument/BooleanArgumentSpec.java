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
package com.github.jonathanxd.wcommands.defaults.argument;

import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.function.Supplier;

/**
 * Boolean argument specification.
 *
 * @param <ID> Id of argument.
 */
public class BooleanArgumentSpec<ID> extends ArgumentSpec<ID, Boolean> {

    private static final Supplier<Matchable<String>> BOOLEAN_CHECKER = () -> Text.of("(true|false)", true, true);

    public BooleanArgumentSpec(ID id, boolean optional, boolean infinite) {
        super(id, TypeInfo.of(Boolean.TYPE), infinite, BOOLEAN_CHECKER, s -> true, optional, d -> d.isEmpty() ? false : Boolean.valueOf(d.get(0)));
    }
}
