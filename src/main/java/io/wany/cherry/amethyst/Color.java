package io.wany.cherry.amethyst;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Color {

  private int red = 0;
  private int green = 0;
  private int blue = 0;
  private int alpha = 255;

  public Color(@NotNull Type type, @NotNull String s) {
    switch (type) {
      case HEX -> {
        Matcher m8 = ColorPattern.HEXA_8.matcher(s);
        Matcher m6 = ColorPattern.HEX_6.matcher(s);
        Matcher m4 = ColorPattern.HEXA_4.matcher(s);
        Matcher m3 = ColorPattern.HEX_3.matcher(s);
        Matcher m2 = ColorPattern.HEX_2.matcher(s);
        Matcher m1 = ColorPattern.HEX_1.matcher(s);

        if (m8.find()) {
          red = Integer.parseInt(m8.group(1) + m8.group(2) + "", 16);
          green = Integer.parseInt(m8.group(3) + m8.group(4) + "", 16);
          blue = Integer.parseInt(m8.group(5) + m8.group(6) + "", 16);
          alpha = Integer.parseInt(m8.group(7) + m8.group(8) + "", 16);
        }
        else if (m6.find()) {
          red = Integer.parseInt(m6.group(1) + m6.group(2) + "", 16);
          green = Integer.parseInt(m6.group(3) + m6.group(4) + "", 16);
          blue = Integer.parseInt(m6.group(5) + m6.group(6) + "", 16);
        }
        else if (m4.find()) {
          red = Integer.parseInt(m4.group(1) + m4.group(1) + "", 16);
          green = Integer.parseInt(m4.group(2) + m4.group(2) + "", 16);
          blue = Integer.parseInt(m4.group(3) + m4.group(3) + "", 16);
          alpha = Integer.parseInt(m4.group(4) + m4.group(4) + "", 16);
        }
        else if (m3.find()) {
          red = Integer.parseInt(m3.group(1) + m3.group(1) + "", 16);
          green = Integer.parseInt(m3.group(2) + m3.group(2) + "", 16);
          blue = Integer.parseInt(m3.group(3) + m3.group(3) + "", 16);
        }
        else if (m2.find()) {
          red = Integer.parseInt(m2.group(1) + m2.group(2) + "", 16);
          green = Integer.parseInt(m2.group(1) + m2.group(2) + "", 16);
          blue = Integer.parseInt(m2.group(1) + m2.group(2) + "", 16);
        }
        else if (m1.find()) {
          red = Integer.parseInt(m1.group(1) + m1.group(1) + "", 16);
          green = Integer.parseInt(m1.group(1) + m1.group(1) + "", 16);
          blue = Integer.parseInt(m1.group(1) + m1.group(1) + "", 16);
        }
      }
      case RGB -> {
        Matcher m1 = ColorPattern.RGB.matcher(s);
        Matcher m2 = ColorPattern.RGBA.matcher(s);
        Matcher m3 = ColorPattern.RGBA_S.matcher(s);

        if (m3.find()) {
          String head = m3.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'r' -> red = Integer.parseInt(m3.group((n + 1) * 3) + "");
              case 'g' -> green = Integer.parseInt(m3.group((n + 1) * 3) + "");
              case 'b' -> blue = Integer.parseInt(m3.group((n + 1) * 3) + "");
              case 'a' -> alpha = Integer.parseInt(m3.group((n + 1) * 3) + "");
            }
          }
        }
        else if (m2.find()) {
          red = Integer.parseInt(m2.group(1) + "");
          green = Integer.parseInt(m2.group(4) + "");
          blue = Integer.parseInt(m2.group(7) + "");
          alpha = Integer.parseInt(m2.group(10) + "");
        }
        else if (m1.find()) {
          red = Integer.parseInt(m1.group(1) + "");
          green = Integer.parseInt(m1.group(4) + "");
          blue = Integer.parseInt(m1.group(7) + "");
        }
      }
      case HSL -> {
        Matcher m1 = ColorPattern.HSL.matcher(s);
        Matcher m2 = ColorPattern.HSLA.matcher(s);
        Matcher m3 = ColorPattern.HSLA_S.matcher(s);

        double hue = 0;
        double saturation = 0;
        double lightness = 0;

        if (m3.find()) {
          String head = m3.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'h' -> hue = Integer.parseInt(m3.group((n + 1) * 3) + "") / 360.0;
              case 's' -> saturation = Integer.parseInt(m3.group((n + 1) * 3) + "") / 100.0;
              case 'l' -> lightness = Integer.parseInt(m3.group((n + 1) * 3) + "") / 100.0;
            }
          }
        }
        else if (m2.find()) {
          hue = Integer.parseInt(m2.group(1) + "") / 360.0;
          saturation = Integer.parseInt(m2.group(4) + "") / 100.0;
          lightness = Integer.parseInt(m2.group(7) + "") / 100.0;
          alpha = Integer.parseInt(m2.group(10) + "");
        }
        else if (m1.find()) {
          hue = Integer.parseInt(m1.group(1) + "") / 360.0;
          saturation = Integer.parseInt(m1.group(4) + "") / 100.0;
          lightness = Integer.parseInt(m1.group(7) + "") / 100.0;
        }

        System.out.println(hue + " " + saturation + " " + lightness);

        double r, g, b;

        if (saturation == 0) {
          System.out.println(0);
          r = lightness;
          g = lightness;
          b = lightness;
        }
        else {
          double q = lightness < 0.5 ? lightness * (1 + saturation) : lightness + saturation - lightness * saturation;
          double p = 2 * lightness - q;
          r = hue2rgb(p, q, hue + 1 / 3.0);
          g = hue2rgb(p, q, hue);
          b = hue2rgb(p, q, hue - 1 / 3.0);
        }

        red = (int) Math.round(r * 255);
        green = (int) Math.round(g * 255);
        blue = (int) Math.round(b * 255);
      }
      case CMYK -> {
        Matcher m1 = ColorPattern.CYMK.matcher(s);
        Matcher m2 = ColorPattern.CYMK_S.matcher(s);

        double cyan = 0;
        double magenta = 0;
        double yellow = 0;
        double key = 0;

        if (m2.find()) {
          String head = m2.group(1).toLowerCase();
          for (int n = 0; n < head.length(); n++) {
            switch (head.charAt(n)) {
              case 'c' -> cyan = Integer.parseInt(m2.group((n + 1) * 3) + "") / 100.0;
              case 'm' -> magenta = Integer.parseInt(m2.group((n + 1) * 3) + "") / 100.0;
              case 'y' -> yellow = Integer.parseInt(m2.group((n + 1) * 3) + "") / 100.0;
              case 'k' -> key = Integer.parseInt(m2.group((n + 1) * 3) + "") / 100.0;
            }
          }
        }
        else if (m1.find()) {
          cyan = Integer.parseInt(m1.group(1) + "") / 100.0;
          magenta = Integer.parseInt(m1.group(4) + "") / 100.0;
          yellow = Integer.parseInt(m1.group(7) + "") / 100.0;
          key = Integer.parseInt(m1.group(10) + "") / 100.0;
        }

        red = (int) (255.0 * (1.0 - cyan) * (1.0 - key));
        green = (int) (255.0 * (1.0 - magenta) * (1.0 - key));
        blue = (int) (255.0 * (1.0 - yellow) * (1.0 - key));
      }
      case MFC -> {
        Matcher m = ColorPattern.MFC.matcher(s);
        if (m.find()) {
          red = Integer.parseInt(m.group(3) + m.group(5) + "", 16);
          green = Integer.parseInt(m.group(7) + m.group(9) + "", 16);
          blue = Integer.parseInt(m.group(11) + m.group(13) + "", 16);
          break;
        }

        Pattern pattern_mfc_single = Pattern.compile("([§&])?([0-9a-fA-F])");
        Matcher m2 = pattern_mfc_single.matcher(s);
        if (m2.find()) {
          String val = m2.group(2);
          switch (val) {
            case "0" -> {
              red = 0;
              green = 0;
              blue = 0;
            }
            case "1" -> {
              red = 0;
              green = 0;
              blue = 170;
            }
            case "2" -> {
              red = 0;
              green = 170;
              blue = 0;
            }
            case "3" -> {
              red = 0;
              green = 170;
              blue = 170;
            }
            case "4" -> {
              red = 170;
              green = 0;
              blue = 0;
            }
            case "5" -> {
              red = 170;
              green = 0;
              blue = 170;
            }
            case "6" -> {
              red = 255;
              green = 170;
              blue = 0;
            }
            case "7" -> {
              red = 170;
              green = 170;
              blue = 170;
            }
            case "8" -> {
              red = 85;
              green = 85;
              blue = 85;
            }
            case "9" -> {
              red = 85;
              green = 85;
              blue = 255;
            }
            case "a" -> {
              red = 85;
              green = 255;
              blue = 85;
            }
            case "b" -> {
              red = 85;
              green = 255;
              blue = 255;
            }
            case "c" -> {
              red = 255;
              green = 85;
              blue = 85;
            }
            case "d" -> {
              red = 255;
              green = 85;
              blue = 255;
            }
            case "e" -> {
              red = 255;
              green = 255;
              blue = 85;
            }
            case "f" -> {
              red = 255;
              green = 255;
              blue = 255;
            }
          }
        }
      }
      case ANSI -> {
        Matcher m = ColorPattern.ANSI_RGB.matcher(s);
        if (m.find()) {
          red = Integer.parseInt(m.group(1) + "");
          green = Integer.parseInt(m.group(4) + "");
          blue = Integer.parseInt(m.group(7) + "");
        }

        Pattern pattern_ansi_single_dark = Pattern.compile("([34])([0-7])");
        Matcher m2 = pattern_ansi_single_dark.matcher(s);
        if (m2.find()) {
          String val = m2.group(2);
          switch (val) {
            case "0" -> {
              red = 0;
              green = 0;
              blue = 0;
            }
            case "1" -> {
              red = 0;
              green = 0;
              blue = 170;
            }
            case "2" -> {
              red = 0;
              green = 170;
              blue = 0;
            }
            case "3" -> {
              red = 0;
              green = 170;
              blue = 170;
            }
            case "4" -> {
              red = 170;
              green = 0;
              blue = 0;
            }
            case "5" -> {
              red = 170;
              green = 0;
              blue = 170;
            }
            case "6" -> {
              red = 255;
              green = 170;
              blue = 0;
            }
            case "7" -> {
              red = 170;
              green = 170;
              blue = 170;
            }
          }
        }

        Pattern pattern_ansi_single_light = Pattern.compile("(9|10)([0-7])");
        Matcher m3 = pattern_ansi_single_light.matcher(s);
        if (m3.find()) {
          String val = m3.group(2);
          switch (val) {
            case "0" -> {
              red = 85;
              green = 85;
              blue = 85;
            }
            case "1" -> {
              red = 85;
              green = 85;
              blue = 255;
            }
            case "2" -> {
              red = 85;
              green = 255;
              blue = 85;
            }
            case "3" -> {
              red = 85;
              green = 255;
              blue = 255;
            }
            case "4" -> {
              red = 255;
              green = 85;
              blue = 85;
            }
            case "5" -> {
              red = 255;
              green = 85;
              blue = 255;
            }
            case "6" -> {
              red = 255;
              green = 255;
              blue = 85;
            }
            case "7" -> {
              red = 255;
              green = 255;
              blue = 255;
            }
          }
        }
      }
    }
    red = Math.max(0, Math.min(255, red));
    green = Math.max(0, Math.min(255, green));
    blue = Math.max(0, Math.min(255, blue));
    alpha = Math.max(0, Math.min(255, alpha));
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }

  public int getAlpha() {
    return alpha;
  }

  public int getCyan() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double k = 1 - Math.max(r, Math.max(g, b));
    return (int) ((1 - r - k) / (1 - k));
  }

  public int getMagenta() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double k = 1 - Math.max(r, Math.max(g, b));
    return (int) ((1 - g - k) / (1 - k));
  }

  public int getYellow() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double k = 1 - Math.max(r, Math.max(g, b));
    return (int) ((1 - b - k) / (1 - k));
  }

  public int getKey() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    return (int) (1 - Math.max(r, Math.max(g, b)));
  }

  public int getHue() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double max = Math.max(r, Math.max(g, b));
    double min = Math.min(r, Math.min(g, b));
    double hue = (max + min) / 2;
    if (max == min) {
      hue = 0;
    }
    else {
      var d = max - min;
      if (max == r) {
        hue = (g - b) / d + (g < b ? 6 : 0);
      }
      else if (max == g) {
        hue = (b - r) / d + 2;
      }
      else if (max == b) {
        hue = (r - g) / d + 4;
      }
      hue /= 6;
    }
    return (int) hue;
  }

  public int getSaturation() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double max = Math.max(r, Math.max(g, b));
    double min = Math.min(r, Math.min(g, b));
    double saturation;
    double lightness = (max + min) / 2;
    if (max == min) {
      saturation = 0;
    }
    else {
      var d = max - min;
      saturation = lightness > 0.5 ? d / (2 - max - min) : d / (max + min);
    }
    return (int) saturation;
  }

  public int getLightness() {
    double r = getRed() / 255.0;
    double g = getGreen() / 255.0;
    double b = getBlue() / 255.0;
    double max = Math.max(r, Math.max(g, b));
    double min = Math.min(r, Math.min(g, b));
    double lightness = (max + min) / 2;
    return (int) lightness;
  }

  public String getRedAsHex() {
    return Integer.toHexString(0x100 | red).substring(1);
  }

  public String getGreenAsHex() {
    return Integer.toHexString(0x100 | green).substring(1);
  }

  public String getBlueAsHex() {
    return Integer.toHexString(0x100 | blue).substring(1);
  }

  public String getAlphaAsHex() {
    return Integer.toHexString(0x100 | alpha).substring(1);
  }

  public int[] getRGB() {
    return new int[]{getRed(), getGreen(), getBlue()};
  }

  public int[] getRGBA() {
    return new int[]{getRed(), getGreen(), getBlue(), getAlpha()};
  }

  public String[] getHEX() {
    return new String[]{getRedAsHex(), getGreenAsHex(), getBlueAsHex()};
  }

  public String[] getHEXWithAlpha() {
    return new String[]{getRedAsHex(), getGreenAsHex(), getBlueAsHex(), getAlphaAsHex()};
  }

  public int[] getHSL() {
    return new int[]{getHue(), getSaturation(), getLightness()};
  }

  public int[] getHSLA() {
    return new int[]{getHue(), getSaturation(), getLightness(), getAlpha()};
  }

  public int[] getCMYK() {
    return new int[]{getCyan(), getMagenta(), getYellow(), getKey()};
  }

  public String getRGBString() {
    return "rgb(" + getRed() + "," + getGreen() + "," + getBlue() + ")";
  }

  public String getRGBAString() {
    return "rgba(" + getRed() + "," + getGreen() + "," + getBlue() + "," + getAlpha() + ")";
  }

  public String getHSLString() {
    return "rgb(" + getHue() + "," + getSaturation() + "," + getLightness() + ")";
  }

  public String getHSLAString() {
    return "rgba(" + getHue() + "," + getSaturation() + "," + getLightness() + "," + getAlpha() + ")";
  }

  public String getHexString() {
    return "#" + getRedAsHex() + getGreenAsHex() + getBlueAsHex();
  }

  public String getHexStringWithAlpha() {
    return "#" + getRedAsHex() + getGreenAsHex() + getBlueAsHex() + getAlphaAsHex();
  }

  public String getCMYKString() {
    return "cmyk(" + getCyan() + "," + getMagenta() + "," + getYellow() + "," + getKey() + ")";
  }

  public String getMFC() {
    switch (getRGBString()) {
      case "0,0,0" -> {
        return "§0";
      }
      case "0,0,170" -> {
        return "§1";
      }
      case "0,170,0" -> {
        return "§2";
      }
      case "0,170,170" -> {
        return "§3";
      }
      case "170,0,0" -> {
        return "§4";
      }
      case "170,0,170" -> {
        return "§5";
      }
      case "255,170,0" -> {
        return "§6";
      }
      case "170,170,170" -> {
        return "§7";
      }
      case "85,85,85" -> {
        return "§8";
      }
      case "85,85,255" -> {
        return "§9";
      }
      case "85,255,85" -> {
        return "§a";
      }
      case "85,255,255" -> {
        return "§b";
      }
      case "255,85,85" -> {
        return "§c";
      }
      case "255,85,255" -> {
        return "§d";
      }
      case "255,255,85" -> {
        return "§e";
      }
      case "255,255,255" -> {
        return "§f";
      }
      default -> {
        String[] hex = getHEX();
        String h = hex[0] + hex[1] + hex[2];
        return h.replaceAll("([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])", "§x§$1§$2§$3§$4§$5§$6");
      }
    }
  }

  public String getANSIEscape() {
    return "\u001b[38;2;" + getRed() + ";" + getGreen() + ";" + getBlue() + "m";
  }

  // net.kyori.adventure.text.format.TextColor;
  public TextColor getTextColor() {
    return TextColor.color(this.getRed(), this.getGreen(), this.getBlue());
  }

  public enum Type {
    HEX, RGB, CMYK, HSL, MFC, ANSI
  }

  private static class ColorPattern {

    public static Pattern HEX_6 =Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
    public static Pattern HEX_3 =Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
    public static Pattern HEX_2 = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])");
    public static Pattern HEX_1 = Pattern.compile("([0-9a-fA-F])");
    public static Pattern HEXA_8 =Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
    public static Pattern HEXA_4 =Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");

    public static Pattern RGB = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
    public static Pattern RGBA = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
    public static Pattern RGBA_S = Pattern.compile("(([rgbaRGBA]){1,4})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

    public static Pattern HSL = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
    public static Pattern HSLA = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
    public static Pattern HSLA_S = Pattern.compile("(([hslaHSLA]){1,4})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

    public static Pattern CYMK = Pattern.compile("(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))([.,:;|! ]+(([0-9]){1,3}))");
    public static Pattern CYMK_S = Pattern.compile("(([cmykCMYK]){1,4})[(.,:;|! ]+?(([0-9]){1,3})([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?([.,:;|! ]+(([0-9]){1,3}))?");

    public static Pattern MFC = Pattern.compile("([§&]x)?([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])([§&])?([0-9a-fA-F])");
    public static Pattern MFC_AND = Pattern.compile("&([0-9a-fA-Frlmnokx])");
    public static Pattern CHAT_HEX = Pattern.compile("#([a-fA-F0-9]|[a-fA-F0-9]{2}|[a-fA-F0-9]{3}|[a-fA-F0-9]{6});");
    public static Pattern CHAT_RGB = Pattern.compile("([rgbRGB]{1,3})\\(?([0-9]{1,3})(?:,([0-9]{1,3}))?(?:,([0-9]{1,3}))?\\)?;");
    public static Pattern CHAT_HSL = Pattern.compile("(([hslHSL]){1,3})\\(?(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?\\)?;");
    public static Pattern CHAT_CYMK = Pattern.compile("(([cmykCMYK]){1,4})\\(?(([0-9]){1,3})(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?(,(([0-9]){1,3}))?\\)?;");

    public static Pattern ANSI_RGB = Pattern.compile("(([0-9]){1,3})([.,:;|! ](([0-9]){1,3}))([.,:;|! ](([0-9]){1,3}))");

  }

  private static double hue2rgb(double p, double q, double t) {
    if (t < 0) t += 1;
    if (t > 1) t -= 1;
    if (t < 1 / 6.0) return p + (q - p) * 6 * t;
    if (t < 1 / 2.0) return q;
    if (t < 2 / 3.0) return p + (q - p) * (2 / 3.0 - t) * 6;
    return p;
  }



  // Direct conversion
  // HEX => ?
  public static String hex2hex(String hex) {
    return new Color(Type.HEX, hex).getHexString();
  }
  public static String hex2hexa(String hex) {
    return new Color(Type.HEX, hex).getHexStringWithAlpha();
  }
  public static String hex2rgb(String hex) {
    return new Color(Type.HEX, hex).getRGBString();
  }
  public static String hex2rgba(String hex) {
    return new Color(Type.HEX, hex).getRGBAString();
  }
  public static String hex2hsl(String hex) {
    return new Color(Type.HEX, hex).getHSLString();
  }
  public static String hex2hsla(String hex) {
    return new Color(Type.HEX, hex).getHSLAString();
  }
  public static String hex2ansi(String hex) {
    return new Color(Type.HEX, hex).getANSIEscape();
  }
  public static String hex2mfc(String hex) {
    return new Color(Type.HEX, hex).getMFC();
  }
  // RGB => ?
  public static String rgb2rgb(String rgb) {
    return new Color(Type.RGB, rgb).getRGBString();
  }
  public static String rgb2rgba(String rgb) {
    return new Color(Type.RGB, rgb).getRGBAString();
  }
  public static String rgb2hex(String rgb) {
    return new Color(Type.RGB, rgb).getHexString();
  }
  public static String rgb2hexa(String rgb) {
    return new Color(Type.RGB, rgb).getHexStringWithAlpha();
  }
  public static String rgb2hsl(String rgb) {
    return new Color(Type.RGB, rgb).getHSLString();
  }
  public static String rgb2hsla(String rgb) {
    return new Color(Type.RGB, rgb).getHSLAString();
  }
  public static String rgb2ansi(String rgb) {
    return new Color(Type.RGB, rgb).getANSIEscape();
  }
  public static String rgb2mfc(String rgb) {
    return new Color(Type.RGB, rgb).getMFC();
  }



  public static String getGradientString(String s) {
    Pattern p = Pattern.compile("gradient\\[([#&§0-9a-fA-FrgbRGB,;]+)](.*);");
    Matcher m = p.matcher(s);
    ArrayList<Matcher> matches = new ArrayList<>();
    while (m.find()) {
      String[] colorStrings = m.group(1).split(";");
      String content = m.group(2);
      List<Color> colors = new ArrayList<>();
      for (String colorString : colorStrings) {
        if (colorString.startsWith("#")) {
          colors.add(new Color(Type.HEX, colorString));
        }
        else if (colorString.startsWith("&") || colorString.startsWith("§")) {
          colors.add(new Color(Type.MFC, colorString));
        }
        else if (colorString.contains(",")) {
          colors.add(new Color(Type.RGB, colorString));
        }
      }
      s = s.replace(m.group(0), getGradientString(colors, content));
    }
    return s;
  }

  public static String getGradientString(List<Color> colors, String string) {
    StringBuilder master = new StringBuilder();
    String cleanString = string.replaceAll("§[0-9a-fA-FrRkKxXmMnNLloO]", "");
    int length = cleanString.length();
    int blockSize = 0;
    int left = 0;
    if (colors.size() == 0) {
      return string;
    }
    else if (colors.size() == 1) {
      colors.add(colors.get(0));
    }
    if (colors.size() <= cleanString.length()) {
      blockSize = length / (colors.size() - 1);
      left = length % (colors.size() - 1);
    }
    else {
      return string;
    }
    int pos = 0;
    boolean bold = false;
    boolean italic = false;
    boolean strike = false;
    boolean underline = false;
    boolean random = false;
    for (int n = 0; n < colors.size() - 1; n++) {
      int gradientLength = 0;
      if (n == colors.size() - 2) {
        gradientLength = blockSize + left;
        blockSize = gradientLength;
      }
      else {
        gradientLength = blockSize;
      }
      List<Color> gradient = getGradient(colors.get(n), colors.get(n + 1), gradientLength + 1);
      for (int m = 0; m < blockSize; m++) {
        char c = cleanString.charAt(pos);
        char s = string.charAt(pos);
        while (s != c) {
          if (s == '§') {
            switch (string.charAt(pos + 1)) {
              case 'r' -> {
                bold = false;
                italic = false;
                strike = false;
                underline = false;
                random = false;
              }
              case 'l' -> {
                bold = true;
              }
              case 'm' -> {
                strike = true;
              }
              case 'n' -> {
                underline = true;
              }
              case 'o' -> {
                italic = true;
              }
              case 'k' -> {
                random = true;
              }
            }
            string = string.replaceFirst("§" + string.charAt(pos + 1), "");
            s = string.charAt(pos);
          }
          else {
            s = c;
          }
        }
        Color color = gradient.get(m);
        master.append(color.getMFC());
        if (bold) {
          master.append("§l");
        }
        if (strike) {
          master.append("§m");
        }
        if (underline) {
          master.append("§n");
        }
        if (italic) {
          master.append("§o");
        }
        if (random) {
          master.append("§k");
        }
        master.append(cleanString.charAt(pos));
        pos++;
      }
    }
    return master.toString();
  }

  public static List<Color> getGradient(Color startColor, Color endColor, int length) {
    int rd = endColor.getRed() - startColor.getRed();
    int gd = endColor.getGreen() - startColor.getGreen();
    int bd = endColor.getBlue() - startColor.getBlue();
    List<Color> colors = new ArrayList<>();
    for (int n = 0; n < length; n++) {
      int r = (int) (startColor.getRed() + Math.round(rd * (n / ((length - 1) * 1.0))));
      int g = (int) (startColor.getGreen() + Math.round(gd * (n / ((length - 1) * 1.0))));
      int b = (int) (startColor.getBlue() + Math.round(bd * (n / ((length - 1) * 1.0))));
      Color color = new Color(Type.RGB, r + "," + g + "," + b);
      colors.add(color);
    }
    return colors;
  }

  public static String and2mfc(@NotNull String s) {
    return s.replaceAll("&([0-9a-fA-Frlmnokx])", "§$1");
  }

  public static String chatHEX2MFC(@NotNull String s) {
    Matcher m = ColorPattern.CHAT_HEX.matcher(s);
    while (m.find()) {
      s = s.replace(m.group(0), hex2mfc(m.group(0)));
    }
    return s;
  }

  public static String chatRGB2MFC(@NotNull String s) {
    Matcher m = ColorPattern.CHAT_RGB.matcher(s);
    while (m.find()) {
      s = s.replace(m.group(0), rgb2mfc(m.group(0)));
    }
    return s;
  }

  public static String chatHSL2MFC(@NotNull String s) {
    Matcher m = ColorPattern.CHAT_HSL.matcher(s);
    while (m.find()) {
      s = s.replace(m.group(0), new Color(Type.HSL, m.group(0)).getMFC());
    }
    return s;
  }

  public static String chatCMYK2MFC(@NotNull String s) {
    Matcher m = ColorPattern.CHAT_CYMK.matcher(s);
    while (m.find()) {
      s = s.replace(m.group(0), new Color(Type.CMYK, m.group(0)).getMFC());
    }
    return s;
  }

  public static String mfc2and(@NotNull String s) {
    return s.replaceAll("§", "&");
  }

  public static String mfc2ansi(@NotNull String s) {
    Pattern p = Pattern.compile("§x(§[0-9a-fA-F]){6}");
    Matcher m = p.matcher(s);
    ArrayList<String> matches = new ArrayList<>();
    while (m.find()) {
      matches.add(m.group(0));
    }
    for (String find : matches) {
      Color color = new Color(Type.MFC, find);
      s = s.replace(find, color.getANSIEscape());
    }

    s = s.replaceAll("§0", "\u001b[30m");
    s = s.replaceAll("§4", "\u001b[31m");
    s = s.replaceAll("§2", "\u001b[32m");
    s = s.replaceAll("§6", "\u001b[33m");
    s = s.replaceAll("§1", "\u001b[34m");
    s = s.replaceAll("§5", "\u001b[35m");
    s = s.replaceAll("§3", "\u001b[36m");
    s = s.replaceAll("§7", "\u001b[37m");
    s = s.replaceAll("§8", "\u001b[90m");
    s = s.replaceAll("§c", "\u001b[91m");
    s = s.replaceAll("§a", "\u001b[92m");
    s = s.replaceAll("§e", "\u001b[93m");
    s = s.replaceAll("§9", "\u001b[94m");
    s = s.replaceAll("§d", "\u001b[95m");
    s = s.replaceAll("§b", "\u001b[96m");
    s = s.replaceAll("§f", "\u001b[97m");

    s = s.replaceAll("§l", "\u001b[1m");
    s = s.replaceAll("§m", "\u001b[2m");
    s = s.replaceAll("§n", "\u001b[4m");
    s = s.replaceAll("§o", "\u001b[3m");
    s = s.replaceAll("§r", "\u001b[0m");
    s = s.replaceAll("§k", "\u001b[5m");

    return s;
  }

  public static String chatEffect(@NotNull String string) {
    string = Color.and2mfc(string);
    string = Color.chatHEX2MFC(string);
    string = Color.chatRGB2MFC(string);
    string = Color.chatHSL2MFC(string);
    string = Color.chatCMYK2MFC(string);
    return string;
  }

}
