/*
 *      WCommands - Yet Another Command API! <https://github.com/JonathanxD/WCommands>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 TheRealBuggy/JonathanxD (https://github.com/JonathanxD/ & https://github.com/TheRealBuggy/) <jonathan.scripter@programmer.net>
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
package com.github.jonathanxd.wcommands.infos.requirements;

import com.github.jonathanxd.iutils.function.stream.BiStreams;
import com.github.jonathanxd.iutils.function.stream.MapStream;
import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            opt = BiStreams.mapStream(providedRequirementMap).filter((mapType, req) -> mapType.isAssignableFrom(type)).findFirst();
        }

        return opt.isPresent() && opt.get().getValue().test(data, arguments, commandData, informationRegister, subject);

    }

}
