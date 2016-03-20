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
package com.github.jonathanxd.wcommands.defaults.argument;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.common.Matchable;
import com.github.jonathanxd.wcommands.text.Text;

import java.util.function.Supplier;

/**
 * Created by jonathan on 27/02/16.
 */
public class BooleanArgumentSpec<ID> extends ArgumentSpec<ID, Boolean> {

    private static final Supplier<Matchable<String>> BOOLEAN_CHECKER = () -> Text.of("(true|false)", true, true);

    public BooleanArgumentSpec(ID id, boolean optional, boolean infinite) {
        super(id, infinite, BOOLEAN_CHECKER, s -> true, optional, d -> d.isEmpty() ? false : Boolean.valueOf(d.get(0)));
    }
}
