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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.regex.Pattern;

/**
 * Created by jonathan on 27/02/16.
 */
public class BooleanTranslator implements Translator<Boolean> {
    private static final Pattern BOOLEAN_REGEX = Pattern.compile("true|false|yes|no");

    @Override
    public boolean isAcceptable(Text text) {
        return BOOLEAN_REGEX.matcher(text.getPlainString()).matches();
    }

    @Override
    public Boolean translate(Text text) {

        String plain = text.getPlainString();

        if(!isAcceptable(text))
            throw new IllegalArgumentException("Cannot translate '"+text+"' to Boolean");

        return plain.equalsIgnoreCase("true") || plain.equalsIgnoreCase("yes");

    }
}
