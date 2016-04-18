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
package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults.list;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.enums.EnumTranslator;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jonathan on 20/03/16.
 */
public class EnumListTranslator implements Translator<List<Enum>> {

    private final Class enumClass;
    private final Reference<?> reference;

    public EnumListTranslator(Reference<?> reference) {
        this.reference = reference;

        Class<?> enClass = null;

        if(reference.getRelated().length != 0)
            enClass = reference.getRelated()[0].getAClass();

        if(enClass == null || !Enum.class.isAssignableFrom(enClass))
            this.enumClass = null;
        else
            this.enumClass = enClass;
    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean isAcceptable(List<String> text) {

        if(enumClass == null)
            return false;

        EnumTranslator enumTranslator = new EnumTranslator(/*(Class<? extends Enum>)*/enumClass);

        for(String str : text) {
            if(!enumTranslator.isAcceptable(Collections.singletonList(str))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<Enum> translate(List<String> text) {

        if(enumClass == null) {
            return Collections.emptyList();
        }

        List<Enum> enumList = new ArrayList<>();

        EnumTranslator enumTranslator = new EnumTranslator(/*(Class<? extends Enum>)*/enumClass);

        for(String str : text) {
            enumList.add(enumTranslator.translate(Collections.singletonList(str)));
        }

        return enumList;
    }
}
