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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators;

import java.util.TreeSet;

/**
 * Created by jonathan on 28/02/16.
 */
public class TranslatorList extends TreeSet<TypeTranslator> {

    public TranslatorList() {
        super((o1, o2) -> {
            if (o1.hashCode() == o2.hashCode())
                return 0;

            int compare = o1.getOrder().compareTo(o2.getOrder());
            return compare == 0 ? compare + 1 : compare;
        });
    }

}
