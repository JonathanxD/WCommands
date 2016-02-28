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
import com.github.jonathanxd.iutils.extra.primitivecontainers.BooleanContainer;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.exception.TranslateException;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.reflection.Primitive;


/**
 * Created by jonathan on 27/02/16.
 */
public class GlobalTypeTranslator implements Translator<Object> {

    private final Class<?> type;
    private final boolean isPrimitive;
    private final TranslatorSupport support;

    public GlobalTypeTranslator(Class<?> type, TranslatorSupport support) {
        this.support = support;

        Class<?> boxed = Primitive.asBoxed(type);

        if (boxed != null) {
            this.isPrimitive = true;
            this.type = boxed;
        } else {
            this.isPrimitive = false;
            this.type = type;
        }
    }

    @Override
    public boolean isAcceptable(Text text) {
        try{
            return translate(text) != null;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Object translate(Text text) {

        Container<Object> objectContainer = new Container<>(null);

        support.forEach((aType, translator) -> {

            if (objectContainer.isPresent() && objectContainer.get() != null)
                return;

            if (aType.isAssignableFrom(type)) {
                Class<?> realType;

                if (isPrimitive) {
                    realType = Primitive.asUnboxed(type);
                    if (realType == null)
                        realType = type;
                } else
                    realType = type;

                ExtraData data = new ExtraData();

                data.registerData(realType);
                data.registerData(this);

                Translator<?> aTranslator = (Translator<?>) data.construct(translator);
                try{
                    if(aTranslator.isAcceptable(text)) {
                        Object casted;
                        Object translated = aTranslator.translate(text);
                        try {
                            casted = realType.cast(translated);
                        } catch (ClassCastException e) {
                            casted = Caster.cast(translated, realType);
                        }

                        objectContainer.set(casted);
                    }
                }catch (Throwable t) {
                    throw new TranslateException("Cannot translate type '"+realType+"'!", t);
                }
            }

        });

        if (isPrimitive && objectContainer.isPresent()) {
            objectContainer.set(Primitive.castToPrimitive(objectContainer.get()));
        }

        return objectContainer.get();
    }
}
