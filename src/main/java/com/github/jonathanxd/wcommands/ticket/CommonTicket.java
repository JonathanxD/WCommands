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
package com.github.jonathanxd.wcommands.ticket;

import com.github.jonathanxd.iutils.data.ReferenceData;
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
    private final ReferenceData referenceData = new ReferenceData();
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
    public ReferenceData getReferenceData() {
        return referenceData;
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
