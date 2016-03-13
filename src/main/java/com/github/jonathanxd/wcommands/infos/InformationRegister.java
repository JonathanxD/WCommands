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

import com.github.jonathanxd.iutils.optional.Require;
import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.common.command.CommandList;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by jonathan on 12/03/16.
 */
public class InformationRegister {

    Set<Information<?>> informationList = new HashSet<>();

    public static InformationBuilder builder(WCommand<?> wCommand) {
        InformationBuilder builder = blankBuilder();
        builder.with(WCommand.class, wCommand);
        return builder;
    }

    public static InformationBuilder builderWithList(WCommand<?> wCommand) {
        InformationBuilder builder = blankBuilder();
        builder.with(CommandList.class, wCommand.getCommandList());
        return builder;
    }

    public static InformationBuilder blankBuilder() {
        return new InformationBuilder();
    }


    public <ID, T> void register(ID informationId, T information) {
        informationList.add(new Information<>(informationId, information));
    }

    public <ID, T> void register(ID informationId, T information, String description) {
        informationList.add(new Information<>(informationId, information, description));
    }

    public <ID> void remove(ID informationId) {
        getById(informationId).ifPresent(info -> informationList.remove(info));
    }

    @SuppressWarnings("unchecked")
    private Optional<Information<?>> getById(Object informationId) {
        return informationList
                .stream()
                .filter(i -> i.getId().equals(informationId))
                .findAny();

    }

    @SuppressWarnings("unchecked")
    private <ID, T> Optional<T> getValById(ID informationId) {
        Optional<Information<?>> optional = getById(informationId);

        if (!optional.isPresent())
            return Optional.empty();
        else
            return Optional.of((T) optional.get().get());
    }

    @SuppressWarnings("unchecked")
    public <ID, T> T getRequired(ID informationId) {
        return (T) Require.require(getById(informationId), "The information id='" + informationId + "' is required.").get();
    }

    @SuppressWarnings("unchecked")
    public <ID, T> Optional<T> getOptional(ID informationId) {
        return this.getValById(informationId);

    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> optional(Object informationId) {
        return getValById(informationId);
    }

    @SuppressWarnings("unchecked")
    public <T> T required(Object informationId) {
        return getRequired(informationId);
    }

    public Stream<Information<?>> stream() {

        return informationList.stream();
    }

    public static final class InformationBuilder {

        Set<Information<?>> informationSet = new HashSet<>();

        private InformationBuilder() {

        }

        public <ID, T> InformationBuilder with(ID informationId, T informationValue) {
            informationSet.add(new Information<>(informationId, informationValue));
            return this;
        }

        public <ID, T> InformationBuilder with(ID informationId, T informationValue, String description) {
            informationSet.add(new Information<>(informationId, informationValue, description));
            return this;
        }

        public <ID, T> InformationBuilder remove(ID informationId) {
            informationSet.removeIf(info -> info.getId() == informationId);
            return this;
        }

        public <ID, T> InformationBuilder remove(ID informationId, T informationValue) {
            informationSet.removeIf(info -> info.getId().equals(informationId) && info.get().equals(informationValue));
            return this;
        }

        public InformationRegister build() {
            InformationRegister informationRegister = new InformationRegister();

            informationRegister.informationList.addAll(informationSet);

            return informationRegister;
        }

    }
}
