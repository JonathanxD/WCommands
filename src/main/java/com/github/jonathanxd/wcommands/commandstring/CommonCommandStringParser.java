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
package com.github.jonathanxd.wcommands.commandstring;

import com.github.jonathanxd.wcommands.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by jonathan on 19/03/16.
 */
public class CommonCommandStringParser implements CommandStringParser {
    @Override
    public List<String> parse(String[] commandString) {

        StringJoiner sj = new StringJoiner(" ");

        Arrays.stream(commandString).forEach(sj::add);

        return StringUtil.argToList(sj.toString(), new char[]{'[', ']', '"', '"'});
    }
}