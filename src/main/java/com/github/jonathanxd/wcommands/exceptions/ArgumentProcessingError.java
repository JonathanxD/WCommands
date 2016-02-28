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
package com.github.jonathanxd.wcommands.exceptions;

/**
 * Created by jonathan on 27/02/16.
 */
public class ArgumentProcessingError extends Throwable {

    private final ArgumentError type;

    public ArgumentProcessingError(ArgumentError type) {
        super();
        this.type = type;
    }

    public ArgumentProcessingError(String message, ArgumentError type) {
        super(message);
        this.type = type;
    }

    public ArgumentProcessingError(String message, Throwable cause, ArgumentError type) {
        super(message, cause);
        this.type = type;
    }

    public ArgumentProcessingError(Throwable cause, ArgumentError type) {
        super(cause);
        this.type = type;
    }

    protected ArgumentProcessingError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ArgumentError type) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.type = type;
    }

    public ArgumentError getType() {
        return type;
    }
}
