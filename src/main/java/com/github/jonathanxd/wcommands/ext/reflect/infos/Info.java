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
package com.github.jonathanxd.wcommands.ext.reflect.infos;

import com.github.jonathanxd.iutils.object.Reference;
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
                        Reference<?> raw = getRaw(parameter);

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

            Reference<?> reference = TypeUtil.toReference(parameter.getType());

            if(annotation.staticFirst()) {
                info = informationRegister.getInformationList().stream().filter(i -> check(reference, annotation, i))
                        .findFirst();

                if(!info.isPresent()) {
                    info = informationRegister.getProvided(from(annotation), reference);
                }
            } else {
                info = informationRegister.getProvided(from(annotation), reference);

                if(!info.isPresent()) {
                    info = informationRegister.getInformationList().stream().filter(i -> check(reference, annotation, i))
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

        private static boolean check(Reference<?> type, Info annotation, Information<?> info) {
            if (!info.isPresent())
                return false;


            if (type.compareToAssignable(info.getReference()) == 0) {

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


        private static Reference<?> getRaw(Parameter parameter) {

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

                    return TypeUtil.toReference(parameterizedType).getRelated()[0];
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
