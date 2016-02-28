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
package com.github.jonathanxd.wcommands.ext.reflect.arguments;

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

/**
 * Created by jonathan on 27/02/16.
 */
public class ArgumentContainer extends Container<Argument> {

    private final ElementBridge bridge;

    public ArgumentContainer(Argument value, ElementBridge bridge) {
        super(value);
        this.bridge = bridge;
        try{
            bridge.getType();
        }catch (Throwable t) {
            throw new IllegalArgumentException("Unsupported element!", t);
        }
    }

    public String getName() {

        if(!this.get().id().trim().isEmpty())
            return this.get().id();

        return this.bridge.getName();
    }

    public Class<?> getType() {
        return this.bridge.getType();
    }

}
