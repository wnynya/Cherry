package io.wany.cherry.supports.bungeecord;

import java.util.List;

public class BungeecordServer {

  private final String name;
  private Status status;
  private List<BungeecordPlayer> players;

  public BungeecordServer(String name, Status status, List<BungeecordPlayer> players) {
    this.name = name;
    this.status = status;
    this.players = players;
  }

  public String name() {
    return this.name;
  }

  public Status status() {
    return this.status;
  }

  public void status(Status status) {
    this.status = status;
  }

  public List<BungeecordPlayer> players() {
    return this.players;
  }

  public void plauers(List<BungeecordPlayer> players) {
    this.players = players;
  }

  public enum Status {
    ONLINE,
    OFFLINE,
    UNKNOWN,
  }

}
