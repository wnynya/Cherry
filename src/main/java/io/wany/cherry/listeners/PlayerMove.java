package io.wany.cherry.listeners;

import io.wany.cherry.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPlayerMove(PlayerMoveEvent event) {

    //speedometer(event);

  }

  public static void speedometer(PlayerMoveEvent event) {

    Player player = event.getPlayer();
    double distance = event.getFrom().distance(event.getTo());
    player.sendActionBar(Message.parse(distance * 20 + ""));

  }

}
