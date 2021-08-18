package io.wany.cherry.amethyst;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tool {

  public static UUID intArray2UUID (int[] intArray) {
    int[] s = new int[]{0, 0, 0, 0};
    System.arraycopy(intArray, 0, s, 0, Math.min(intArray.length, s.length));
    StringBuilder sb = new StringBuilder();
    for (var i = 0; i < 4; i++) {
      int a = s[i];
      sb.append(Long.toHexString(4294967296L + a));
    }
    return UUID.fromString(sb.toString().replaceAll("([0-9a-f]{8})([0-9a-f]{4})([0-9a-f]{4})([0-9a-f]{4})([0-9a-f]{12})", "$1-$2-$3-$4-$5"));
  }

  public static Object booleanObject(boolean bool, Object trueObject, Object falseObject) {
    if (bool) {
      return trueObject;
    }
    else {
      return falseObject;
    }
  }

  public static ItemStack booleanItemStack(boolean bool, ItemStack trueObject, ItemStack falseObject) {
    if (bool) {
      return trueObject;
    }
    else {
      return falseObject;
    }
  }

  public static class Lista {
    public static java.util.List<String> materials() {
      java.util.List<String> list = new ArrayList<>();
      for (Material material : Material.values()) {
        list.add(material.toString().toLowerCase());
      }
      return list;
    }

    public static java.util.List<String> materialBlocks() {
      java.util.List<String> list = new ArrayList<>();
      for (Material material : Material.values()) {
        if (material.isBlock()) {
          list.add(material.toString().toLowerCase());
        }
      }
      return list;
    }

    public static java.util.List<String> materialItems() {
      java.util.List<String> list = new ArrayList<>();
      for (Material material : Material.values()) {
        if (material.isItem()) {
          list.add(material.toString());
        }
      }
      return list;
    }

    public static java.util.List<String> playerNames() {
      java.util.List<String> list = new ArrayList<>();
      for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
        list.add(player.getName());
      }
      return list;
    }

    public static java.util.List<String> playerNames(String prefix) {
      java.util.List<String> list = new ArrayList<>();
      for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
        list.add(prefix + player.getName());
      }
      return list;
    }

    public static java.util.List<UUID> playerUUIDs() {
      java.util.List<UUID> list = new ArrayList<>();
      for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
        list.add(player.getUniqueId());
      }
      return list;
    }

    public static java.util.List<String> playerUUIDStrings() {
      java.util.List<String> list = new ArrayList<>();
      for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
        list.add(player.getUniqueId().toString());
      }
      return list;
    }

    public static java.util.List<String> playerUUIDStrings(String prefix) {
      java.util.List<String> list = new ArrayList<>();
      for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
        list.add(prefix + player.getUniqueId());
      }
      return list;
    }

    public static java.util.List<String> worldNames() {
      java.util.List<String> list = new ArrayList<>();
      for (World world : Bukkit.getWorlds()) {
        list.add(world.getName());
      }
      return list;
    }

  }

}
