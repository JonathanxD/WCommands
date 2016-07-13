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
package com.github.jonathanxd.wcommands.ext.reflect.processor;

import com.github.jonathanxd.iutils.object.GenericRepresentation;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.commandstring.CommandStringParser;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.enums.EnumTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorList;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TranslatorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.TypeTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.BooleanTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.NumberTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.StringTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.list.EnumListTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.list.StringListTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.commands.sub.SubCommand;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitorSupport;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.AnnotationVisitors;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.containers.NamedContainer;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults.ArgumentVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults.CommandVisitor;
import com.github.jonathanxd.wcommands.ext.reflect.visitors.defaults.SubCommandVisitor;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.interceptor.Order;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;

/**
 * Created by jonathan on 27/02/16.
 */

public class ReflectionCommandProcessor extends WCommandCommon implements TranslatorSupport, AnnotationVisitorSupport {

    private final TranslatorList translatorList = new TranslatorList();
    private final AnnotationVisitors annotationVisitors = new AnnotationVisitors();
    private Queue<Runnable> waitingQueue = new LinkedList<>();

    public ReflectionCommandProcessor() {
        super();
        initBasics();
    }

    public ReflectionCommandProcessor(ErrorHandler<List<CommandData<CommandHolder>>> errorHandler) {
        super(errorHandler);
        initBasics();
    }

    public ReflectionCommandProcessor(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> handler) {
        super(processor, handler);
        initBasics();
    }

    public ReflectionCommandProcessor(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> errorHandler, CommandStringParser commandStringParser) {
        super(processor, errorHandler, commandStringParser);
        initBasics();
    }


    private void initBasics() {
        addGlobalTranslator(GenericRepresentation.aEnd(Boolean.class), BooleanTranslator.class);
        addGlobalTranslator(GenericRepresentation.aEnd(Number.class), NumberTranslator.class);
        addGlobalTranslator(GenericRepresentation.aEnd(String.class), StringTranslator.class);
        addGlobalTranslator(GenericRepresentation.aEnd(Enum.class), EnumTranslator.class, Order.SEVENTH);
        addGlobalTranslator(GenericRepresentation.representationOf().a(List.class).of(String.class).build(), StringListTranslator.class);
        addGlobalTranslator(GenericRepresentation.representationOf().a(List.class).of(Enum.class).build(), EnumListTranslator.class);

        registerVisitor(new CommandVisitor(Command.class));
        registerVisitor(new ArgumentVisitor(Argument.class));
        registerVisitor(new SubCommandVisitor(SubCommand.class));

    }

    @Override
    public <T> ReflectionRegister<T> getRegister(RegistrationTicket<T> registrationTicket) {
        return new ReflectionRegister<>(this, registrationTicket);
    }

    @Override
    public List<CommandData<CommandHolder>> process(List<String> arguments, Requirements requirements, InformationRegister informationRegister) {

        if (!this.waitingQueue.isEmpty()) {
            Runnable runnable;

            while ((runnable = waitingQueue.poll()) != null) {
                runnable.run();
            }
        }

        return super.process(arguments, requirements, informationRegister);
    }

    @Override
    public <T> void addGlobalTranslator(GenericRepresentation<T> type, Class<? extends Translator<?>> translator, Order order) {
        translatorList.add(new TypeTranslator<>(type, translator, order));
    }

    @Override
    public <T> void removeGlobalTranslator(GenericRepresentation<T> type) {
        translatorList.removeIf(t -> t.getType() == type);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void forEach(BiConsumer<GenericRepresentation<?>, Class<? extends Translator<?>>> consumer) {
        translatorList.forEach(t -> consumer.accept(t.getType(), t.getTranslator()));
    }


    @Override
    public boolean registerVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor) {
        if (annotationVisitors.contains(annotationVisitor))
            return false;
        annotationVisitors.add(annotationVisitor);
        return true;
    }

    @Override
    public boolean overrideVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor) {
        if (!annotationVisitors.contains(annotationVisitor))
            return false;
        annotationVisitors.removeIf(a -> a.getAnnotationClass() == annotationVisitor.getAnnotationClass());
        annotationVisitors.add(annotationVisitor);
        return true;
    }

    @Override
    public boolean removeVisitor(AnnotationVisitor<?, ?, ?> annotationVisitor) {
        return annotationVisitors.remove(annotationVisitor);
    }

    @Override
    public boolean removeVisitor(Class<?> annotationType) {
        return annotationVisitors.removeIf(a -> a.getAnnotationClass() == annotationType);
    }

    @Override
    public <T extends Annotation, C extends NamedContainer, R> Optional<AnnotationVisitor<T, C, R>> getVisitorFor(Class<? extends Annotation> clazz) {
        return annotationVisitors.<T, C, R>getFor(clazz);
    }

    protected AnnotationVisitors getAnnotationVisitors() {
        return annotationVisitors;
    }

    protected Queue<Runnable> getWaitingQueue() {
        return waitingQueue;
    }

    public enum PropSet {
        FINAL
    }
}
