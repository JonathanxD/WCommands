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
package com.github.jonathanxd.wcommands.ext.reflect.infos;

import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.util.reflection.TypeUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jonathan on 12/03/16.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Description for 'InformationRegister' in parameters.
 * You can use @Info annotation for Information if wan't to set description
 *
 * @see com.github.jonathanxd.wcommands.infos.Description
 */
public @interface Info {

    String[] tags() default "";

    Class<?> type() default Object.class;

    /**
     * Default: True
     *
     * True to search for statically provided information before ProvidedInformation.
     *
     * False to search for ProvidedInformation before Statically Information
     */
    boolean staticFirst() default true;

    String description() default "";


    final class InformationUtil {

        public static Object[] findAssignable(InformationRegister register, Parameter[] parameters, int start, Object[] passedParameters) {
            Set<Information<?>> informationSet = register.getInformationList();
            List<Object> passParameters = new ArrayList<>(Arrays.asList(passedParameters));

            Class<?>[] classes = new Class<?>[parameters.length];

            for (int x = 0; x < parameters.length; ++x) {
                classes[x] = parameters[x].getType();
            }

            for (int x = start; x < parameters.length; ++x) {
                Parameter parameter = parameters[x];
                Info infoAnn;
                if ((infoAnn = parameter.getAnnotation(Info.class)) != null) {

                    if (parameter.getType() == Information.class) {
                        Class<?> raw = getRaw(parameter);

                        Objects.requireNonNull(raw, "Cannot get Raw Type!");


                        Optional<Information<?>> info;
                        if(infoAnn.staticFirst()) {
                             info = informationSet.stream().filter(i -> check(raw, infoAnn, i))//i -> i.isPresent() && raw.isAssignableFrom(i.get().getClass())
                                    .findFirst();
                            if(!info.isPresent()) {
                                info = register.getProvided(from(infoAnn), raw);
                            }
                        } else {
                            info = register.getProvided(from(infoAnn), raw);
                            if(!info.isPresent()) {
                                info = informationSet.stream().filter(i -> check(raw, infoAnn, i))//i -> i.isPresent() && raw.isAssignableFrom(i.get().getClass())
                                        .findFirst();
                            }
                        }


                        if (info.isPresent()) {
                            informationSet.remove(info.get());
                            Information<?> clonedInfo = info.get().clone();
                            clonedInfo.getDescription().provide(infoAnn.description());

                            passParameters.add(clonedInfo);

                        } else {
                            passParameters.add(Information.empty());
                        }

                    } else {
                        defaultHandle(passParameters, register, parameter, infoAnn);
                    }

                } else {
                    defaultHandle(passParameters, register, parameter, infoAnn);
                }
            }
            return passParameters.toArray();

        }

        private static void defaultHandle(List<Object> passParameters, InformationRegister informationRegister, Parameter parameter, Info annotation) {

            Optional<Information<?>> info;

            if(annotation.staticFirst()) {
                info = informationRegister.getInformationList().stream().filter(i -> check(parameter.getType(), annotation, i))
                        .findFirst();

                if(!info.isPresent()) {
                    info = informationRegister.getProvided(from(annotation), parameter.getType());
                }
            } else {
                info = informationRegister.getProvided(from(annotation), parameter.getType());

                if(!info.isPresent()) {
                    info = informationRegister.getInformationList().stream().filter(i -> check(parameter.getType(), annotation, i))
                            .findFirst();
                }
            }


            if (info.isPresent()) {
                passParameters.add(info.get().get());
            } else {
                passParameters.add(null);
            }
        }

        private static InfoId from(Info annotation) {
            return new InfoId(annotation.tags(), annotation.type());
        }

        private static boolean check(Class<?> type, Info annotation, Information<?> info) {
            if (!info.isPresent())
                return false;


            if (type.isAssignableFrom(info.get().getClass())) {

                if (annotation == null)
                    return true;

                if (isEmpty(annotation.tags()) || info.getId().matchesAny(annotation.tags())) {
                    if (annotation.type() == null || annotation.type().isAssignableFrom(info.getId().getIdentification())) {
                        return true;
                    }
                }
            }

            return false;
        }

        private static boolean isEmpty(String[] o) {
            return o == null || o.length == 0 || o.length == 1 && o[0].isEmpty();
        }


        private static Class<?> getRaw(Parameter parameter) {

            AnnotatedType type;

            if ((type = parameter.getAnnotatedType()) != null) {

                if (!(type.getType() instanceof ParameterizedType)) {
                    //throw new RuntimeException("Cannot get raw type...");
                    // Only use the current type. {@link Info} is expected to handle it properly
                } else {
                    ParameterizedType parameterizedType = (ParameterizedType) type.getType();

                    if (parameterizedType.getActualTypeArguments().length < 1) {
                        throw new RuntimeException("Cannot get raw type for type: '" + parameterizedType + "'");
                    }

                    return TypeUtil.from(parameterizedType.getActualTypeArguments()[0]);
                }

            }

            return null;
        }

        public static String toString(Info info) {

            StringBuilder sb = new StringBuilder();

            sb.append("tags=(").append(Arrays.toString(info.tags())).append(')');
            sb.append(',').append(' ');
            sb.append("type=(").append(info.type().getSimpleName()).append(')');
            sb.append(',').append(' ');
            sb.append("description=(").append(info.description()).append(')');

            return sb.toString();
        }

    }
}
