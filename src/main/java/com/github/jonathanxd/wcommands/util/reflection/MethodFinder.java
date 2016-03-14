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
package com.github.jonathanxd.wcommands.util.reflection;

import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.infos.Information;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by jonathan on 12/03/16.
 */
public class MethodFinder {
    @SuppressWarnings("Duplicates")
    public static <T> List<T> findAssignable(List<List<T>> lists, Class<?>[] classes, int start) {
        int testPosition = classes.length - 1;

        for (int x = lists.size() - 1; x > -1; --x) {
            int pos = testPosition;

            Class<?> testClass = classes[pos];
            List<T> list = lists.get(x);

            for (int i = list.size() - 1; i > -1; --i) {
                T o = list.get(i);
                if (testClass.isAssignableFrom(o.getClass())) {
                    if (pos - 1 < start) {
                        if (i - 1 <= -1 && !list.isEmpty()) {
                            return list;
                        } else {
                            break;
                        }
                    }
                    testClass = classes[--pos];
                } else {
                    break;
                }

                if (i - 1 <= -1 && !list.isEmpty()) {
                    return list;
                }
            }
        }

        return Collections.emptyList();
    }

    @SuppressWarnings("Duplicates")
    public static <T> List<FindData<T>> findAssignable(List<List<T>> lists, Parameter[] parameters, int start, Function<T, Class<?>> classTranslator) {
        Class<?>[] classes = new Class<?>[parameters.length];

        for (int x = 0; x < parameters.length; ++x) {
            classes[x] = parameters[x].getType();
        }

        int testPosition = classes.length - 1;

        List<FindData<T>> dataList = new ArrayList<>();

        for (int x = lists.size() - 1; x > -1; --x) {
            int pos = testPosition;

            Class<?> testClass = classes[pos];
            List<T> list = lists.get(x);

            dataList.clear();

            for (int i = list.size() - 1; i > -1; --i) {
                T object = list.get(i);

                Class<?> oClass = classTranslator.apply(object);

                testClass = updateTestClass(classes[pos], parameters[pos], object, oClass, testClass, dataList);

                if (testClass.isAssignableFrom(oClass)) {
                    if (pos - 1 < start) {
                        if (i - 1 <= -1 && !list.isEmpty()) {
                            return dataList;
                        } else {
                            break;
                        }
                    }
                    --pos;
                    testClass = updateTestClass(classes[pos], parameters[pos], object, oClass, testClass, dataList);
                } else {
                    break;
                }

                if (i - 1 <= -1 && !list.isEmpty()) {
                    return dataList;
                }
            }
        }

        return Collections.emptyList();
    }

    private static <T> Class<?> updateTestClass(Class<?> testClass, Parameter parameter, T element, Class<?> matchType, Class<?> paramType, List<FindData<T>> dataList) {

        AnnotatedType type;

        boolean isParameterizedType = false;

        if ((type = parameter.getAnnotatedType()) != null) {

            if (!(type.getType() instanceof ParameterizedType)) {
                //throw new RuntimeException("Cannot get raw type...");
                // Only use the current type. {@link Info} is expected to handle it properly
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type.getType();

                if (parameterizedType.getActualTypeArguments().length < 1) {
                    throw new RuntimeException("Cannot get raw type for type: '" + parameterizedType + "'");
                }

                isParameterizedType = true;

                testClass = TypeUtil.from(parameterizedType.getActualTypeArguments()[0]);
            }

            dataList.add(new FindData<>(element, parameter.getDeclaredAnnotations(), parameter, matchType, paramType, isParameterizedType));
        } else {
            dataList.add(new FindData<>(element, new Annotation[0], parameter, matchType, paramType, isParameterizedType));
        }

        return testClass;
    }

    public static final class FindData<T> {
        private final T value;
        private final Annotation[] annotations;
        private final Parameter parameter;
        private final Class<?> matchType;
        private final Class<?> parameterType;
        private final boolean isParameterizedType;


        public FindData(T value, Annotation[] annotations, Parameter parameter, Class<?> matchType, Class<?> parameterType, boolean isParameterizedType) {
            this.value = value;
            this.annotations = annotations;
            this.parameter = parameter;
            this.matchType = matchType;
            this.parameterType = parameterType;
            this.isParameterizedType = isParameterizedType;
        }

        public T getValue() {
            return value;
        }

        public Annotation[] getAnnotations() {
            return annotations;
        }

        public Parameter getParameter() {
            return parameter;
        }

        public Class<?> getMatchType() {
            return matchType;
        }

        public Class<?> getParameterType() {
            return parameterType;
        }

        public boolean isParameterizedType() {
            return isParameterizedType;
        }
    }
}
