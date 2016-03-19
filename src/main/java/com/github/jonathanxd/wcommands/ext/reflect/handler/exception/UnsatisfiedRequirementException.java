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
package com.github.jonathanxd.wcommands.ext.reflect.handler.exception;

/**
 * Created by jonathan on 18/03/16.
 */
public class UnsatisfiedRequirementException extends RuntimeException {

    private final Class<?> type;
    private final String data;

    public UnsatisfiedRequirementException(String message, Class<?> type, String data) {
        super(message);
        this.type = type;
        this.data = data;
    }

    public UnsatisfiedRequirementException(String message, Throwable cause, Class<?> type, String data) {
        super(message, cause);
        this.type = type;
        this.data = data;
    }

    public UnsatisfiedRequirementException(Throwable cause, Class<?> type, String data) {
        super(cause);
        this.type = type;
        this.data = data;
    }

    protected UnsatisfiedRequirementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> type, String data) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.type = type;
        this.data = data;
    }

    public Class<?> getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
