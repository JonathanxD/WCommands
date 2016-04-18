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
package com.github.jonathanxd.wcommands.exceptions;

/**
 * Created by jonathan on 27/02/16.
 */
public enum ErrorType {
    MISSING_ARGUMENT(Type.ERROR),
    MISSING_COMMAND(Type.ERROR),
    MISSING_SUB_COMMAND(Type.ERROR),
    NO_COMMAND_PROVIDED(Type.ERROR),
    POSSIBLE_BUG(Type.POSSIBLE_BUG),
    FAIL(Type.FAIL);

    private final Type type;

    ErrorType(Type type) {
        this.type = type;
    }

    public Type getExceptionType() {
        return type;
    }

    public enum Type {
        /**
         * Indicates a possible bug in the Processor
         */
        POSSIBLE_BUG,
        /**
         * Indicates a error occurred during Parse Process.
         */
        ERROR,
        /**
         * Indicates a Severe System Fail!
         */
        FAIL
    }
}
