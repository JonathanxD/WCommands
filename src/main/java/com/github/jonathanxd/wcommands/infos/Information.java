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

import com.github.jonathanxd.iutils.function.stream.BiStream;
import com.github.jonathanxd.iutils.function.stream.MapStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jonathan on 12/03/16.
 */
public class Information {

    Map<Object, Object> informationMap = new HashMap<>();

    public static InformationBuilder builder() {
        return new InformationBuilder();
    }

    public <ID, T> void register(ID informationId, T information) {
        informationMap.put(informationId, information);
    }

    public <ID> void remove(ID informationId) {
        informationMap.remove(informationId);
    }

    @SuppressWarnings("unchecked")
    public <ID, T> T getRequired(ID informationId) {
        return (T) informationMap.get(informationId);
    }

    @SuppressWarnings("unchecked")
    public <ID, T> Optional<T> getOptional(ID informationId) {
        if (!informationMap.containsKey(informationId))
            return Optional.empty();

        return Optional.of((T) informationMap.get(informationId));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> optional(Object informationId) {
        if (!informationMap.containsKey(informationId))
            return Optional.empty();

        return Optional.of((T) informationMap.get(informationId));
    }

    @SuppressWarnings("unchecked")
    public <T> T required(Object informationId) {
        return (T) informationMap.get(informationId);
    }

    public BiStream<Object, Object> stream() {
        return MapStream.of(informationMap);
    }

    public static final class InformationBuilder {

        Map<Object, Object> informationMap = new HashMap<>();

        private InformationBuilder() {

        }

        public <ID, T> InformationBuilder with(ID informationId, T informationValue) {
            informationMap.put(informationId, informationValue);
            return this;
        }

        public <ID, T> InformationBuilder remove(ID informationId) {
            informationMap.remove(informationId);
            return this;
        }

        public <ID, T> InformationBuilder remove(ID informationId, T informationValue) {
            informationMap.remove(informationId, informationValue);
            return this;
        }

        public Information build() {
            Information information = new Information();

            informationMap.forEach(information::register);

            return information;
        }

    }
}
