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
package com.github.jonathanxd.wcommands.text;

import com.github.jonathanxd.wcommands.common.Matchable;

/**
 * Created by jonathan on 24/02/16.
 */
public class Text implements Matchable<String>, Comparable<Text> {

    private final String plainString;
    private final boolean isRegex;
    private final boolean ignoreCase;

    public Text(String plainString, boolean isRegex, boolean ignoreCase) {
        this.plainString = plainString;
        this.isRegex = isRegex;
        this.ignoreCase = ignoreCase;
    }

    public static boolean matches(Text text, String other, boolean ignoreCase) {

        if (other == null || text.getPlainString() == null)
            return false;

        String plain = text.plainString;

        if (ignoreCase) {
            if (!text.isRegex()) {
                plain = plain.toLowerCase();
                other = other.toLowerCase();
            } else {
                if (!plain.contains("(?i)")) {
                    plain = "(?i)".concat(plain);
                }
            }
        }

        if (!text.isRegex()) {
            return plain.equals(other);
        } else {
            return other.matches(plain);
        }

    }

    public static Text of(String text, boolean isRegex, boolean ignoreCase) {
        return new Text(text, isRegex, ignoreCase);
    }

    public static Text of(String text) {
        return new Text(text, false, false);
    }

    public static Text ofIgnoreCase(String text) {
        return new Text(text, false, true);
    }

    public static Text ofRegex(String text) {
        return new Text(text, true, false);
    }

    public static Text ofRegexIgnoreCase(String text) {
        return new Text(text, true, true);
    }

    public String getPlainString() {
        return plainString;
    }

    public boolean isRegex() {
        return isRegex;
    }

    public boolean ignoreCase() {
        return ignoreCase;
    }

    @Override
    public boolean matches(String other) {
        return matches(this, other, this.ignoreCase());
    }

    @Override
    public boolean matchesIgnoreCase(String other) {
        return matches(this, other, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return matches((String) obj);
        }
        if (obj instanceof Text) {
            return compareTo((Text) obj) == 0;
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(Text o) {

        if (this.isRegex() && o.isRegex())
            throw new RuntimeException("Cannot compare Two Regex Texts");

        if (this.ignoreCase() && o.ignoreCase()) {
            return matches(this, o.plainString, true) ? 0 : -1;
        } else {
            return matches(this, o.plainString, false) ? 0 : -1;
        }
    }

    @Override
    public String toString() {
        return this.getPlainString();
    }
}
