package com.dhy.utils;

import java.util.regex.Pattern;

public class Regex {
  public static boolean IsCheckout(String str, String rex){
    return Pattern.matches(rex, str);
  }
}
