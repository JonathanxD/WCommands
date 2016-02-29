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
import com.github.jonathanxd.wcommands.interceptor.Order;

import java.util.function.BiConsumer;

/**
 * Created by jonathan on 28/02/16.
 */
public interface TranslatorSupport {

    /**
     * Add a Translator to Translators Map
     *
     * @param type       Type Class
     * @param translator Type Translator to Object
     * @param <T>        Type
     */
    <T> void addGlobalTranslator(Reference<T> type, Class<? extends Translator<?>> translator, Order order);

    default <T> void addGlobalTranslator(Reference<T> type, Class<? extends Translator<?>> translator) {
        addGlobalTranslator(type, translator, Order.SEVENTH);
    }

    /**
     * Remove Type Translator
     *
     * @param type Type Class
     * @param <T>  Type
     */
    <T> void removeGlobalTranslator(Reference<T> type);

    /**
     * Foreach elements
     *
     * @param consumer Consumer
     */
    void forEach(BiConsumer<Reference<?>, Class<? extends Translator<?>>> consumer);
}
