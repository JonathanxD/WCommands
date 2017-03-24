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
package com.github.jonathanxd.wcommands.common.alias;

import com.github.jonathanxd.wcommands.text.Text;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List of all command aliases
 */
public class AliasList extends ArrayList<Text> {
    public AliasList() {
        super();
    }

    public AliasList(Collection<? extends Text> c) {
        super(c);
    }

    /**
     * Returns true if any alias match the input string {@code str}.
     *
     * @param str        Alias string.
     * @param ignoreCase Ignore case.
     * @return True if any alias match the input string {@code str}.
     */
    public boolean anyMatches(String str, boolean ignoreCase) {
        for (Text text : this) {
            if (Text.matches(text, str, ignoreCase))
                return true;

        }
        return false;
    }

    /**
     * Returns true if any alias match the input string {@code str}.
     *
     * @param str Alias string.
     * @return True if any alias match the input string {@code str}.
     */
    public boolean anyMatches(String str) {
        return this.anyMatches(str, false);
    }

    /**
     * Create a copy of this alias list.
     *
     * @return Copy of this alias list.
     */
    public AliasList copy() {
        return new AliasList(this);
    }
}
