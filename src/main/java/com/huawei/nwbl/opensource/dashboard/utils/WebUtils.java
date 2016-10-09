package com.huawei.nwbl.opensource.dashboard.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Web format utils for thymeleaf
 */
public class WebUtils {

    public static String formatSet(Set<Object> objects) {
        List<String> stringList = new ArrayList<>();
        for (Object object : objects) {
            stringList.add(object.toString());
        }
        Collections.sort(stringList);
        return String.join(",", stringList);
    }

}
