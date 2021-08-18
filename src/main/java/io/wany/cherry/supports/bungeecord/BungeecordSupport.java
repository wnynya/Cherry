package io.wany.cherry.supports.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BungeecordSupport {

  public static String COLOR = "#FFD24B;";
  public static String PREFIX = COLOR + "&l[Bungeecord]:&r ";
  public static boolean EXIST = false;

  protected static String CHANNEL;

  public static void onEnable() {
    if (!Cherry.CONFIG.getBoolean("bungeecord-support.enable")) {
      Console.debug(PREFIX + "Bungeecord-Support Disabled");
      return;
    }
    Console.debug(PREFIX + "Enabling Bungeecord-Support");
    EXIST = true;

    CHANNEL = Cherry.CONFIG.getString("bungeecord-support.channel");

    // BungeeCord Messaging Channel
    Cherry.PLUGIN.getServer().getMessenger().registerOutgoingPluginChannel(Cherry.PLUGIN, "BungeeCord");

    // Cherry Messaging Channel
    Cherry.PLUGIN.getServer().getMessenger().registerOutgoingPluginChannel(Cherry.PLUGIN, CHANNEL);
    Cherry.PLUGIN.getServer().getMessenger().registerIncomingPluginChannel(Cherry.PLUGIN, CHANNEL, new BungeecordMessageListener());

  }

  public static void send(ByteArrayDataOutput message) {
    if (!EXIST) {
      return;
    }
    List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
    if (players.size() == 0) {
      return;
    }
    Player player = players.get(0);
    player.sendPluginMessage(Cherry.PLUGIN, CHANNEL, message.toByteArray());
  }

}
