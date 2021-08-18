package io.wany.cherry.supports.vault;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class VaultChat {

  public static String getPrefix(Player player) {
    if (VaultSupport.CHAT == null) {
      return "";
    }
    else {
      return VaultSupport.CHAT.getPlayerPrefix(player);
    }
  }

  public static String getSuffix(Player player) {
    if (VaultSupport.CHAT == null) {
      return "";
    }
    else {
      return VaultSupport.CHAT.getPlayerSuffix(player);
    }
  }

  public static String getPrefix(World world, OfflinePlayer player) {
    if (VaultSupport.CHAT == null) {
      return "";
    }
    else {
      return VaultSupport.CHAT.getPlayerPrefix(world.getName(), player);
    }
  }

  public static String getSuffix(World world, OfflinePlayer player) {
    if (VaultSupport.CHAT == null) {
      return "";
    }
    else {
      return VaultSupport.CHAT.getPlayerSuffix(world.getName(), player);
    }
  }

}
