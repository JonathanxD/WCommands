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
package com.github.jonathanxd.wcommands;

import com.github.jonathanxd.wcommands.arguments.holder.ArgumentHolder;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.handler.Handler;

import java.util.Optional;

/**
 * Created by jonathan on 27/02/16.
 */
public interface CommonHandler extends Handler<CommandHolder> {

    abstract class Value<ID, T> implements Handler<CommandHolder> {

        private final ID id;

        public Value(ID id) {
            this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handle(CommandData<CommandHolder> commandData) {
            Optional<?> opt = commandData.getCommand().getArgValue(id);
            if(!opt.isPresent()) {
                throw new IllegalStateException("Cannot find argument (id: '"+id+"')");
            }
            handle((T) opt.get());
        }

        public abstract void handle(T value);
    }

    abstract class AnyValue<ID> implements Handler<CommandHolder> {

        public AnyValue() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handle(CommandData<CommandHolder> commandData) {

            for(ArgumentHolder holder : commandData.getCommand().getArguments()) {
                handleAny((ID) holder.getArgumentSpec().getId(), holder.convertValue());
            }

        }

        public abstract <T> void handleAny(ID id, T value);
    }
}
