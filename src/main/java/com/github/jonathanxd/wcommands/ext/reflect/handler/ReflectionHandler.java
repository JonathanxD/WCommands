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
package com.github.jonathanxd.wcommands.ext.reflect.handler;

import com.github.jonathanxd.iutils.arrays.Arrays;
import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.wcommands.CommonHandler;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 27/02/16.
 */
public class ReflectionHandler implements CommonHandler {

    private final InstanceContainer instance;
    private final TreeNamedContainer commandContainer;

    public ReflectionHandler(InstanceContainer instance, TreeNamedContainer commandContainer) {
        this.instance = instance;
        this.commandContainer = commandContainer;
    }

    @Override
    public void handle(CommandData<CommandHolder> commandData, InformationRegister informationRegister) {

        CommandHolder holder = commandData.getCommand();
        ElementBridge bridge = commandContainer.getBridge();

        if (bridge.isField()) {
            Optional<ArgumentHolder<String, Object>> argumentHolder = holder.getArgument(bridge.getName(), true);

            try {
                if (argumentHolder.isPresent()) {

                    ArgumentHolder<String, Object> arg = argumentHolder.get();

                    Object value = null;

                    if (arg.getArgumentSpec().isOptional() && bridge.getType() == Optional.class) {
                        if (!arg.isPresent())
                            value = Optional.empty();
                        else
                            value = Optional.of(arg.convertValue());
                    } else {
                        if (arg.isPresent())
                            value = arg.convertValue();
                    }

                    if (arg.isPresent()) {
                        boolean force = argumentHolder.get().getArgumentSpec().getData().findData(ReflectionCommandProcessor.PropSet.FINAL.getClass());
                        bridge.setValue(instance.get(), value, true, force);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else if (bridge.isMethod()) {

            List<Object> methodArguments = new ArrayList<>();
            List<Class<?>> methodParamTypes = new ArrayList<>();

            Class<?>[] paramTypes = ((Method) bridge.getMember()).getParameterTypes();

            ArgumentsHolder arguments = holder.getArguments();

            for (int x = 0; x < arguments.size(); ++x) {

                ArgumentHolder argument = arguments.get(x);

                Object value = argument.convertValue();

                if (value == null) {
                    if (paramTypes[x] == Optional.class && argument.getArgumentSpec().isOptional()) {
                        methodArguments.add(Optional.empty());
                    } else {
                        methodArguments.add(/*value*/null);
                    }
                    methodParamTypes.add(paramTypes[x]);
                } else {
                    if (paramTypes[x] == Optional.class && argument.getArgumentSpec().isOptional()) {
                        methodArguments.add(Optional.of(value));
                    } else {
                        methodArguments.add(value);
                        methodParamTypes.add(value.getClass());
                    }
                }
            }

            Object[] argObjects = methodArguments.toArray(new Object[methodArguments.size()]);
            Class<?>[] argTypes = methodParamTypes.toArray(new Class<?>[methodParamTypes.size()]);

            try {
                bridge.invoke(instance.get(), argTypes, argObjects, true);
            } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {

                if (informationRegister == null) {
                    throw new RuntimeException(e);
                }

                Node<Object[], Class<?>[]> informationFor = Info.InformationUtil.getForMethod(argObjects, argTypes, bridge, informationRegister);

                if (informationFor == null) {
                    throw new RuntimeException(e);
                }

                try {
                    bridge.invoke(instance.get(), informationFor.getValue(), informationFor.getKey(), true);
                } catch (InvocationTargetException | IllegalAccessException e2) {
                    RuntimeException ex = new RuntimeException(e);
                    ex.setStackTrace(Arrays.ofG(ex.getStackTrace()).addAll(e2.getStackTrace()).toGenericArray());
                    throw ex;
                }
            }

        }

    }

}
