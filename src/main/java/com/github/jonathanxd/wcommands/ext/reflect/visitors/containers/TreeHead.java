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

import com.github.jonathanxd.iutils.annotations.Immutable;
import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jonathan on 18/03/16.
 */
public class TreeHead {

    List<TreeNamedContainer> heads = new ArrayList<>();

    public void addHead(TreeNamedContainer treeNamedContainer) {
        heads.add(treeNamedContainer);
    }

    public void exitHead(TreeNamedContainer treeNamedContainer) {
        heads.remove(treeNamedContainer);
    }

    public TreeNamedContainer getLast() {
        return !heads.isEmpty() ? heads.get(0) : null;
    }

    @Immutable
    public List<TreeNamedContainer> getHeads() {
        return Collections.unmodifiableList(heads);
    }
}
