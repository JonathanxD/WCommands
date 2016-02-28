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
package com.github.jonathanxd.wcommands.common.alias;

import com.github.jonathanxd.wcommands.text.Text;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jonathan on 24/02/16.
 */
public class AliasList extends ArrayList<Text> {
    public AliasList() {
        super();
    }

    public AliasList(Collection<? extends Text> c) {
        super(c);
    }

    public boolean anyMatches(String str, boolean ignoreCase) {
        for(Text text : this) {
            if(Text.matches(text, str, ignoreCase))
                return true;

        }
        return false;
    }

    public boolean anyMatches(String str) {
        return anyMatches(str, false);
    }

    public AliasList copy() {
        return new AliasList(this);
    }
}
