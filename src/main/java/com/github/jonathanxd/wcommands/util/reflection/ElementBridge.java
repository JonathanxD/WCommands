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

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by jonathan on 27/02/16.
 */
public class ElementBridge implements AnnotatedElement {

    private final Object element;
    private final Class<?> elementClass;
    private Reference<?> reference;

    public ElementBridge(Object element) {
        this(element, null);
    }

    public ElementBridge(Object element, Reference<?> reference) {
        this.element = element;
        this.elementClass = this.element.getClass();
        this.reference = reference;
    }

    public String getName() {
        try {
            return (String) call("getName", new Class<?>[]{}, new Object[]{});
        } catch (Exception e) {
            return "param|" + element.toString();
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

                if (result instanceof ParameterizedTypeImpl) {
                    ParameterizedTypeImpl type = (ParameterizedTypeImpl) result;

                    try {
                        this.reference = TypeUtil.toReference(type);
                    } catch (Exception ignored) {
                    }

                    return type.getRawType();
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

    public void invoke(Object instance, Class<?>[] argTypes, Object[] args, boolean forceAccess) throws InvocationTargetException, IllegalAccessException {
        Method method = (Method) element;
        if (forceAccess)
            method.setAccessible(true);

        method.invoke(instance, args);
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
            throw new RuntimeException(e);
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
}
