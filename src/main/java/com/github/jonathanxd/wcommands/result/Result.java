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
package com.github.jonathanxd.wcommands.result;

import com.github.jonathanxd.wcommands.handler.Handler;

/**
 * Created by jonathan on 18/03/16.
 */
public class Result {
    private final Handler<?> source;
    private final Object resultValue;

    public Result(Handler<?> source, Object resultValue) {
        this.source = source;
        this.resultValue = resultValue;
    }

    public Handler<?> getSource() {
        return source;
    }

    public Object getResultValue() {
        return resultValue;
    }

    @Override
    public String toString() {
        return "Result[source="+source+", resultValue="+resultValue+"]";
    }
}
