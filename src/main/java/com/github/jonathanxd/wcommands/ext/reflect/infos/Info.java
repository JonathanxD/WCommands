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

import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.util.ListUtils;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;
import com.github.jonathanxd.wcommands.util.reflection.MethodFinder;
import com.github.jonathanxd.wcommands.util.reflection.MethodFinder.FindData;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    String description() default "";


    final class InformationUtil {
        public static Node<Object[], Class<?>[]> getForMethod(Object[] invokeObjects, Class<?>[] invokeTypes, ElementBridge bridge, InformationRegister informationRegister) {

            List<List<Information<?>>> poss = ListUtils.possibilities(informationRegister.stream().collect(Collectors.toList()));

            if (bridge.isMethod()) {
                Method m = (Method) bridge.getMember();

                for (int x = 0; x < m.getParameterTypes().length; ++x) {
                    if (x < invokeTypes.length) {
                        if (invokeTypes[x] != m.getParameterTypes()[x]) {
                            throw new IllegalStateException("Cannot cast type '" + invokeTypes[x] + "' to '" + m.getParameterTypes()[x] + "'");
                        }
                    } else {
                        List<FindData<Information<?>>> ass = MethodFinder.findAssignable(poss, m.getParameters(), x, information -> information.get().getClass());
                        if (ass.isEmpty()) {
                            throw new RuntimeException("Cannot determine parameters: '" + Arrays.toString(Arrays.copyOfRange(m.getParameterTypes(), x, m.getParameterTypes().length)) + "' probably Information is Missing!");
                        } else {
                            List<Object> objectList = new ArrayList<>(Arrays.asList(invokeObjects));
                            List<Class<?>> typeList = new ArrayList<>(Arrays.asList(invokeTypes));

                            for (FindData<Information<?>> findData : ass) {

                                Information<?> information = findData.getValue().clone();

                                for (Annotation annotation : findData.getAnnotations()) {
                                    if (annotation instanceof Info) {
                                        String desc = ((Info) annotation).description();

                                        if(desc != null && !desc.isEmpty())
                                            information.getDescription().provide(desc);
                                    }
                                }

                                if (findData.isParameterizedType()) {
                                    objectList.add(information);
                                    typeList.add(information.getClass());
                                } else {
                                    objectList.add(information.get());
                                    typeList.add(information.get().getClass());
                                }


                            }

                            return new Node<>(objectList.toArray(), typeList.toArray(new Class<?>[typeList.size()]));
                        }
                    }
                }


            }
            return null;
        }

    }
}
