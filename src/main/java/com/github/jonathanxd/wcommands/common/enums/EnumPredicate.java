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
package com.github.jonathanxd.wcommands.common.enums;

import java.util.function.Predicate;

/**
 * Created by jonathan on 28/02/16.
 */
public class EnumPredicate implements Predicate<String> {
    private final Class<? extends Enum> enumClass;

    public EnumPredicate(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public boolean test(String stringMatchable) {

        return get(stringMatchable) != null;
    }

    public Enum get(String stringMatchable) {
        if (stringMatchable == null)
            return null;

        for (Enum constant : enumClass.getEnumConstants()) {
            if (stringMatchable.equalsIgnoreCase(constant.name())) {
                return constant;
            }
        }
        return null;
    }
}
