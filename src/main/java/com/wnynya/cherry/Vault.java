package com.wnynya.cherry;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.Plugin;

public class Vault implements Listener {

  public static boolean exist() {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
    return plugin != null;
  }

  private static Chat vaultChat = null;

  public static void loadVaultChat() {
    if (Vault.exist()) {
      Chat vaultChatClass = Bukkit.getServer().getServicesManager().load(Chat.class);
      Msg.debug("[VAULT] Chat: " + (vaultChatClass == null ? "...없음" : vaultChatClass.getName()));
      vaultChat = vaultChatClass;
    }
  }

  public static Chat getVaultChat() {
    return vaultChat;
  }

  public static String getPrefix(Player player) {
    if (vaultChat == null) {
      return "";
    }
    else {
      return vaultChat.getPlayerPrefix(player);
    }
  }

  public static String getSuffix(Player player) {
    if (vaultChat == null) {
      return "";
    }
    else {
      return vaultChat.getPlayerSuffix(player);
    }
  }

  @EventHandler
  public void onServiceChange(ServiceRegisterEvent event) {
    if (event.getProvider().getService() == Chat.class) {
      loadVaultChat();
    }
  }


  public static void enable() {

    if (!Cherry.config.getBoolean("vault-support.enable")) {
      return;
    }

    if (!Vault.exist()) {
      Msg.debug("[VAULT] Vault-Support option has been enabled, But couldn't find Vault plugin.");
      return;
    }

    Cherry.getPlugin().registerEvent(new Vault());
    Vault.loadVaultChat();

  }

}
