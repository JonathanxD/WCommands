package com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.defaults;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;

import java.util.Collections;
import java.util.List;

/**
 * Created by jonathan on 20/03/16.
 */
public class StringListTranslator implements Translator<List<String>> {
    @Override
    public boolean isAcceptable(List<String> text) {
        return !text.isEmpty();
    }

    @Override
    public List<String> translate(List<String> text) {
        return Collections.unmodifiableList(text);
    }
}
