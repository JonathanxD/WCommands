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
package com.github.jonathanxd.wcommands.ext.reflect.visitors.containers;

import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 27/02/16.
 */
public class TreeNamedContainer extends NamedContainer {

    private final List<TreeNamedContainer> child = new ArrayList<>();
    private final List<SingleNamedContainer> argumentContainers = new ArrayList<>();

    public TreeNamedContainer(String name, Annotation value, ElementBridge bridge) {
        super(name, value, bridge);
    }

    public boolean isMethod() {
        return getBridge().getMember() instanceof Method;
    }

    public boolean isField() {
        return getBridge().getMember() instanceof Field;
    }

    public List<TreeNamedContainer> getChild() {
        return child;
    }

    public List<SingleNamedContainer> getArgumentContainers() {
        return argumentContainers;
    }

    public TreeNamedContainer recreate(String name) {
        return recreate(name, this.get(), this.getBridge());
    }

    public TreeNamedContainer recreate(Annotation value) {
        return recreate(this.getName(), value, this.getBridge());
    }

    public TreeNamedContainer recreate(String name, Annotation value) {
        return recreate(name, value, this.getBridge());
    }

    public TreeNamedContainer recreate(String name, Annotation value, ElementBridge bridge) {
        TreeNamedContainer treeNamedContainer = new TreeNamedContainer(name, value, bridge);
        treeNamedContainer.child.addAll(this.child);
        treeNamedContainer.argumentContainers.addAll(this.argumentContainers);

        return treeNamedContainer;
    }
}
