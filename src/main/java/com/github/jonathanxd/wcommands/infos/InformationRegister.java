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
import com.github.jonathanxd.wcommands.util.reflection.ToString;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by jonathan on 12/03/16.
 */
public class InformationRegister {

    Set<Information<?>> informationList = new HashSet<>();

    public static InformationBuilder builder(WCommand<?> wCommand) {
        InformationBuilder builder = blankBuilder();
        builder.with(WCommand.WCOMMAND_INFOID, wCommand);
        return builder;
    }

    public static InformationBuilder builderWithList(WCommand<?> wCommand) {
        InformationBuilder builder = blankBuilder();
        builder.with(CommandList.COMMANDLIST_INFOID, wCommand.getCommandList());
        return builder;
    }

    public static InformationBuilder blankBuilder() {
        return new InformationBuilder();
    }


    public <T> void register(InfoId informationId, T information) {
        informationList.add(new Information<>(informationId, information));
    }

    public <T> void register(InfoId informationId, T information, String description) {
        informationList.add(new Information<>(informationId, information, description));
    }

    public void remove(InfoId informationId) {
        getById(informationId).ifPresent(info -> informationList.remove(info));
    }

    @SuppressWarnings("unchecked")
    public Optional<Information<?>> getById(InfoId informationId) {
        return informationList
                .stream()
                .filter(i -> i.getId().equals(informationId))
                .findAny();

    }

    @SuppressWarnings("unchecked")
    public Optional<Information<?>> get(Predicate<Information<?>> predicate) {
        return informationList
                .stream()
                .filter(predicate)
                .findAny();

    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> getValById(InfoId informationId) {
        Optional<Information<?>> optional = getById(informationId);

        if (!optional.isPresent())
            return Optional.empty();
        else
            return Optional.of((T) optional.get().get());
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> getVal(Predicate<Information<?>> predicate) {
        Optional<Information<?>> optional = get(predicate);

        if (!optional.isPresent())
            return Optional.empty();
        else
            return Optional.of((T) optional.get().get());
    }


    @SuppressWarnings("unchecked")
    public <T> T getRequired(InfoId informationId) {
        return (T) Require.require(getById(informationId), "The information id='" + informationId + "' is required.").get();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(InfoId informationId) {
        return this.getValById(informationId);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(String tag, Class<?> informationId) {
        return this.getVal(i -> i.isPresent() && i.getId().matchRequirements(tag, informationId));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(String[] tags, Class<?> informationId) {
        return this.getVal(i -> i.isPresent() && i.getId().matchRequirements(tags, informationId));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(Class<?> informationId) {
        return this.getVal(i -> i.isPresent() && i.getId().matchRequirements((String) null, informationId));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(String[] tags) {
        return this.getVal(i -> i.isPresent() && i.getId().matchRequirements(tags, null));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> optional(InfoId informationId) {
        return getValById(informationId);
    }

    @SuppressWarnings("unchecked")
    public <T> T required(InfoId informationId) {
        return getRequired(informationId);
    }

    public Set<Information<?>> getInformationList() {
        return new HashSet<>(informationList);
    }

    public Stream<Information<?>> stream() {

        return informationList.stream();
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }

    public static final class InformationBuilder {

        Set<Information<?>> informationSet = new HashSet<>();

        private InformationBuilder() {

        }

        private static <T> Information<?> buildSimple(Class<?> type, String tag, T o, String description) {
            if (tag == null)
                return build(type, (String[]) null, o, description);
            else
                return build(type, new String[]{tag}, o, description);
        }

        private static <T> Information<?> build(Class<?> type, String[] tag, T o, String description) {

            Class<?> oClass = o.getClass();

            if (type == null) type = oClass;
            if (tag == null) tag = new String[]{oClass.getSimpleName()};

            if (description == null)
                return new Information<>(new InfoId(tag, type), o);
            else
                return new Information<>(new InfoId(tag, type), o, description);
        }

        public <T> InformationBuilder with(T informationValue) {
            informationSet.add(build(null, null, informationValue, null));
            return this;
        }

        public <T> InformationBuilder with(T informationValue, String description) {
            informationSet.add(build(null, null, informationValue, description));
            return this;
        }

        //////////////////////////////////////////////////////////////////////////////////////

        public <T> InformationBuilder with(String informationString, T informationValue) {
            informationSet.add(buildSimple(null, informationString, informationValue, null));
            return this;
        }

        public <T> InformationBuilder with(String informationString, T informationValue, String description) {
            informationSet.add(buildSimple(null, informationString, informationValue, description));
            return this;
        }

        public <T> InformationBuilder with(Class<?> idType, T informationValue) {
            informationSet.add(build(idType, null, informationValue, null));
            return this;
        }

        public <T> InformationBuilder with(Class<?> idType, T informationValue, String description) {
            informationSet.add(build(idType, null, informationValue, description));
            return this;
        }

        public <T> InformationBuilder with(Class<?> idType, String tag, T informationValue, String description) {
            informationSet.add(buildSimple(idType, tag, informationValue, description));
            return this;
        }

        public <T> InformationBuilder with(Class<?> idType, String[] tag, T informationValue, String description) {
            informationSet.add(build(idType, tag, informationValue, description));
            return this;
        }

        //////////////////////////////////////////////////////////////////////////////////////

        public <T> InformationBuilder with(InfoId informationId, T informationValue) {
            informationSet.add(new Information<>(informationId, informationValue));
            return this;
        }

        public <T> InformationBuilder with(InfoId informationId, T informationValue, String description) {
            informationSet.add(new Information<>(informationId, informationValue, description));
            return this;
        }

        //////////////////////////////////////////////////////////////////////////////////////

        public InformationBuilder remove(InfoId informationId) {
            informationSet.removeIf(info -> info.getId() == informationId);
            return this;
        }

        public <T> InformationBuilder remove(InfoId informationId, T informationValue) {
            informationSet.removeIf(info -> info.getId().equals(informationId) && info.get().equals(informationValue));
            return this;
        }

        //////////////////////////////////////////////////////////////////////////////////////

        public InformationRegister build() {
            InformationRegister informationRegister = new InformationRegister();

            informationRegister.informationList.addAll(informationSet);

            return informationRegister;
        }

    }
}
