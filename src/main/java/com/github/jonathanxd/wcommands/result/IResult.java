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
package com.github.jonathanxd.wcommands.result;

import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.handler.Handler;

import java.util.Optional;

/**
 * Created by jonathan on 20/03/16.
 */
public interface IResult<T> {

    Enum<?> getId();

    T getResultValue();

    Handler<?> getSource();

    CommandData<?> getCommandData();

    Optional<Enum<?>> getBoxedId();

    <V extends Enum<V>> Enum<V> getGenId();

    <V extends Enum<V>> Enum<V> getGenId(Class<V> clazz);


    @SuppressWarnings("unchecked")
    static IResult<?> create(Handler<?> source, CommandData<?> commandData, Object resultValue, Enum<?> id) {
        return new Result<>(source, commandData, resultValue, id);
    }

}
