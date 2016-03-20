package com.github.jonathanxd.wcommands.util.reflection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * Created by jonathan on 19/03/16.
 */
public class ToString {

    public static String toString(Object object) {

        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");

        Class<?> clazz = object.getClass();

        for(Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                Object o = f.get(object);

                if(o instanceof Collection) {
                    stringJoiner.add(f.getName() + " = {" + f.get(object) + "}");
                }else {
                    stringJoiner.add(f.getName() + " = '" + f.get(object) + "'");
                }
            } catch (IllegalAccessException ignored) {}
        }

        return stringJoiner.toString();
    }

}
