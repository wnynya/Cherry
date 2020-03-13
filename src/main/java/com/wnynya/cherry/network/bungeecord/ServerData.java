package com.wnynya.cherry.network.bungeecord;

import java.util.HashMap;

public class ServerData {

  private static HashMap<String, ServerData> serverDatas = new HashMap<>();

  private String name;
  private boolean isOnline;
  private int cp;
  private int mp;

  private ServerData(String name) {
    this.name = name;
    this.isOnline = false;
    this.cp = 0;
    this.mp = 0;
  }

  public String getName() {
    return this.name;
  }

  public void setOnline(boolean b) {
    this.isOnline = b;
  }

  public boolean isOnline() {
    return isOnline;
  }

  public void setCurrentPlayers(int i) {
    this.cp = i;
  }

  public int getCurrentPlayers() {
    return cp;
  }

  public void setMaxPlayers(int i) {
    this.mp = i;
  }

  public int getMaxPlayers() {
    return mp;
  }

  public static ServerData getServerData(String name) {
    if (serverDatas.containsKey(name)) {
      return serverDatas.get(name);
    }
    else {
      ServerData s = new ServerData(name);
      serverDatas.put(name, s);
      return s;
    }
  }

}
