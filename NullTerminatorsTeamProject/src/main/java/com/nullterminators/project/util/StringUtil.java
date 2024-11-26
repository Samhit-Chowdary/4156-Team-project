package com.nullterminators.project.util;

public class StringUtil {
    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
