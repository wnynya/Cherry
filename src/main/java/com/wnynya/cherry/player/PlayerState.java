package com.wnynya.cherry.player;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerState {

  private static HashMap<Player, PlayerState> playerStates = new HashMap<>();

  private boolean cancelTabComplete = false;

  public PlayerState(Player player) {
    playerStates.put(player, this);
  }

  public void setCancelTabComplete(boolean cancelTabComplete) {
    this.cancelTabComplete = cancelTabComplete;
  }

  public boolean isCancelTabComplete() {
    return cancelTabComplete;
  }

  public static PlayerState getPlayerState(Player player) {
    if (playerStates.containsKey(player)) {
      return playerStates.get(player);
    }
    else {
      return new PlayerState(player);
    }
  }

}
