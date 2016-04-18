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
