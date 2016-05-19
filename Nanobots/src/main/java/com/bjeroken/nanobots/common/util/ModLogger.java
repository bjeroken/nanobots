package com.bjeroken.nanobots.common.util;

public class ModLogger {

  public static void log(String text) {
    if (Refs.LOGGING)
      System.out.println(text);
  }
}
