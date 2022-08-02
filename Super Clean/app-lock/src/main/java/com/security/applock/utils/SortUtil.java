package com.security.applock.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortUtil {

    public static final String TIME_ASC_NAME = "Time↑";
    public static final String TIME_DESC_NAME = "Time↓";

    public static final String NAME_ASC_NAME = "Name↑";
    public static final String NAME_DESC_NAME = "Name↓";

    public static final String SIZE_ASC_NAME = "Size↑";
    public static final String SIZE_DESC_NAME = "Size↓";

    public static final String TYPE_ASC_NAME = "Type↑";
    public static final String TYPE_DESC_NAME = "Type↓";

    public static List<String> listSort() {
        List<String> list = new ArrayList<>();
        list.add(TIME_ASC_NAME);
        list.add(TIME_DESC_NAME);
        list.add(NAME_ASC_NAME);
        list.add(NAME_DESC_NAME);
        list.add(SIZE_ASC_NAME);
        list.add(SIZE_DESC_NAME);
        list.add(TYPE_ASC_NAME);
        list.add(TYPE_DESC_NAME);
        return list;
    }

    public static List<File> sortTimeFile(File[] files, Boolean isDESC) {
        List<File> lstData = new ArrayList<>();
        for (File file : files) {
            lstData.add(file);
        }
        Collections.sort(lstData, (o1, o2) -> Long.compare(o1.lastModified(), o2.lastModified()));
        if (isDESC) {
            Collections.reverse(lstData);
        }
        return lstData;
    }

}
