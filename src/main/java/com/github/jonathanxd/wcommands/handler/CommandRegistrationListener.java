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
package com.github.jonathanxd.wcommands.handler;

import com.github.jonathanxd.iutils.annotations.Immutable;
import com.github.jonathanxd.iutils.arrays.Arrays;
import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

/**
 * Created by jonathan on 21/03/16.
 */
public interface CommandRegistrationListener {

    default void onStart(WCommand<?> manager, RegistrationTicket<?> ticket) {
    }

    /**
     * Handle command registration
     *
     * @param registrationHandleResults Results returned by handlers, if it is the first, a new
     *                                  Deque with singleton {@link RegistrationHandleResult} will
     *                                  be passed.
     * @param manager                   A {@link WCommand} instance
     * @return a {@link RegistrationHandleResult} with the specifications or null if your handle
     * command does nothing!
     */
    RegistrationHandleResult handle(@Immutable Arrays<RegistrationHandleResult> registrationHandleResults, @Immutable CommandList targetList, WCommand<?> manager, RegistrationTicket<?> ticket);

    default void onEnd(RegistrationTicket<?> ticket) {

    }

}
