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
package com.github.jonathanxd.wcommands.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 13/03/16.
 */
public class StringUtil {

    public static List<String> toList(String[] arguments) {

        List<String> list = new ArrayList<>();

        String currentString = null;

        for (String arg : arguments) {
            if (arg.startsWith("\"")) {
                if (currentString == null) {
                    currentString = arg.substring(1);
                } else {
                    if (QuoteUtil.allOpenAllClose(currentString)) {

                        currentString += " " + arg;
                        list.add(currentString.substring(0, currentString.length()-1));
                        currentString = null;
                    }
                }

            } else if (arg.endsWith("\"")) {
                if (currentString != null) {
                    if (QuoteUtil.allOpenAllClose(currentString)) {
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

}
