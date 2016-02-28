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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.Translator;
import com.github.jonathanxd.wcommands.text.Text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Created by jonathan on 27/02/16.
 */
public class NumberTranslator implements Translator<Number> {
    public static final Pattern NUMBER_REGEX = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");

    @Override
    public boolean isAcceptable(Text text) {
        return NUMBER_REGEX.matcher(text.getPlainString()).matches();
    }

    @Override
    public Number translate(Text text) {
        return numberConversion(text.getPlainString());
    }


    private static Number numberConversion(String data) {

        if(data.contains(".")) {
            try{
                return Long.valueOf(data);
            }catch (Throwable ignored){}
            try{
                return Double.valueOf(data);
            }catch (Throwable ignored){}
            return new BigDecimal(data);
        }

        try{
            return Byte.valueOf(data);
        }catch (Throwable ignored){}

        try{
            return Short.valueOf(data);
        }catch (Throwable ignored){}
        try{
            return Integer.valueOf(data);
        }catch (Throwable ignored){}
        try{
            return Long.valueOf(data);
        }catch (Throwable ignored){}

        try{
            return new BigInteger(data);
        }catch (Throwable ignored){}

        throw new IllegalArgumentException("Cannot convert '"+data+"' to Number Representation!");
    }

}
