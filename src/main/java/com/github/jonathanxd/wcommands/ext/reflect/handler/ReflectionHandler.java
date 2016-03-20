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

import com.github.jonathanxd.wcommands.CommonHandler;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.arguments.holder.ArgumentsHolder;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.reflect.handler.exception.UnsatisfiedRequirementException;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.ext.reflect.infos.require.Require;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.TreeNamedContainer;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Object handle(CommandData<CommandHolder> commandData, Requirements requirements, InformationRegister informationRegister) {

        Object theReturn = null;

        CommandHolder holder = commandData.getCommand();
        ElementBridge bridge = commandContainer.getBridge();

        Require[] requires = null;

        try{
            requires = bridge.getDeclaredAnnotationsByType(Require.class);
        } catch (Exception e){}

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

                    //if(!
                    test(requirements, requires, commandData, informationRegister, new Object[] {value});
                    //) { throw Exception };

                    boolean force = argumentHolder.get().getArgumentSpec().getData().findData(ReflectionCommandProcessor.PropSet.FINAL.getClass());

                    if (arg.isPresent()) {
                        bridge.setValue(instance.get(), value, true, force);
                    }else if(arg.getArgumentSpec().isOptional()) {
                        if(Boolean.class.isAssignableFrom(bridge.getType()) || Boolean.TYPE.isAssignableFrom(bridge.getType())) {
                            bridge.setValue(instance.get(), true, true, force);
                        }
                    }

                    theReturn = value;
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else if (bridge.isMethod()) {

            List<Object> methodArguments = new ArrayList<>();

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
                } else {
                    if (paramTypes[x] == Optional.class && argument.getArgumentSpec().isOptional()) {
                        methodArguments.add(Optional.of(value));
                    } else {
                        methodArguments.add(value);
                    }
                }
            }

            Object[] argObjects = methodArguments.toArray(new Object[methodArguments.size()]);

            try {
                if(ElementBridge.check(argObjects, (Method) bridge.getMember())) {
                    test(requirements, requires, commandData, informationRegister, Arrays.copyOf(argObjects, argObjects.length));
                }
                theReturn = bridge.invoke(instance.get(), argObjects, true);
            } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {

                if (informationRegister == null) {
                    throw new RuntimeException(e);
                }

                Object[] os = Info.InformationUtil.findAssignable(informationRegister, ((Method) bridge.getMember()).getParameters(), argObjects.length, argObjects);

                test(requirements, requires, commandData, informationRegister, Arrays.copyOf(os, os.length));

                try {
                    theReturn = bridge.invoke(instance.get(), os, true);
                } catch (Throwable tt) {
                    throw new RuntimeException(tt.getMessage(), e);
                }
            }

        } else if (bridge.isClass()) {
            Class<?> clazz = (Class<?>) bridge.getMember();

            test(requirements, requires, commandData, informationRegister, new Object[]{});

            if(Handler.class.isAssignableFrom(clazz)) {
                @SuppressWarnings("unchecked") Handler<CommandHolder> handler = (Handler<CommandHolder>) instance.get();
                theReturn = handler.handle(commandData, requirements, informationRegister);
            }else {
                throw new InvalidCommand("The command '" + commandData.getCommand() + "' for input '" + commandData.getInputArgument() + "' is invalid! Classes has no Executors to run!");
            }
        }


        return theReturn;
    }

    public void test(Requirements requirements, Require[] requires, CommandData<CommandHolder> commandData, InformationRegister informationRegister, Object[] args) {
        if(requires == null)
            return;

        for(Require require : requires) {
            test(requirements.test(require.type(), require.data(), args, commandData, informationRegister), require);
        }
    }

    public void test(boolean b, Require require) {
        if(!b) {
            throw new UnsatisfiedRequirementException("Requirement: '"+ require +"'", require.type(), require.data());
        }
    }

}
