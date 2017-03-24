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
package com.github.jonathanxd.wcommands.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ListUtils {

    public static boolean equals(List<?> list, List<?> list2) {
        if (list.size() != list2.size())
            return false;

        if (list.isEmpty() && list2.isEmpty())
            return true;

        for (int x = 0; x < list.size(); ++x) {
            Object element = list.get(x);
            Object element2 = list2.get(x);

            if (element == null && element2 != null
                    || element != null && element2 == null
                    || (element != null && !element.equals(element2))) {
                return false;
            }
        }

        return true;
    }

    public static <T> List<List<T>> combination(T[] elements, int amount) {

        List<List<T>> list = new ArrayList<>();

        int maxSize = elements.length;

        if (amount > maxSize) {
            return Collections.emptyList();
        }


        int combination[] = new int[amount];

        int r = 0;
        int index = 0;

        while (r >= 0) {
            if (index <= (maxSize + (r - amount))) {
                combination[r] = index;

                if (r == amount - 1) {
                    List<T> sList = new ArrayList<>();

                    for (int com : combination) {
                        sList.add(elements[com]);
                    }

                    list.add(sList);

                    index++;
                } else {
                    index = combination[r] + 1;
                    r++;
                }
            } else {
                r--;
                if (r > 0)
                    index = combination[r] + 1;
                else
                    index = combination[0] + 1;
            }
        }

        return list;
    }

    public static <T> List<List<T>> possibilities(List<T> values) {
        List<List<T>> powerList = new LinkedList<>();

        for (int i = 1; i <= values.size(); i++) {
            powerList.addAll(possibilities(values, i));
        }

        return powerList;
    }

    public static <T> List<List<T>> possibilities(List<T> values, int size) {

        if (0 == size) {
            return Collections.singletonList(Collections.emptyList());
        }

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<T>> possibilities = new LinkedList<>();

        T actual = values.iterator().next();

        List<T> child = new LinkedList<>(values);
        child.remove(actual);

        List<List<T>> subSetCombination = possibilities(child, size - 1);

        for (List<T> set : subSetCombination) {
            List<T> newSet = new LinkedList<>(set);
            newSet.add(0, actual);
            possibilities.add(newSet);

            generatePerm(newSet).forEach(p -> {
                if (!possibilities.contains(p)) {
                    possibilities.add(p);
                }
            });
        }

        possibilities.addAll(possibilities(child, size));

        return possibilities;
    }

    public static <E> List<List<E>> generatePerm(List<E> original) {

        original = new ArrayList<>(original);

        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);

        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

}
