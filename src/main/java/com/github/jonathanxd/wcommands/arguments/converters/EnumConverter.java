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
package com.github.jonathanxd.wcommands.arguments.converters;

import com.github.jonathanxd.wcommands.common.enums.EnumPredicate;

import java.util.function.Function;

/**
 * Created by jonathan on 12/03/16.
 */
public class EnumConverter implements Function<String, Object> {
    private final Class<? extends Enum> enumClass;
    private final EnumPredicate predicate;

    public EnumConverter(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
        this.predicate = new EnumPredicate(enumClass);
    }

    public boolean isAcceptable(String text) {
        return predicate.test(text);
    }

    public Object translate(String text) {

        Enum e = predicate.get(text);

        if(e == null && text != null)
            throw new IllegalStateException("Cannot get enum ("+enumClass.getCanonicalName()+") constant '"+text+"'");

        return e;
    }

    @Override
    public Object apply(String s) {
        return translate(s);
    }
}
