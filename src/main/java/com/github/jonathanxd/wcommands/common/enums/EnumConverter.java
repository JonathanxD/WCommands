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

import com.github.jonathanxd.wcommands.text.Text;

import java.util.function.Function;

/**
 * Created by jonathan on 28/02/16.
 */

/**
 * Convert enums
 *
 * CASE INSENSITIVE
 */
public class EnumConverter<T extends Enum> implements Function<Text, T> {
    private final Class<T> enumClass;

    public EnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T apply(Text text) {
        for (T ts : enumClass.getEnumConstants()) {
            if (ts.name().equalsIgnoreCase(text.getPlainString()))
                return ts;
        }
        return null;
    }
}
