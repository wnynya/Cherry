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

  public static final String prefix = Msg.effect("#E8CF8B;&l[Vault]: &r");

  private static Chat vaultChat = null;

  public static void loadVaultChat() {
    if (Vault.exist()) {
      Chat vaultChatClass = Bukkit.getServer().getServicesManager().load(Chat.class);
      Msg.debug(Vault.prefix + "Chat: " + (vaultChatClass == null ? "...없음" : vaultChatClass.getName()));
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
      Msg.debug(Vault.prefix + "Vault-Support option has been enabled, But couldn't find Vault plugin.");
      return;
    }

    Cherry.plugin.registerEvent(new Vault());
    Vault.loadVaultChat();

  }

}
