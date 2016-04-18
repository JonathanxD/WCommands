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
package com.github.jonathanxd.wcommands.ext.reflect.commands;

import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.ext.reflect.handler.ReflectionHandler;
import com.github.jonathanxd.wcommands.handler.Handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jonathan on 27/02/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Repeatable(Commands.class)
public @interface Command {
    /**
     * Name of the command, if you don't define the name it will be determined by Reflection (like
     * Field name and Method name).
     *
     * @return Name of the command, if you don't define the name it will be determined by Reflection
     * (like Field name and Method name).
     */
    String name() default "";

    /**
     * Description of the command, for future features of the API
     *
     * @return Description of the command, for future features of the API
     */
    String desc() default "";

    /**
     * Prefix of the command. You can direct set prefix in name, but that isn't recommended!
     *
     * @return Prefix of the command. You can direct set prefix in name, but that isn't recommended!
     */
    String prefix() default "";

    /**
     * Suffix of the command. You can direct set suffix in name, but that isn't recommended!
     *
     * @return Suffix of the command. You can direct set suffix in name, but that isn't recommended!
     */
    String suffix() default "";

    /**
     * True to set as optional command, only works with sub-commands
     *
     * @return True to set as optional command, only works with sub-commands
     */
    boolean isOptional() default false;


    /**
     * Handler implementation, default is {@link ReflectionHandler}
     *
     * @return Handler implementation, default is {@link ReflectionHandler}
     */
    Class<? extends Handler<CommandHolder>> handler() default ReflectionHandler.class;

}
