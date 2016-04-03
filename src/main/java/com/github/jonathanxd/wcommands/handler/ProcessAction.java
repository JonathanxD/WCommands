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

/**
 * Created by jonathan on 26/03/16.
 */
public enum ProcessAction {
    /**
     * Continue the process (may cause {@link StackOverflowError} if the exception type is {@link com.github.jonathanxd.wcommands.exceptions.ErrorType.Type#ERROR}
     */
    CONTINUE,
    /**
     * Stop process and throw error!
     */
    STOP,
    /**
     * Cancel process, don't throw any error.
     */
    CANCEL
}
