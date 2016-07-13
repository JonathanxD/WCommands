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
package com.github.jonathanxd.wcommands.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 13/03/16.
 */
public class StringUtil {

    @Deprecated
    public static List<String> toList(String[] arguments) {

        List<String> list = new ArrayList<>();

        String currentString = null;

        for (String arg : arguments) {
            if (arg.startsWith("\"")) {
                if (currentString == null) {
                    currentString = arg.substring(1);
                } else {
                    if (TagUtil.allOpenAllClose(currentString)) {

                        currentString += " " + arg;
                        list.add(currentString.substring(0, currentString.length() - 1));
                        currentString = null;
                    }
                }

            } else if (arg.endsWith("\"")) {
                if (currentString != null) {
                    if (TagUtil.allOpenAllClose(currentString)) {
                        list.add(currentString);
                        currentString = null;
                    }
                } else {
                    list.add(arg);
                }
            } else {
                if (currentString != null) {
                    currentString += " " + arg;
                } else {
                    list.add(arg);
                }
            }
        }

        return list;
    }

    public static List<String> argToList(String arguments, char[] tags) {
        TagUtil.TagsData tagsData = TagUtil.process(arguments, tags);
        return TagUtil.getSplited(arguments, tagsData, ' ', true);
    }

}
