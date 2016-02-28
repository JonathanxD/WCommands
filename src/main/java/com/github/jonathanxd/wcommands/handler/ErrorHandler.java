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

import com.github.jonathanxd.wcommands.exceptions.ArgumentProcessingError;

/**
 * Created by jonathan on 27/02/16.
 */
public interface ErrorHandler {

    /**
     * Handle the errors!
     *
     * @param error Exception
     * @return True to STOP the process and print the error, or false to continue processing.
     */
    boolean handle(ArgumentProcessingError error);

    class Container {
        private final ErrorHandler handler;

        public Container(ErrorHandler handler) {
            this.handler = handler;
        }

        public void handle(ArgumentProcessingError error) throws ArgumentProcessingError {
            if(!handler.handle(error)) {
                throw error;
            }
        }
    }

}
