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
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.util.reflection.ElementBridge;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

        try {
            requires = bridge.getDeclaredAnnotationsByType(Require.class);
        } catch (Exception e) {
        }

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
                    test(requirements, requires, commandData, informationRegister, new Object[]{value});
                    //) { throw Exception };

                    boolean force = argumentHolder.get().getArgumentSpec().getData().findData(ReflectionCommandProcessor.PropSet.FINAL.getClass());

                    if (arg.isPresent()) {
                        bridge.setValue(instance.get(), value, true, force);
                    } else if (arg.getArgumentSpec().isOptional()) {
                        if (Boolean.class.isAssignableFrom(bridge.getType()) || Boolean.TYPE.isAssignableFrom(bridge.getType())) {
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
                if (ElementBridge.check(argObjects, (Method) bridge.getMember())) {
                    test(requirements, requires, commandData, informationRegister, Arrays.copyOf(argObjects, argObjects.length));
                }
                theReturn = bridge.invoke(instance.get(), argObjects, true);

            } catch (Exception e) {

                if (informationRegister == null) {
                    throw new RuntimeException(e);
                }

                Object[] os = Info.InformationUtil.findAssignable(informationRegister, ((Method) bridge.getMember()).getParameters(), argObjects.length, argObjects);

                test(requirements, requires, commandData, informationRegister, Arrays.copyOf(os, os.length));

                try {
                    theReturn = bridge.invoke(instance.get(), os, true);
                } catch (Throwable tt) {

                    if (find(tt, NullPointerException.class)) {
                        System.err.println("Make sure the NullPointerException isn't caused by missing information.");
                        testInformations(bridge, informationRegister, os);
                    }

                    e.printStackTrace();
                    throw new RuntimeException(tt.getMessage(), tt);
                }
            }

        } else if (bridge.isClass()) {
            Class<?> clazz = (Class<?>) bridge.getMember();

            test(requirements, requires, commandData, informationRegister, new Object[]{});

            if (Handler.class.isAssignableFrom(clazz)) {
                @SuppressWarnings("unchecked") Handler<CommandHolder> handler = (Handler<CommandHolder>) instance.get();
                theReturn = handler.handle(commandData, requirements, informationRegister);
            } else {
                throw new InvalidCommand("The command '" + commandData.getCommand() + "' for input '" + commandData.getInputArgument() + "' is invalid! Classes has no Executors to run!");
            }
        }


        return theReturn;
    }

    private boolean find(Throwable tt, Class<? extends Throwable> thClass) {
        if(thClass.isInstance(tt)) {
            return true;
        }

        while((tt = tt.getCause()) != null) {
            if(thClass.isInstance(tt)) {
                return true;
            }
        }
        return false;
    }

    private void testInformations(ElementBridge bridge, InformationRegister register, Object[] passedParameters) {
        if (bridge.isMethod()) {
            Method method = (Method) bridge.getMember();

            Parameter[] parameters = method.getParameters();

            for (int x = 0; x < parameters.length; ++x) {
                Parameter parameter = parameters[x];
                Info info = parameter.getDeclaredAnnotation(Info.class);

                if (info != null) {
                    Object o = passedParameters[x];

                    if (o == null) {
                        System.err.println("Information '" + Info.InformationUtil.toString(info) + "' is missing, argument at index '" + x + "' is null!");
                    }
                }
            }

            System.err.println("Provided information = '"+register+"'");
        }
    }

    public void test(Requirements requirements, Require[] requires, CommandData<CommandHolder> commandData, InformationRegister informationRegister, Object[] args) {
        if (requires == null)
            return;

        for (Require require : requires) {

            Information<?> subject = Information.empty();

            Info infoSubject = require.subject();
            if (infoSubject.type() != Object.class || infoSubject.tags().length > 0) {
                Optional<Information<?>> byId = informationRegister.getById(new InfoId(infoSubject.tags(), infoSubject.type()));

                if (byId.isPresent()) {
                    subject = byId.get();
                }

            }

            test(requirements.test(require.type(), require.data(), args, commandData, informationRegister, subject), require);
        }
    }

    public void test(boolean b, Require require) {
        if (!b) {
            throw new UnsatisfiedRequirementException("Requirement: '" + require + "'", require.type(), require.data());
        }
    }

}
