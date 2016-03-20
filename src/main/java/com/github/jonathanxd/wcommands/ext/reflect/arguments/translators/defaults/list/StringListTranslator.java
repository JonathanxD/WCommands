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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.list;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;

import java.util.Collections;
import java.util.List;

/**
 * Created by jonathan on 20/03/16.
 */
public class StringListTranslator implements Translator<List<String>> {
    @Override
    public boolean isAcceptable(List<String> text) {
        return !text.isEmpty();
    }

    @Override
    public List<String> translate(List<String> text) {
        return Collections.unmodifiableList(text);
    }
}
