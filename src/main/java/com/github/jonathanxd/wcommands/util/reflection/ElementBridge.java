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

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.interceptor.Order;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

/**
 * Created by jonathan on 27/02/16.
 */
public class ElementBridge implements AnnotatedElement {

    private final Object element;
    private final Class<?> elementClass;
    private final ElementType location;
    private final Order priority;
    private Reference<?> reference;

    public ElementBridge(Object element, ElementType location, Order priority) {
        this.element = element;
        this.elementClass = this.element.getClass();
        this.location = location;
        this.priority = priority;
    }

    public ElementBridge(Object element, ElementType location) {
        this(element, location, (Order) null);
    }

    public ElementBridge(Object element, ElementType location, Reference<?> reference, Order priority) {
        this.element = element;
        this.location = location;
        this.elementClass = this.element.getClass();
        this.reference = reference;
        this.priority = priority;
    }

    public ElementBridge(Object element, ElementType location, Reference<?> reference) {
        this(element, location, reference, null);
    }

    public static boolean check(Object[] args, Method method) {
        if (args.length != method.getParameterCount()) {
            return false;
        }

        Class<?>[] types = method.getParameterTypes();

        for (int x = 0; x < args.length; ++x) {
            Object val = args[x];
            Class<?> type = types[x];

            if (val != null && !type.isAssignableFrom(val.getClass()))
                return false;
        }

        return true;
    }

    public boolean hasAlternativePriority() {
        return priority != null;
    }

    public Order getPriority() {
        return priority;
    }

    public String getName() {
        try {
            if (element instanceof Class) {
                return (String) call("getSimpleName", new Class<?>[]{}, new Object[]{});
            }
            return (String) call("getName", new Class<?>[]{}, new Object[]{});
        } catch (Exception e) {
            return "unknown|" + element.toString();
        }
    }

    public Reference<?> directReference() {
        return reference;
    }

    public Reference<?> getParameterizedReference() {
        return reference != null ? reference : Reference.aEnd(getType());
    }

    public Class<?> getType() {
        try {
            Method method = elementClass.getDeclaredMethod("getType");
            method.setAccessible(true);
            return (Class<?>) method.invoke(element);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            try {
                Method method = elementClass.getMethod("getType");
                method.setAccessible(true);

                Object result = method.invoke(element);

                if (result instanceof ParameterizedType) {
                    ParameterizedType type = (ParameterizedType) result;

                    try {
                        this.reference = TypeUtil.toReference(type);
                    } catch (Exception ignored) {
                    }

                    return TypeUtil.from(type.getRawType());
                }

                return (Class<?>) result;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e2) {
                throw new UnsupportedOperationException(e2);
            }
        }
    }

    public Object getMember() {
        return element;
    }

    public boolean isMethod() {
        return element instanceof Method;
    }

    public boolean isClass() {
        return element instanceof Class;
    }

    public Object invoke(Object instance, Object[] args, boolean forceAccess) throws InvocationTargetException, IllegalAccessException {
        Method method = (Method) getMember();
        if (forceAccess)
            method.setAccessible(true);

        check(args, method);

        return method.invoke(instance, args);
    }

    public boolean isField() {
        return element instanceof Field;
    }

    public void setValue(Object instance, Object newValue, boolean forceAccess, boolean forceSet) throws IllegalAccessException, NoSuchFieldException {
        Field field = (Field) element;
        if (forceAccess)
            field.setAccessible(true);
        if (forceSet) {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        }
        field.set(instance, newValue);
    }

    public AnnotatedElement getAsAnnotated() {
        return (AnnotatedElement) element;
    }

    private Object call(String methodName, Class<?>[] parameterTypes, Object[] parameters) {

        try {
            Method method = elementClass.getDeclaredMethod(methodName, parameterTypes);
            return method.invoke(element, parameters);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            try {
                Method method = elementClass.getMethod(methodName, parameterTypes);
                return method.invoke(element, parameters);
            } catch (Exception e2) {
                throw new RuntimeException(e.getMessage() + "|" + e2.getMessage(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) call("getAnnotation", new Class<?>[]{Class.class}, new Object[]{annotationClass});
    }

    @Override
    public Annotation[] getAnnotations() {
        return (Annotation[]) call("getAnnotations", new Class<?>[]{}, new Object[]{});
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return (Annotation[]) call("getDeclaredAnnotations", new Class<?>[]{}, new Object[]{});
    }

    public ElementType getLocation() {
        return location;
    }
}
