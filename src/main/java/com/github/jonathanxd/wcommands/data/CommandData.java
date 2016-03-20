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
package com.github.jonathanxd.wcommands.data;

/**
 * Created by jonathan on 26/02/16.
 */
public class CommandData<C> {

    private final String inputArgument;
    //private final CommandSpec command;
    //private final Arguments arguments;
    private final C command;
    private final C parent;

    public CommandData(String inputArgument, C command, C parent) {
        this.inputArgument = inputArgument;
        this.command = command;
        this.parent = parent;
    }

    public String getInputArgument() {
        return inputArgument;
    }

    public C getCommand() {
        return command;
    }

    public C getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        return command.hashCode();
    }
}
