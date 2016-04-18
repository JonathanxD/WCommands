/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
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
