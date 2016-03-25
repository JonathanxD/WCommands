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
package com.github.jonathanxd.wcommands.handler;

import com.github.jonathanxd.iutils.annotations.Immutable;
import com.github.jonathanxd.iutils.arrays.Arrays;
import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.common.command.CommandList;
import com.github.jonathanxd.wcommands.handler.registration.RegistrationHandleResult;

/**
 * Created by jonathan on 21/03/16.
 */
public interface CommandRegistrationHandler {

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
    RegistrationHandleResult handle(@Immutable Arrays<RegistrationHandleResult> registrationHandleResults, @Immutable CommandList targetList, WCommand<?> manager);

}
