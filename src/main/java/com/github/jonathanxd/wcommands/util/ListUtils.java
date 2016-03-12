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

import java.util.List;

/**
 * Created by jonathan on 11/03/16.
 */
public class ListUtils {

    public static boolean equals(List<?> list, List<?> list2) {
        if (list.size() != list2.size())
            return false;

        if(list.isEmpty() && list2.isEmpty())
            return true;

        for (int x = 0; x < list.size(); ++x) {
            Object element = list.get(x);
            Object element2 = list2.get(x);

            if (element == null && element2 != null
                    || element != null && element2 == null
                    || (element != null && !element.equals(element2))) {
                return false;
            }
        }

        return true;
    }

}
