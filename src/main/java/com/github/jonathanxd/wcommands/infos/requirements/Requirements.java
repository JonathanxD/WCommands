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
package com.github.jonathanxd.wcommands.infos.requirements;

import com.github.jonathanxd.iutils.function.stream.MapStream;
import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jonathan on 18/03/16.
 */
public class Requirements {
    //@Require(type = Permission.class, tags = {"smp.lithium"})
    private final Map<Class<?>, ProvidedRequirement> providedRequirementMap = new HashMap<>();

    public void add(Class<?> type, ProvidedRequirement providedRequirement) {
        if (!providedRequirementMap.containsKey(type))
            providedRequirementMap.put(type, providedRequirement);
    }

    public void overwrite(Class<?> type, ProvidedRequirement providedRequirement) {
        remove(type);
        add(type, providedRequirement);
    }

    public void remove(Class<?> type) {
        providedRequirementMap.remove(type);
    }

    // provide(new InformationProvided("deep"));

    public boolean test(Class<?> type, String data, Object[] arguments, CommandData<CommandHolder> commandData, InformationRegister informationRegister, Information<?> subject) {
        Optional<Node<Class<?>, ProvidedRequirement>> opt;

        if (providedRequirementMap.containsKey(type)) {
            opt = Optional.of(new Node<>(null, providedRequirementMap.get(type)));
        } else {
            opt = MapStream.of(providedRequirementMap).filter((mapType, req) -> mapType.isAssignableFrom(type)).findFirst();
        }

        return opt.isPresent() && opt.get().getValue().test(data, arguments, commandData, informationRegister, subject);

    }

}
