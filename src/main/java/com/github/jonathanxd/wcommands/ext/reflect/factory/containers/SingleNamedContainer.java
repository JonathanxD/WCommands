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
package com.github.jonathanxd.wcommands.ext.reflect.factory.containers;

import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.annotation.Annotation;

/**
 * Created by jonathan on 27/02/16.
 */
public class SingleNamedContainer extends NamedContainer {


    public SingleNamedContainer(String name, Annotation value, ElementBridge bridge) {
        super(name, value, bridge);
        try{
            bridge.getParameterizedReference();
        }catch (Throwable t) {
            throw new IllegalArgumentException("Unsupported element! Type: "+bridge.getMember().getClass(), t);
        }
    }

    public Reference<?> getTypes() {
        return getBridge().getParameterizedReference();
    }

}
