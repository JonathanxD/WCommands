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

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import java.util.Arrays;

/**
 * Created by jonathan on 26/03/16.
 */
public class Register<T> {
    private final WCommand<?> wCommand;
    private final RegistrationTicket<T> ticket;

    public Register(WCommand<?> wCommand, RegistrationTicket<T> ticket) {
        this.wCommand = wCommand;
        this.ticket = ticket;
        ticket.getListeners().forEach(commandRegistrationListener -> commandRegistrationListener.onStart(wCommand, ticket));
    }

    public WCommand<?> getwCommand() {
        return wCommand;
    }

    public RegistrationTicket<T> getTicket() {
        return ticket;
    }

    /**
     * Register command
     *
     * @param commandSpec Command Specification
     */
    public Register<T> registerCommand(CommandSpec... commandSpec) {
        if (commandSpec.length == 1) {
            wCommand.getCommands().add(commandSpec[0], getTicket());
        } else {
            wCommand.getCommands().addAll(Arrays.asList(commandSpec), getTicket());
        }

        return this;
    }

    /**
     * Register sub command
     * @param main Root/Main Command
     *             @param subCommands Sub commands
     */
    public Register<T> registerSubCommand(CommandSpec main, CommandSpec... subCommands) {
        main.addSubs(getTicket(), subCommands);
        return this;
    }

    /**
     * Register all commands from another list
     *
     * @param commands Command list
     */
    public void registerAllFrom(CommandList commands) {
        wCommand.getCommands().addAll(commands, getTicket());
    }
}
