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
