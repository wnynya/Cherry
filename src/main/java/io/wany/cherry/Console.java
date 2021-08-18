package io.wany.cherry;

import io.wany.cherry.amethyst.Color;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class Console {

  public static void message(Component message) {
    Bukkit.getConsoleSender().sendMessage(message);
  }

  public static void log(String message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + message) + "\u001b[0m"));
  }

  public static void log(Component message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + Message.stringify(message)) + "\u001b[0m"));
  }

  public static void log(int message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + "&6" + message) + "\u001b[0m"));
  }

  public static void log(float message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + "&6" + message) + "\u001b[0m"));
  }

  public static void log(long message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + "&6" + message) + "\u001b[0m"));
  }

  public static void log(double message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + "&6" + message) + "\u001b[0m"));
  }

  public static void log(boolean message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + "&b" + message) + "\u001b[0m"));
  }

  public static void log(char message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + message) + "\u001b[0m"));
  }

  public static void warn(String message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + message) + "\u001b[0m"));
  }

  public static void error(String message) {
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + message) + "\u001b[0m"));
  }

  public static void debug(String message) {
    if (!Cherry.DEBUG) {
      return;
    }
    Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(Message.effect(Prefix.CHERRY + Cherry.COLOR + Prefix.DEBUG + message) + "\u001b[0m"));
  }

  public static class Prefix {
    public static String CHERRY = "[Cherry]&r ";
    public static String DEBUG = "[Debug]:&r ";
  }

}
