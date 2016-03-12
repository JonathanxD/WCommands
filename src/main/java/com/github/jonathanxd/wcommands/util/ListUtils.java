package com.github.jonathanxd.wcommands.util;

import java.util.List;

/**
 * Created by jonathan on 11/03/16.
 */
public class ListUtils {

    public static boolean equals(List<?> list, List<?> list2) {
        if (list.size() != list2.size())
            return false;

        if(list.isEmpty() && list2.isEmpty())
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

}
