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
import com.github.jonathanxd.wcommands.util.reflection.ToString;

import java.util.Optional;

/**
 * Created by jonathan on 18/03/16.
 */
public class Result<T> implements IResult<T> {
    private final Handler<?> source;
    private final CommandData<?> commandData;
    private final T resultValue;
    private final Enum<?> id;

    Result(Handler<?> source, T resultValue, CommandData<?> commandData) {
        this(null, source, commandData, resultValue);
    }

    Result(Enum<?> id, Handler<?> source, CommandData<?> commandData, T resultValue) {
        this.id = id;
        this.source = source;
        this.commandData = commandData;
        this.resultValue = resultValue;
    }

    public Result(Enum<?> id, T resultValue) {
        this.id = id;
        this.source = null;
        this.commandData = null;
        this.resultValue = resultValue;
    }

    @Override
    public Optional<Enum<?>> getBoxedId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Enum<?> getId() {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends Enum<V>> Enum<V> getGenId() {
        return id == null ? null : (Enum<V>) id;
    }

    @Override
    public <V extends Enum<V>> Enum<V> getGenId(Class<V> clazz) {
        return id == null || clazz == null ? null : clazz.cast(id);
    }

    @Override
    public CommandData<?> getCommandData() {
        return commandData;
    }

    @Override
    public Handler<?> getSource() {
        return source;
    }

    @Override
    public T getResultValue() {
        return resultValue;
    }

    @Override
    public String toString() {
        return "Result["+ ToString.toString(this)+"]";
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : commandData.hashCode();
    }

    @SuppressWarnings("unchecked")
    public static IResult<?> fill(IResult<?> iResult, Enum<?> id, Handler<?> source, CommandData<?> commandData, Object resultValue) {

        Handler<?> fSource = iResult.getSource();
        CommandData<?> fCommandData = iResult.getCommandData();
        Object fResultValue = iResult.getResultValue();
        Enum<?> fId = iResult.getId();

        fSource = fSource != null ? fSource : source;
        fCommandData = fCommandData != null ? fCommandData : commandData;
        fResultValue = fResultValue != null ? fResultValue : resultValue;
        fId = fId != null ? fId : id;

        return new Result<>(fId, fSource, fCommandData, fResultValue);
    }
}
