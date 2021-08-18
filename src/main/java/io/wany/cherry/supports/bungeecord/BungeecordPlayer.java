package io.wany.cherry.supports.bungeecord;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BungeecordPlayer {

  private String name;
  private UUID uuid;
  private BungeecordServer server;

  public BungeecordPlayer(String name, UUID uuid, BungeecordServer server) {
    this.name = name;
    this.uuid = uuid;
    this.server = server;
  }

  public String name() {
    return this.name;
  }

  public UUID uuid() {
    return this.uuid;
  }

  public BungeecordServer server() {
    return this.server;
  }

  public void server(BungeecordServer server) {
    this.server = server;
  }

  public boolean here() {
    return Bukkit.getPlayer(this.uuid) != null;
  }

  public Player player() {
    return Bukkit.getPlayer(this.uuid);
  }

  public OfflinePlayer offlinePlayer() {
    return Bukkit.getOfflinePlayer(this.uuid);
  }

}
