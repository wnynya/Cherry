package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit {

  public static void server(PlayerQuitEvent event) {

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

  }

}
