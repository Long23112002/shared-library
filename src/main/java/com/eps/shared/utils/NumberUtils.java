package com.eps.shared.utils;

public class NumberUtils {

  public static int parseInt(String number, int defaultNumber) {

    try {
      return Integer.parseInt(number);
    } catch (Exception e) {

      return defaultNumber;
    }
  }
}
