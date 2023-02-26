package com.github.natanfoleto.kabanaprisao.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TypeUtils {
    public static boolean isInteger(String str) {
        return str != null && str.matches("[0-9]*");
    }

    public static String getStringWithoutTarget(String[] args) {
        List<String> newArgs = new LinkedList<>(Arrays.asList(args));

        newArgs.remove(0);

        return String.join(" ", newArgs);
    }
}

