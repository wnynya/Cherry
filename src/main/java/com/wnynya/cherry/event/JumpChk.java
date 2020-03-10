package com.wnynya.cherry.event;

import com.wnynya.cherry.Msg;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class JumpChk implements Listener {

  @EventHandler
  public void onFlightAttempt(PlayerToggleFlightEvent event) {

    Msg.info("j");

    if (!event.getPlayer().isFlying() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {

      event.setCancelled(true);

      event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(0, 1, 0)));

      Msg.info(event.getPlayer(), "kump!");

    }

  }

  @EventHandler
  public void onaaa(PlayerMoveEvent event) {

    if (event.getPlayer().isOnGround()) {
      Msg.info(event.getPlayer(), "땅 위에 있음 => 플라이 끔");
      event.getPlayer().setAllowFlight(false);
    }

    if (!event.getPlayer().isOnGround()) {
      Msg.info(event.getPlayer(), "날고 있음 => 플라이 켬");
      event.getPlayer().setAllowFlight(true);
    }

  }

}