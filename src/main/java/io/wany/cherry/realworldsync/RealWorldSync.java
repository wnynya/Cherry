package io.wany.cherry.realworldsync;

public class RealWorldSync {

  public static String NAME = "RealWorldSync";
  public static String COLORHEX = "#A8E700";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[" + NAME + "]:&r ";
  public static boolean ENABLE = false;

  public static void onEnable() {
    WeatherSync.onEnable();
    DatetimeSync.onEnable();
  }

  public static void onDisable() {
    WeatherSync.onDisable();
    DatetimeSync.onDisable();
  }

}
