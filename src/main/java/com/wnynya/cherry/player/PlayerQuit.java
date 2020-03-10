package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.terminal.WebSocketClient;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerQuit {

  private static ArrayList<Player> canceledPlayers = new ArrayList<>();

  public static void server(PlayerQuitEvent event) {

    Player player = event.getPlayer();

    if (canceledPlayers.contains(player)) {
      event.setQuitMessage(null);
      canceledPlayers.remove(player);
    }
    else {
      if (Cherry.config.getBoolean("event.quit.setMessage.playerChat.enable")) {

        event.setQuitMessage(null);
        String format = Cherry.config.getString("event.quit.setMessage.playerChat.format");
        Msg.allPwO(Msg.playerFormatter(event.getPlayer(), format), player);

      }

      if (Cherry.config.getBoolean("event.quit.setMessage.playerActionbar.enable")) {

        event.setQuitMessage(null);
        String format = Cherry.config.getString("event.quit.setMessage.playerActionbar.format");

        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
          Msg.actionBar(p, Msg.playerFormatter(event.getPlayer(), format));
        }

      }
    }

    if (Cherry.config.getBoolean("event.quit.setMessage.console.enable")) {

      event.setQuitMessage(null);
      String format = Cherry.config.getString("event.quit.setMessage.console.format");
      Msg.info(Msg.playerFormatter(event.getPlayer(), format));

    }

    if (Cherry.config.getBoolean("event.quit.websocket") && Cherry.config.getBoolean("websocket.enable") && WebSocketClient.isConnected) {
      WebSocketClient.Message.quit(player);
    }

  }

  public static void network(Player player) {

    // 퇴장 메시지 (플레이어 채팅)
    if (Cherry.config.getBoolean("event.networkquit.setMessage.playerChat.enable")) {
      String format = Cherry.config.getString("event.networkquit.setMessage.playerChat.format");
      Msg.allPwO(Msg.playerFormatter(player, format), player);
    }

    // 퇴장 메시지 (콘솔)
    if (Cherry.config.getBoolean("event.networkquit.setMessage.console.enable")) {
      String format = Cherry.config.getString("event.networkquit.setMessage.console.format");
      Msg.info(Msg.playerFormatter(player, format));
    }

  }

  public static void switchS(Player player, String fromServer, String gotoServer) {

    // 퇴장 메시지 (플레이어 채팅)
    if (Cherry.config.getBoolean("event.switchquit.setMessage.playerChat.enable")) {
      canceledPlayers.add(player);
      String format = Cherry.config.getString("event.switchquit.setMessage.playerChat.format");
      String msg = Msg.playerFormatter(player, format);
      msg = msg.replace("{fromserver}", fromServer);
      msg = msg.replace("{gotoserver}", gotoServer);
      Msg.allPwO(msg, player);
    }

    // 퇴장 메시지 (콘솔)
    if (Cherry.config.getBoolean("event.switchquit.setMessage.console.enable")) {
      String format = Cherry.config.getString("event.switchquit.setMessage.console.format");
      String msg = Msg.playerFormatter(player, format);
      msg = msg.replace("{fromserver}", fromServer);
      msg = msg.replace("{gotoserver}", gotoServer);
      Msg.info(msg);
    }

  }

}
