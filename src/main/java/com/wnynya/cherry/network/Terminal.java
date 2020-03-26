package com.wnynya.cherry.network;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.network.terminal.WebSocket;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Terminal {

  private static String name;
  public static URI uri;

  public enum Status {
    ONLINE, OFFLINE, RELOAD, UPDATE
  }

  public static boolean connected = false;

  public static class Msg {

    public static void send(JSONObject obj) {
      if (!connected) {
        return;
      }
      WebSocket.send(obj.toJSONString());
    }

    public static void send(String str) {
      if (!connected) {
        return;
      }
      WebSocket.send(str);
    }

    public static void init() {
      JSONObject msg = new JSONObject();
      msg.put("event", "init");
      JSONObject data = new JSONObject();
      data.put("name", name);
      msg.put("data", data);

      Msg.send(msg);

      Msg.serverStatus(Status.ONLINE);
      Msg.serverData();
    }

    public static void log(String string) {
      JSONObject msg = new JSONObject();
      msg.put("event", "console");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("INFO"));
      data.put("text", string);
      msg.put("data", data);
      send(msg);
    }

    public static void serverData() {
      Runtime r = Runtime.getRuntime();
      com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

      JSONObject msg = new JSONObject();
      msg.put("event", "system-info");

      JSONObject data = new JSONObject();
      data.put("freeMemory", r.freeMemory());
      data.put("maxMemory", r.maxMemory());
      data.put("totalMemory", r.totalMemory());
      data.put("cpuUsage", osb.getSystemCpuLoad());
      data.put("cpuUsageServer", osb.getProcessCpuLoad());
      data.put("tps", Bukkit.getServer().getTPS()[0]);
      data.put("players", Tool.List.playerNames());
      data.put("maxPlayers", Bukkit.getServer().getMaxPlayers());

      msg.put("data", data);

      send(msg);
    }

    public static void serverStatus(Status status) {
      JSONObject msg = new JSONObject();
      msg.put("event", "status");
      JSONObject data = new JSONObject();
      data.put("status", status.toString());
      msg.put("data", data);
      send(msg);
      Cherry.status = status;
    }

  }

  private static ExecutorService dataExecutorService = Executors.newFixedThreadPool(1);
  private static Timer dataTimer;
  public static void dataLoop() {
    dataExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        dataTimer = new Timer();
        dataTimer.schedule(new TimerTask() {
          public void run() {
            if (Terminal.connected) {
              Terminal.Msg.serverData();
            }
          }
        }, 0, 1000);
      }
    });
  }

  public static void init() throws Exception {
    if (!Cherry.config.isString("terminal.host") || Cherry.config.getString("terminal.host") == null || Cherry.config.getString("terminal.host").replaceAll(" ", "").equals("")) {
      throw new Exception("URL이 설정되지 않았습니다.");
    }
    if (!Cherry.config.isString("terminal.name") || Cherry.config.getString("terminal.name") == null || Cherry.config.getString("terminal.name").replaceAll(" ", "").equals("")) {
      throw new Exception("서버 이름이 설정되지 않았습니다.");
    }
    try {
      uri = new URI(Cherry.config.getString("terminal.host"));
      name = Cherry.config.getString("terminal.name");
      dataLoop();
    }
    catch (Exception e) {
      throw e;
    }
  }

  public static void enable() {

    if (Cherry.javaVersion < 11) {
      com.wnynya.cherry.Msg.debug("[CWT] CWT requires Java 11 or higher version");
      com.wnynya.cherry.Msg.debug("[CWT] CWT Disabled");
      return;
    }

    if (!Cherry.config.getBoolean("terminal.enable")) {
      com.wnynya.cherry.Msg.debug("[CWT] CWT Disabled");
      return;
    }

    com.wnynya.cherry.Msg.debug("[CWT] Enabling CWT v0.1");

    try {
      init();
    }
    catch (Exception e) {
      com.wnynya.cherry.Msg.debug("[CWT] ERROR: " + e.getMessage());
      return;
    }

    WebSocket.enable();

  }

  public static void disable() {

    if (Cherry.javaVersion < 11) {
      return;
    }

    if (!Cherry.config.getBoolean("terminal.enable")) {
      return;
    }

    dataTimer.cancel();
    dataExecutorService.shutdownNow();

    WebSocket.disable();
  }

}
