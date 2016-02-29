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
package com.github.jonathanxd.wcommands.interceptor;

import java.util.TreeSet;

/**
 * Created by jonathan on 27/02/16.
 */
public class Interceptors extends TreeSet<InvokeInterceptor> {
    public Interceptors() {
        super((o1, o2) -> {
            int compare = o1.priority().compareTo(o2.priority());
            return compare != 0 ? compare : compare + 1;
        });
    }

    public Interceptors getPhase(Phase phase) {
        Interceptors invokeInterceptors = new Interceptors();
        this.stream().filter(p -> p.phase() == phase).forEach(invokeInterceptors::add);
        return invokeInterceptors;
    }
}
