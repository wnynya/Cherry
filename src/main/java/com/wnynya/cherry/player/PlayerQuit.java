package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.terminal.WebSocketClient;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit {

  public static void server(PlayerQuitEvent event) {

    Player player = event.getPlayer();

    if (Cherry.config.getBoolean("event.quit.setMessage.playerChat.enable")) {

      event.setQuitMessage(null);
      String format = Cherry.config.getString("event.quit.setMessage.playerChat.format");
      Msg.allP(Msg.playerFormatter(event.getPlayer(), format));

    }

    if (Cherry.config.getBoolean("event.quit.setMessage.playerActionbar.enable")) {

      event.setQuitMessage(null);
      String format = Cherry.config.getString("event.quit.setMessage.playerActionbar.format");

      for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
        Msg.actionBar(p, Msg.playerFormatter(event.getPlayer(), format));
      }

    }

    if (Cherry.config.getBoolean("event.quit.setMessage.console.enable")) {

      event.setQuitMessage(null);
      String format = Cherry.config.getString("event.quit.setMessage.console.format");
      Msg.allP(Msg.playerFormatter(event.getPlayer(), format));

    }

    if (Cherry.config.getBoolean("event.quit.websocket") &&
      Cherry.config.getBoolean("websocket.enable") && WebSocketClient.isConnected ) {
      WebSocketClient.Message.quit(player);
    }

  }

}
