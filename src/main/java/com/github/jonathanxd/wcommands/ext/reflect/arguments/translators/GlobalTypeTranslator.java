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

import com.github.jonathanxd.iutils.caster.Caster;
import com.github.jonathanxd.iutils.extra.Container;
import com.github.jonathanxd.iutils.extra.primitivecontainers.BooleanContainer;
import com.github.jonathanxd.iutils.map.Map;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Translator;
import com.github.jonathanxd.wcommands.text.Text;
import com.github.jonathanxd.wcommands.util.reflection.Primitive;


/**
 * Created by jonathan on 27/02/16.
 */
public class GlobalTypeTranslator implements Translator<Object> {

    private static final Map<Class<?>, Translator<?>> translatorMap = new Map<>();

    static {
        translatorMap.put(Boolean.class, new BooleanTranslator());
        translatorMap.put(Number.class, new NumberTranslator());
        translatorMap.put(String.class, new StringTranslator());
    }

    private final Class<?> type;
    private final boolean isPrimitive;

    public GlobalTypeTranslator(Class<?> type) {

        Class<?> boxed = Primitive.asBoxed(type);

        if (boxed != null) {
            this.isPrimitive = true;
            this.type = boxed;
        } else {
            this.isPrimitive = false;
            this.type = type;
        }
    }

    public static <T> void addTranslator(Class<T> type, Translator<T> translator) {
        if (!translatorMap.containsKey(type))
            translatorMap.put(type, translator);
    }

    public static <T> void removeTranslator(Class<T> type) {
        translatorMap.remove(type);
    }

    @Override
    public boolean isAcceptable(Text text) {
        BooleanContainer container = new BooleanContainer(false);

        translatorMap.forEach((aType, translator) -> {
            if (container.isPresent()) {
                return;
            }
            if (aType.isAssignableFrom(type)) {
                container.set(true);
            }
        });

        if (!container.toBoolean()) {
            throw new IllegalArgumentException("Cannot translate type '" + type + "' with " + this.getClass().getSimpleName());
        }

        return container.toBoolean();
    }

    @Override
    public Object translate(Text text) {

        Container<Object> objectContainer = new Container<>(null);

        translatorMap.forEach((aType, translator) -> {

            if (objectContainer.isPresent())
                return;

            if (aType.isAssignableFrom(type)) {

                Class<?> realType;

                if (isPrimitive) {
                    realType = Primitive.asUnboxed(type);
                    if (realType == null)
                        realType = type;
                } else
                    realType = type;

                Object casted;
                Object translated = translator.translate(text);
                try {
                    casted = realType.cast(translated);
                } catch (ClassCastException e) {
                    casted = Caster.cast(translated, realType);
                }

                objectContainer.set(casted);
            }

        });

        if (isPrimitive && objectContainer.isPresent()) {
            objectContainer.set(Primitive.castToPrimitive(objectContainer.get()));
        }

        return objectContainer.get();
    }
}
