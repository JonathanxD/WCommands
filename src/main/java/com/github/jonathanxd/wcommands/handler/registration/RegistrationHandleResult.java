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
package com.github.jonathanxd.wcommands.handler.registration;

import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.util.reflection.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Created by jonathan on 21/03/16.
 */

/**
 *
 */
public class RegistrationHandleResult {

    private final CommandSpec original;
    private final CommandSpec modifiedCommandSpec;
    private final Action action;

    RegistrationHandleResult(CommandSpec original, CommandSpec modifiedCommandSpec, Action action) {
        this.original = original;
        this.modifiedCommandSpec = modifiedCommandSpec;
        this.action = action;
    }

    public enum Action {
        ACCEPT,
        CANCEL,
        MODIFY
    }

    /**
     * Get original command
     * @return original command
     */
    public Optional<CommandSpec> getOriginal() {
        return Optional.ofNullable(original);
    }

    /**
     * Get original or modified command
     * @return Original or modified command
     */
    public Optional<CommandSpec> getModifiedCommandSpec() {
        return Optional.ofNullable(modifiedCommandSpec);
    }

    /**
     * Get action
     * @return Action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Cancel registration
     * @return Canceller
     */
    public static RegistrationHandleResult cancel() {
        return new RegistrationHandleResult(null, null, Action.CANCEL);
    }

    /**
     * Accept registration
     * @return Acceptor
     */
    public static RegistrationHandleResult accept() {
        return new RegistrationHandleResult(null, null, Action.ACCEPT);
    }

    /**
     * Modify registration
     * @param modified Modified command
     * @return Modifier
     */
    public static RegistrationHandleResult modify(CommandSpec modified) {
        return new RegistrationHandleResult(null, modified, Action.MODIFY);
    }

    /**
     * Create a new instance with known instances
     * @param original Original Command
     * @param commandSpec Modified or Original command
     * @param action Action
     * @return Return instance with known values
     */
    public static RegistrationHandleResult newInstance(@Nullable CommandSpec original, @Nullable CommandSpec commandSpec, Action action) {
        return new RegistrationHandleResult(original, commandSpec, action);
    }

    /**
     * Apply specified values and {@code registrationHandleResult} values to new instance.
     * @param original Original command
     * @param registrationHandleResult    Created result
     * @return New instance of registration result with mixed values of a non-null instance of {@code registrationHandleResult}
     */
    public static RegistrationHandleResult applyTo(CommandSpec original, RegistrationHandleResult registrationHandleResult) {
        if(registrationHandleResult.getOriginal().isPresent())
            throw new IllegalStateException("Invalid RegistrationHandleResult! Original Command is present! Desc: "+registrationHandleResult);

        return new RegistrationHandleResult(original, registrationHandleResult.getModifiedCommandSpec().orElse(null), registrationHandleResult.getAction());
    }

    @Override
    public int hashCode() {

        List<Object> objectList = new ArrayList<>();

        if(this.getOriginal().isPresent()) {
            objectList.add(this.getOriginal().get());
        }

        if(this.getModifiedCommandSpec().isPresent()) {
            objectList.add(this.getModifiedCommandSpec().get());
        }

        objectList.add(this.getAction());

        return objectList.hashCode();
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
