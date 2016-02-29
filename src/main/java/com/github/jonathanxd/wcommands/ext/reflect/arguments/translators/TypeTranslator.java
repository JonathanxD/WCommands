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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.interceptor.Priority;

import java.util.Objects;

/**
 * Created by jonathan on 28/02/16.
 */
public class TypeTranslator<T> {
    private final Reference<T> type;
    private final Class<? extends Translator<?>> translator;
    private final Priority priority;

    public TypeTranslator(Reference<T> type, Class<? extends Translator<?>> translator, Priority priority) {
        this.type = type;
        this.translator = translator;
        this.priority = priority;
    }

    public Reference<T> getType() {
        return type;
    }

    public Class<? extends Translator<?>> getTranslator() {
        return translator;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, translator);
    }
}
