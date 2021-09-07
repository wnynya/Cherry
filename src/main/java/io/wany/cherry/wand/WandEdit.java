package io.wany.cherry.wand;

import io.wany.cherry.Message;
import io.wany.cherry.wand.area.Area;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class WandEdit {

  private final Wand wand;
  private final HashMap<Integer, Location> positions = new HashMap<>();

  private Timer particleTimer;
  public List<Location> particleArea = new ArrayList<>();

  public WandEdit(Wand wand) {
    this.wand = wand;
  }

  public void setPosition(int n, Location pos) {
    if (this.getPosition(n) == null || !this.getPosition(n).equals(pos)) {
      this.positions.put(n, pos);
    }
  }

  public Location getPosition(int n) {
    return this.positions.get(n);
  }

  public void setParticleArea(List<Location> particleArea) {
    this.particleArea = particleArea;
  }

  public void startParticleArea() {
    this.particleTimer = new Timer();
    this.particleTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        for (Location loc : particleArea) {
          wand.getPlayer().spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 120, 45), 1));
        }
      }
    }, 0, 200);
  }

  public void stopParticleArea() {
    this.particleTimer.cancel();
  }

  public static void onPlayerJoin(PlayerJoinEvent event) {

    if (!Wand.ENABLED) {
      return;
    }

    Player player = event.getPlayer();
    Wand wand = Wand.getWand(player);
    WandEdit edit = wand.getEdit();
    edit.startParticleArea();

  }

  public static void onPlayerQuit(PlayerQuitEvent event) {

    if (!Wand.ENABLED) {
      return;
    }

    Player player = event.getPlayer();
    Wand wand = Wand.getWand(player);
    WandEdit edit = wand.getEdit();
    edit.stopParticleArea();

  }

  public static void onPlayerInteract(PlayerInteractEvent event) {

    if (!Wand.ENABLED) {
      return;
    }

    Player player = event.getPlayer();

    if (!player.hasPermission("cherry.wand.edit.wand")) {
      return;
    }
    if (event.getItem() == null || !event.getItem().getType().equals(Material.SWEET_BERRIES)) {
      return;
    }
    if (player.isSneaking()) {
      return;
    }

    event.setCancelled(true);

    Wand wand = Wand.getWand(player);
    WandEdit edit = wand.getEdit();

    switch (event.getAction()) {

      case LEFT_CLICK_BLOCK -> {
        Block block = player.getTargetBlockExact(10);
        if (block == null) {
          return;
        }
        Location location = block.getLocation();
        edit.setPosition(1, location);
        String message = "첫번째 포지션이 설정되었습니다. (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";
        if (edit.getPosition(1) != null && edit.getPosition(2) != null) {
          int count = Area.CUBE.getArea(edit.getPosition(1), edit.getPosition(2)).size();
          message += " (" + Wand.COLOR + count + "&r블록)";
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(2)));
        }
        else {
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(1)));
        }
        Message.info(player, Wand.PREFIX + Message.effect(message));
      }

      case RIGHT_CLICK_BLOCK -> {
        Block block = player.getTargetBlockExact(10);
        if (block == null) {
          return;
        }
        Location location = block.getLocation();
        edit.setPosition(2, location);
        String message = "두번째 포지션이 설정되었습니다. (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";
        if (edit.getPosition(1) != null && edit.getPosition(2) != null) {
          int count = Area.CUBE.getArea(edit.getPosition(1), edit.getPosition(2)).size();
          message += " (" + Wand.COLOR + count + "&r블록)";
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(2)));
        }
        else {
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(2), edit.getPosition(2)));
        }
        Message.info(player, Wand.PREFIX + Message.effect(message));
      }

      case LEFT_CLICK_AIR -> {
        edit.setPosition(1, null);
        if (edit.getPosition(1) != null || edit.getPosition(2) != null) {
          if (edit.getPosition(1) != null && edit.getPosition(2) != null) {
            edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(2)));
          }
          else {
            edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(2), edit.getPosition(2)));
          }
        }
        else {
          edit.setParticleArea(new ArrayList<>());
        }
      }

      case RIGHT_CLICK_AIR -> {
        edit.setPosition(2, null);
        if (edit.getPosition(1) != null || edit.getPosition(2) != null) {
          if (edit.getPosition(1) != null && edit.getPosition(2) != null) {
            edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(2)));
          }
          else {
            edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(1)));
          }
        }
        else {
          edit.setParticleArea(new ArrayList<>());
        }
      }

    }

  }

}
