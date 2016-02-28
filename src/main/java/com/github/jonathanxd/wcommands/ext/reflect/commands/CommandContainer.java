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
package com.github.jonathanxd.wcommands.ext.reflect.commands;

import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.ArgumentContainer;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 27/02/16.
 */
public class CommandContainer extends Container<Command> {

    private final ElementBridge bridge;
    private final List<CommandContainer> child = new ArrayList<>();
    private final List<ArgumentContainer> argumentContainers = new ArrayList<>();

    public CommandContainer(Command value, ElementBridge bridge) {
        super(value);
        this.bridge = bridge;
    }

    public String getName() {

        if (!this.get().name().trim().isEmpty()) {
            return this.get().name();
        }

        return this.bridge.getName();
    }

    public ElementBridge getBridge() {
        return bridge;
    }

    public boolean isMethod() {
        return bridge.getMember() instanceof Method;
    }

    public boolean isField() {
        return bridge.getMember() instanceof Field;
    }

    public List<CommandContainer> getChild() {
        return child;
    }

    public List<ArgumentContainer> getArgumentContainers() {
        return argumentContainers;
    }
}
