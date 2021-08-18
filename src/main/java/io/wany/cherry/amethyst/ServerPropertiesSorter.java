package io.wany.cherry.amethyst;

import io.wany.cherry.Cherry;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ServerPropertiesSorter {

  public static File file;
  public static HashMap<String, String> propertiesMap = new HashMap<>();
  public static List<String> propertiesKeys = new ArrayList<>();

  public static void load() {
    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream(file));
      propertiesKeys = new ArrayList<>(properties.stringPropertyNames());
      Collections.sort(propertiesKeys);
      for (String key : propertiesKeys) {
        String value = properties.getProperty(key);
        propertiesMap.put(key, value);
      }
      properties.clear();
      properties.store(new FileOutputStream(file), "Minecraft server properties");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void save() {
    try {
      String content = Files.readString(file.toPath());
      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      writer.append(content);
      for (String key : propertiesKeys) {
        String value = propertiesMap.get(key);
        writer.append(key).append("=").append(value).append("\n");
      }
      writer.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void onEnable() {
    file = new File(Cherry.SERVER_DIR + "/server.properties");
    load();
    save();
  }

}
