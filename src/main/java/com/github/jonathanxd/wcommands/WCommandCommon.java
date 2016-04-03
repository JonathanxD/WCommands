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

import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.commandstring.CommandStringParser;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.exceptions.ErrorType;
import com.github.jonathanxd.wcommands.handler.ErrorHandler;
import com.github.jonathanxd.wcommands.handler.ProcessAction;
import com.github.jonathanxd.wcommands.processor.CommonProcessor;
import com.github.jonathanxd.wcommands.processor.Processor;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import java.util.List;

/**
 * Created by jonathan on 27/02/16.
 */
public class WCommandCommon extends WCommand<List<CommandData<CommandHolder>>> {

    public WCommandCommon(Processor<List<CommandData<CommandHolder>>> processor) {
        this(processor, (e, d, l, v, r, k) -> {
            if(e != null)
                e.printStackTrace();
            return (e != null && e.getType().getExceptionType() != ErrorType.Type.ERROR) ? ProcessAction.CONTINUE : ProcessAction.STOP;
        });
    }

    public WCommandCommon() {
        this(new CommonProcessor());
    }

    public WCommandCommon(ErrorHandler<List<CommandData<CommandHolder>>> errorHandler) {
        this(new CommonProcessor(), errorHandler);
    }

    public WCommandCommon(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> errorHandler) {
        super(processor, errorHandler);
    }

    public WCommandCommon(Processor<List<CommandData<CommandHolder>>> processor, ErrorHandler<List<CommandData<CommandHolder>>> errorHandler, CommandStringParser commandStringParser) {
        super(processor, errorHandler, commandStringParser);
    }

    /**
     * Export settings to other {@link WCommandCommon}
     */
    public void exportTo(WCommandCommon wCommandCommon) {
        wCommandCommon.importFrom(this);
    }

    /**
     * Import commands and interceptors from another {@link WCommandCommon}
     *
     * @param commandCommon {@link WCommandCommon} to import
     */
    public void importFrom(WCommandCommon commandCommon) {

        if (commandCommon == this)
            throw new UnsupportedOperationException("Cannot import from same WCommandCommon");

        commandCommon.getCommands().stream().filter(spec -> !this.getCommands().contains(spec)).forEach(spec -> this.getCommands().add(spec, RegistrationTicket.empty(this)));
        commandCommon.getInterceptors().stream().filter(invokeInterceptor -> !this.getInterceptors().contains(invokeInterceptor)).forEach(invokeInterceptor -> this.getInterceptors().add(invokeInterceptor));

    }
}
