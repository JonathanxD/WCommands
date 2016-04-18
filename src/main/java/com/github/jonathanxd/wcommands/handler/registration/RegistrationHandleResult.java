/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
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

    /**
     * Cancel registration
     *
     * @return Canceller
     */
    public static RegistrationHandleResult cancel() {
        return new RegistrationHandleResult(null, null, Action.CANCEL);
    }

    /**
     * Accept registration
     *
     * @return Acceptor
     */
    public static RegistrationHandleResult accept() {
        return new RegistrationHandleResult(null, null, Action.ACCEPT);
    }

    /**
     * Modify registration
     *
     * @param modified Modified command
     * @return Modifier
     */
    public static RegistrationHandleResult modify(CommandSpec modified) {
        return new RegistrationHandleResult(null, modified, Action.MODIFY);
    }

    /**
     * Create a new instance with known instances
     *
     * @param original    Original Command
     * @param commandSpec Modified or Original command
     * @param action      Action
     * @return Return instance with known values
     */
    public static RegistrationHandleResult newInstance(@Nullable CommandSpec original, @Nullable CommandSpec commandSpec, Action action) {
        return new RegistrationHandleResult(original, commandSpec, action);
    }

    /**
     * Apply specified values and {@code registrationHandleResult} values to new instance.
     *
     * @param original                 Original command
     * @param registrationHandleResult Created result
     * @return New instance of registration result with mixed values of a non-null instance of
     * {@code registrationHandleResult}
     */
    public static RegistrationHandleResult applyTo(CommandSpec original, RegistrationHandleResult registrationHandleResult) {
        if (registrationHandleResult.getOriginal().isPresent())
            throw new IllegalStateException("Invalid RegistrationHandleResult! Original Command is present! Desc: " + registrationHandleResult);

        return new RegistrationHandleResult(original, registrationHandleResult.getModifiedCommandSpec().orElse(null), registrationHandleResult.getAction());
    }

    /**
     * If Modified CommandSpec is present, return it, otherwise return original CommandSpec
     *
     * @return If Modified CommandSpec is present, return it, otherwise returns original CommandSpec
     */
    public Optional<CommandSpec> getResult() {
        return getModifiedCommandSpec().isPresent() ? getModifiedCommandSpec() : getOriginal();
    }

    /**
     * Get original command
     *
     * @return original command
     */
    public Optional<CommandSpec> getOriginal() {
        return Optional.ofNullable(original);
    }

    /**
     * Get original or modified command
     *
     * @return Original or modified command
     */
    public Optional<CommandSpec> getModifiedCommandSpec() {
        return Optional.ofNullable(modifiedCommandSpec);
    }

    /**
     * Get action
     *
     * @return Action
     */
    public Action getAction() {
        return action;
    }

    @Override
    public int hashCode() {

        List<Object> objectList = new ArrayList<>();

        if (this.getOriginal().isPresent()) {
            objectList.add(this.getOriginal().get());
        }

        if (this.getModifiedCommandSpec().isPresent()) {
            objectList.add(this.getModifiedCommandSpec().get());
        }

        objectList.add(this.getAction());

        return objectList.hashCode();
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }

    public enum Action {
        ACCEPT,
        CANCEL,
        MODIFY
    }
}
