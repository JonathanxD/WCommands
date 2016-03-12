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
@Target({ElementType.FIELD, ElementType.METHOD})
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
