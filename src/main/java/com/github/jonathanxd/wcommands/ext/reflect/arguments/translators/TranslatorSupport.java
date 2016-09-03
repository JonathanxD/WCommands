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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators;

import com.github.jonathanxd.iutils.object.TypeInfo;
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
    <T> void addGlobalTranslator(TypeInfo<T> type, Class<? extends Translator<?>> translator, Order order);

    default <T> void addGlobalTranslator(TypeInfo<T> type, Class<? extends Translator<?>> translator) {
        addGlobalTranslator(type, translator, Order.SEVENTH);
    }

    /**
     * Remove Type Translator
     *
     * @param type Type Class
     * @param <T>  Type
     */
    <T> void removeGlobalTranslator(TypeInfo<T> type);

    /**
     * Foreach elements
     *
     * @param consumer Consumer
     */
    void forEach(BiConsumer<TypeInfo<?>, Class<? extends Translator<?>>> consumer);
}
