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
        return "Result[" + ToString.toString(this) + "]";
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : commandData.hashCode();
    }
}
