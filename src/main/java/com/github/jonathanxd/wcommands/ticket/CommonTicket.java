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
package com.github.jonathanxd.wcommands.ticket;

import com.github.jonathanxd.iutils.data.MapData;
import com.github.jonathanxd.wcommands.handler.CommandRegistrationListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jonathan on 26/03/16.
 */
public final class CommonTicket<T> implements RegistrationTicket<T> {

    private final T ticketProvider;
    private final MapData additionalData = new MapData();
    private final List<CommandRegistrationListener> registrationListeners = new ArrayList<>();
    private boolean isOpen = true;

    public CommonTicket(T ticketProvider) {
        this.ticketProvider = ticketProvider;
    }

    @Override
    public T getTicketProvider() {
        return ticketProvider;
    }

    @Override
    public MapData getAdditionalData() {
        return additionalData;
    }

    @Override
    public void addRegistrationListener(CommandRegistrationListener listener) {
        registrationListeners.add(listener);
    }

    @Override
    public void removeRegistrationListener(CommandRegistrationListener listener) {
        registrationListeners.remove(listener);
    }

    @Override
    public Collection<CommandRegistrationListener> getListeners() {
        return isOpen ? Collections.unmodifiableList(registrationListeners) : Collections.emptyList();
    }

    @Override
    public void endRegistration() {
        isOpen = false;
        registrationListeners.forEach(commandRegistrationListener -> commandRegistrationListener.onEnd(this));
    }

    @Override
    public boolean isOpenRegistration() {
        return isOpen;
    }
}
