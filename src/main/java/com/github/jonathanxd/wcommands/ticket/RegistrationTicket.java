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
public interface RegistrationTicket<T> {

    T getTicketProvider();
    ReferenceData getReferenceData();

    void addRegistrationListener(CommandRegistrationListener listener);
    void removeRegistrationListener(CommandRegistrationListener listener);
    Collection<CommandRegistrationListener> getListeners();

    void endRegistration();

    boolean isOpenRegistration();

    static <T> RegistrationTicket<T> empty(T ticketProvider) {
        return new Empty<>(ticketProvider);
    }

    final class Empty<T> implements RegistrationTicket<T> {

        private final T ticketProvider;
        private boolean isOpen = true;

        public Empty(T ticketProvider) {
            this.ticketProvider = ticketProvider;
        }

        @Override
        public T getTicketProvider() {
            return ticketProvider;
        }

        @Override
        public ReferenceData getReferenceData() {
            return new ReferenceData();
        }

        @Override
        public void addRegistrationListener(CommandRegistrationListener listener) {}

        @Override
        public void removeRegistrationListener(CommandRegistrationListener listener) {}

        @Override
        public Collection<CommandRegistrationListener> getListeners() {
            return Collections.emptyList();
        }

        @Override
        public void endRegistration() {
            isOpen = false;
        }

        @Override
        public boolean isOpenRegistration() {
            return isOpen;
        }
    }

}
