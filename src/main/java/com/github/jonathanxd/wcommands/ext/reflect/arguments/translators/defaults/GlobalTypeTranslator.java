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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults;

import com.github.jonathanxd.iutils.caster.Caster;
import com.github.jonathanxd.iutils.data.ExtraData;
import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.IsOptional;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.exception.TranslateException;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.SingleNamedContainer;
import com.github.jonathanxd.wcommands.util.reflection.Primitive;

import java.util.List;
import java.util.Optional;


/**
 * Created by jonathan on 27/02/16.
 */
public class GlobalTypeTranslator implements Translator<Object> {

    private final Reference<?> type;
    private final boolean isPrimitive;
    private final TranslatorSupport support;
    private final SingleNamedContainer container;

    private final boolean isOptional;

    public GlobalTypeTranslator(Reference<?> type, SingleNamedContainer container, TranslatorSupport support, IsOptional isOptional) {
        this.support = support;

        this.container = container;

        Class<?> aType = type.getAClass();

        this.isOptional = isOptional != null && isOptional == IsOptional.TRUE;

        if (this.isOptional) {
            if (type.getAClass() == Optional.class) {
                aType = type.getRelated()[0].getAClass();
            }
        }

        Class<?> boxed = Primitive.asBoxed(aType);

        if (boxed != null) {
            this.isPrimitive = true;
            @SuppressWarnings("unchecked") Reference<?> initChange = Reference.a(aType).of(type.getRelated()).build();
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

        Container<Object> objectContainer = new Container<>(null);

        support.forEach((aType, translator) -> {

            if (objectContainer.isPresent() && objectContainer.get() != null)
                return;

            Class<?> testType = type.getAClass();
            Class<?> original = testType;
            if(Primitive.asBoxed(testType) != null) {
                testType = Primitive.asBoxed(testType);
            }
            if ((aType.getAClass().isAssignableFrom(testType) || aType.compareTo(type) == 0)
                    || (type.getRelated().length > 0
                    && this.isOptional
                    && type.getAClass() == Optional.class
                    && aType.getAClass().isAssignableFrom((testType = type.getRelated()[0].getAClass())))) {

                if(testType == null) {
                    return;
                }

                ExtraData data = new ExtraData();

                data.registerData(type);
                data.registerData(testType);
                data.registerData(this);

                Translator<?> aTranslator = (Translator<?>) data.construct(translator);
                try {
                    if (aTranslator.isAcceptable(text)) {
                        Object casted;
                        Object translated = aTranslator.translate(text);
                        try {
                            casted = original.cast(translated);
                        } catch (ClassCastException e) {
                            try{
                                casted = Caster.cast(translated, original);
                            }catch (Throwable t) {
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
