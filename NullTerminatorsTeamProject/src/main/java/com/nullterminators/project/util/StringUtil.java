package com.nullterminators.project.util;


/**
 * Utility class for String operations.
 */
public class StringUtil {

  public static boolean isNullOrBlank(String str) {
    return str == null || str.trim().isEmpty();
  }
}
