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
