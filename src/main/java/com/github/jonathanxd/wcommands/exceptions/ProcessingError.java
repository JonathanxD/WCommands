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
public class ProcessingError extends Throwable {

    private final ErrorType type;

    public ProcessingError(ErrorType type) {
        super();
        this.type = type;
    }

    public ProcessingError(String message, ErrorType type) {
        super(message);
        this.type = type;
    }

    public ProcessingError(String message, Throwable cause, ErrorType type) {
        super(message, cause);
        this.type = type;
    }

    public ProcessingError(Throwable cause, ErrorType type) {
        super(cause);
        this.type = type;
    }

    protected ProcessingError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorType type) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}
