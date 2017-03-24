/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands;

import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.handler.Handler;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;

import java.util.Optional;

public interface CommonHandler extends Handler<CommandHolder> {

    abstract class Value<ID, T> implements Handler<CommandHolder> {

        private final ID id;

        public Value(ID id) {
            this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object handle(CommandData<CommandHolder> commandData, Requirements requirements, InformationRegister informationRegister) {
            Optional<?> opt = commandData.getCommand().getArgValue(id);
            if (!opt.isPresent()) {
                throw new IllegalStateException("Cannot find argument (id: '" + id + "')");
            }
            return handle((T) opt.get());
        }

        public abstract Object handle(T value);
    }

    abstract class AnyValue<ID> implements Handler<CommandHolder> {

        public AnyValue() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object handle(CommandData<CommandHolder> commandData, Requirements requirements, InformationRegister informationRegister) {
            Object ret = null;
            for (ArgumentHolder holder : commandData.getCommand().getArguments()) {
                ret = handleAny((ID) holder.getArgumentSpec().getId(), holder.convertValue());
            }

            return ret;
        }

        public abstract <T> Object handleAny(ID id, T value);
    }
}
