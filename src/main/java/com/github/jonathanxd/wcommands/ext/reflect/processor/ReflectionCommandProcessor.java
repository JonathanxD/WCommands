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
package com.github.jonathanxd.wcommands.ext.reflect.processor;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.Register;
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
        addGlobalTranslator(Reference.aEnd(Boolean.class), BooleanTranslator.class);
        addGlobalTranslator(Reference.aEnd(Number.class), NumberTranslator.class);
        addGlobalTranslator(Reference.aEnd(String.class), StringTranslator.class);
        addGlobalTranslator(Reference.aEnd(Enum.class), EnumTranslator.class, Order.SEVENTH);
        addGlobalTranslator(Reference.referenceTo().a(List.class).of(String.class).build(), StringListTranslator.class);
        addGlobalTranslator(Reference.referenceTo().a(List.class).of(Enum.class).build(), EnumListTranslator.class);

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
    public <T> void addGlobalTranslator(Reference<T> type, Class<? extends Translator<?>> translator, Order order) {
        translatorList.add(new TypeTranslator<>(type, translator, order));
    }

    @Override
    public <T> void removeGlobalTranslator(Reference<T> type) {
        translatorList.removeIf(t -> t.getType() == type);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void forEach(BiConsumer<Reference<?>, Class<? extends Translator<?>>> consumer) {
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
