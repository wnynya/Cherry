package io.wany.cherry.amethyst.troll;

import io.wany.cherry.Console;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public enum Troll implements Trolling {

  RAMDOM {
    @Override
    public void troll(@NotNull Player player, String[] args) {

    }
  },

  NULL {
    @Override
    public void troll(@NotNull Player player, String[] args) {

    }
  },

  VELOCITY {
    @Override
    public void troll(@NotNull Player player, String[] args) {
      Vector randomVector = new Vector(Math.random() * 10 - 5, Math.random() * 10 - 5, Math.random() * 10 - 5);
      player.setVelocity(randomVector);
    }
  },

  RAMDOM_VELOCITY {
    @Override
    public void troll(@NotNull Player player, String[] args) {
      Vector randomVector = new Vector(Math.random() * 10 - 5, Math.random() * 10 - 5, Math.random() * 10 - 5);
      player.setVelocity(randomVector);
    }
  }

}
