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
package com.github.jonathanxd.wcommands.infos;

import com.github.jonathanxd.iutils.annotations.Immutable;
import com.github.jonathanxd.iutils.optional.Require;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by jonathan on 12/03/16.
 */

/**
 * Description class is used to register descriptions. Normally the description will be provided by
 * registration (as description provided by registration {@link #getProvidedByRegistration()}) and
 * {@link com.github.jonathanxd.wcommands.ext.reflect.infos.Info} annotation (as unknown source
 * {@link #getProvidedByUnknownSource()}).
 */
public class Description implements Cloneable {

    public final Optional<String> providedByRegistration;
    public final List<String> providedByUnknownSource = new ArrayList<>();

    public Description(String providedByRegistration) {
        this.providedByRegistration = Optional.ofNullable(providedByRegistration);
    }

    /**
     * Provide a description to unknown source list
     *
     * @param desc Description to provide
     */
    public void provide(String desc) {
        this.providedByUnknownSource.add(desc);
    }

    /**
     * Return true if has description provided by registration
     *
     * @return Return true if has description provided by registration
     */
    public boolean hasProvidedByRegistration() {
        return providedByRegistration.isPresent();
    }

    /**
     * Get Description provided by register
     *
     * @return Description provided by register
     * @throws IllegalStateException If the description is not provided by registration.
     */
    public String getProvidedByRegistration() {
        return Require.require(providedByRegistration);
    }

    /**
     * Get a list of descriptions provided by unknown source
     *
     * @return A list of descriptions provided by unknown source
     */
    @Immutable
    public List<String> getProvidedByUnknownSource() {
        return Collections.unmodifiableList(providedByUnknownSource);
    }

    /**
     * Get last description provided by unknown source
     *
     * @return Last description provided by unknown source
     */
    public String lastProvided() {
        if (providedByUnknownSource.isEmpty()) {
            return null;
        } else {
            return providedByUnknownSource.get(providedByUnknownSource.size() - 1);
        }
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Description clone() {
        Description description = new Description(this.providedByRegistration.orElse(null));
        description.providedByUnknownSource.addAll(this.getProvidedByUnknownSource());
        return description;
    }
}
