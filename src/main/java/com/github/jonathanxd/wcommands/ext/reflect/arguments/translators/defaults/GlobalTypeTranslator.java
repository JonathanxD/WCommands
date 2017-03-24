/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults;

import com.github.jonathanxd.iutils.container.MutableContainer;
import com.github.jonathanxd.iutils.data.Data;
import com.github.jonathanxd.iutils.data.DataReflect;
import com.github.jonathanxd.iutils.type.TypeInfo;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.IsOptional;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.exception.TranslateException;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.util.reflection.Primitive;

import java.util.List;
import java.util.Optional;


public class GlobalTypeTranslator implements Translator<Object> {

    private final TypeInfo<?> type;
    private final boolean isPrimitive;
    private final TranslatorSupport support;
    private final SingleNamedContainer container;

    private final boolean isOptional;

    public GlobalTypeTranslator(TypeInfo<?> type, SingleNamedContainer container, TranslatorSupport support, IsOptional isOptional) {
        this.support = support;

        this.container = container;

        Class<?> aType = type.getTypeClass();

        this.isOptional = isOptional != null && isOptional == IsOptional.TRUE;

        if (this.isOptional) {
            if (type.getTypeClass() == Optional.class) {
                aType = type.getRelated()[0].getTypeClass();
            }
        }

        Class<?> boxed = Primitive.asBoxed(aType);

        if (boxed != null) {
            this.isPrimitive = true;
            @SuppressWarnings("unchecked") TypeInfo<?> initChange = TypeInfo.builderOf(aType).of(type.getRelated()).build();
            this.type = initChange;
        } else {
            this.isPrimitive = false;
            this.type = type;
        }


    }

    @Override
    public boolean isAcceptable(List<String> text) {

        try {
            return translate(text) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object translate(List<String> text) {

        MutableContainer<Object> objectContainer = new MutableContainer<>(null);

        support.forEach((aType, translator) -> {

            if (objectContainer.isPresent() && objectContainer.get() != null)
                return;

            Class<?> testType = type.getTypeClass();
            Class<?> original = testType;
            if (Primitive.asBoxed(testType) != null) {
                testType = Primitive.asBoxed(testType);
            }
            if (((type.getRelated().length == 0 && aType.getRelated().length == 0 && aType.getTypeClass().isAssignableFrom(testType))
                    || aType.compareTo(type) == 0
                    || aType.isAssignableFrom(type))
                    || (type.getRelated().length > 0
                    && this.isOptional
                    && type.getTypeClass() == Optional.class
                    && aType.getTypeClass().isAssignableFrom((testType = type.getRelated()[0].getTypeClass())))) {

                if (testType == null) {
                    return;
                }

                Data data = new Data();

                data.set("type", type);
                data.set("testType", testType);
                data.set("translator", this);

                Translator<?> aTranslator = (Translator<?>) DataReflect.construct(translator, data);
                try {
                    if (aTranslator.isAcceptable(text)) {
                        Object casted;
                        Object translated = aTranslator.translate(text);
                        try {
                            casted = original.cast(translated);
                        } catch (ClassCastException e) {
                            try {
                                casted = original.cast(translated);
                            } catch (Throwable t) {
                                casted = translated;
                            }

                        }

                        objectContainer.set(casted);
                    }
                } catch (Throwable t) {
                    throw new TranslateException("Cannot translate type '" + type + "'!", t);
                }
            }

        });

        if (isPrimitive && objectContainer.isPresent()) {
            objectContainer.set(Primitive.castToPrimitive(objectContainer.get()));
        }

        return objectContainer.get();
    }
}
