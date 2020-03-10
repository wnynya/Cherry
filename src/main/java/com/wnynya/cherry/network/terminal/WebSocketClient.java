package com.wnynya.cherry.network.terminal;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Tool;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class WebSocketClient {

  public static WebSocket ws;
  public static boolean isConnected = false;

  private static URI uri;
  private static String name;

  private ByteArrayOutputStream baos = new ByteArrayOutputStream();

  private static void send(String msg) {
    if (isConnected) {
      ws.sendText(msg, true);
    }
  }

  public static class Message {

    public static void init() {
      JSONObject msg = new JSONObject();
      msg.put("event", "init");
      JSONObject data = new JSONObject();
      data.put("name", name);
      msg.put("data", data);

      send(msg.toJSONString());

      Message.status(Status.ONLINE);
      Message.serverData();
    }

    public static void status(Status status) {
      JSONObject msg = new JSONObject();
      msg.put("event", "status");
      JSONObject data = new JSONObject();
      data.put("status", status.toString());
      msg.put("data", data);
      send(msg.toJSONString());
      Cherry.status = status;
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

      send(msg.toJSONString());
    }

    public static void chat(Player player, String content) {
      if (!Cherry.config.getBoolean("websocket.event.player.chat")) {
        return;
      }
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("PLAYER", "CHAT"));
      data.put("text", player.getName() + ": " + content);
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void join(Player player) {
      if (!Cherry.config.getBoolean("websocket.event.player.join")) {
        return;
      }
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("PLAYER", "JOIN"));
      data.put("text", player.getName());
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void quit(Player player) {
      if (!Cherry.config.getBoolean("websocket.event.player.quit")) {
        return;
      }
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("PLAYER", "QUIT"));
      data.put("text", player.getName());
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void command(Player player, String commandString) {
      if (!Cherry.config.getBoolean("websocket.event.player.command")) {
        return;
      }
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("PLAYER", "COMMAND"));
      data.put("text", player.getName() + "> " + commandString);
      data.put("loc", Tool.loc2StrWithWorld(player.getLocation()));
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void command(Entity entity, String commandString) {
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("ENTITY", "COMMAND"));
      data.put("text", entity.getName() + "> " + commandString);
      data.put("loc", Tool.loc2StrWithWorld(entity.getLocation()));
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void command(CommandBlock block, String commandString) {
      if (!Cherry.config.getBoolean("websocket.event.player.commandblock.command")) {
        return;
      }
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("COMMANDBLOCK", "COMMAND"));
      data.put("text", block.getName() + "> " + commandString);
      data.put("loc", Tool.loc2StrWithWorld(block.getLocation()));
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void command(String sender, String commandString) {
      if (sender.equalsIgnoreCase("CONSOLE") && !Cherry.config.getBoolean("websocket.event.player.console.command")) {
        return;
      }
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList(sender.toUpperCase(), "COMMAND"));
      if (sender.equalsIgnoreCase("web")) {
        sender = "#";
      }
      data.put("text", sender + "> " + commandString);
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void webChat(String content) {
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("WEB", "CHAT"));
      data.put("text", "#: " + content);
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void webCommand(String commandString) {
      JSONObject msg = new JSONObject();
      msg.put("event", "log");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("WEB", "COMMAND"));
      data.put("text", "#> " + commandString);
      msg.put("data", data);
      send(msg.toJSONString());
    }

    public static void output(String string) {
      JSONObject msg = new JSONObject();
      msg.put("event", "console");
      JSONObject data = new JSONObject();
      data.put("tags", Arrays.asList("INFO"));
      data.put("text", string);
      msg.put("data", data);
      send(msg.toJSONString());
    }

  }

  public enum Status {
    ONLINE, OFFLINE, RELOAD, UPDATE
  }

  public static void receive(String msgString) {
    JSONObject msg;

    try {
      JSONParser parser = new JSONParser();
      msg = (JSONObject) parser.parse(msgString);
    }
    catch (Exception e) {
      return;
    }

    if (!msg.containsKey("event")) {
      return;
    }

    switch ((String) msg.get("event")) {

      case "web-msg": {
        JSONObject data = (JSONObject) msg.get("data");
        String text = data.get("text").toString();
        String name = data.get("name").toString();
        String id = data.get("id").toString();
        Bukkit.getServer().getConsoleSender().sendMessage(com.wnynya.cherry.Msg.n2s("[WEB]: " + id + ": " + text));
        com.wnynya.cherry.Msg.allP(com.wnynya.cherry.Msg.n2s("&e&l[WEB]:&r&f " + id + ": " + text));
        Message.webChat((String) data.get("text"));
        break;
      }

      case "web-command": {
        JSONObject data = (JSONObject) msg.get("data");
        String command = (String) data.get("command");
        String name = data.get("name").toString();
        String id = data.get("id").toString();
        Bukkit.getServer().getConsoleSender().sendMessage(com.wnynya.cherry.Msg.n2s("[WEB]: " + id + ": " + command));
        try {
          Bukkit.getScheduler().callSyncMethod(Cherry.getPlugin(), new Callable<Boolean>() {
            @Override
            public Boolean call() {
              Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(1));
              return true;
            }
          }).get();
        }
        catch (Exception ignored) {
        }
        Message.webCommand(command);
        break;
      }

    }

  }

  public static void connect() {
    try {
      CountDownLatch latch = new CountDownLatch(1);

      WebSocket wsc = HttpClient.newHttpClient().newWebSocketBuilder().connectTimeout(Duration.ofSeconds(10)).subprotocols("cwt-server").buildAsync(uri, new WebSocketConnection(latch)).join();

      ws = wsc;

      Message.init();

      latch.await();
    }
    catch (Exception e) {
    }
  }

  private static Timer dataTimer;

  public static void startDataLoop() {
    dataTimer = new Timer();
    dataTimer.schedule(new TimerTask() {
      public void run() {
        if (WebSocketClient.isConnected) {
          Message.serverData();
        }
      }
    }, 0, 1 * 1000);
  }

  public static void cancelDataLoop() {
    if (dataTimer != null) {
      dataTimer.cancel();
    }
  }

  private static Timer connectTimer;

  public static void startConnectLoop() {
    connectTimer = new Timer();
    connectTimer.schedule(new TimerTask() {
      public void run() {
        if (!WebSocketClient.isConnected) {
          connect();
        }
      }
    }, 0, 10 * 1000);
  }

  public static void cancelConnectLoop() {
    if (connectTimer != null) {
      connectTimer.cancel();
    }
  }

  public static void init() throws Exception {
    if (!Cherry.config.isString("websocket.host") || Cherry.config.getString("websocket.host") == null || Cherry.config.getString("websocket.host").replaceAll(" ", "").equals("")) {
      throw new Exception("URL이 설정되지 않았습니다.");
    }
    if (!Cherry.config.isString("websocket.name") || Cherry.config.getString("websocket.name") == null || Cherry.config.getString("websocket.name").replaceAll(" ", "").equals("")) {
      throw new Exception("서버 이름이 설정되지 않았습니다.");
    }
    try {
      uri = new URI(Cherry.config.getString("websocket.host"));
      name = Cherry.config.getString("websocket.name");
    }
    catch (Exception e) {
      throw e;
    }

    startConnectLoop();
  }

  public static void enable() {
    if (Cherry.config.getBoolean("websocket.enable")) {
      try {
        WebSocketClient.init();
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new CherryLoggerFilter());
    }
  }

  public static void disable() {
    if (Cherry.config.getBoolean("websocket.enable")) {
      if (WebSocketClient.isConnected) {
        if (Cherry.status.equals(WebSocketClient.Status.RELOAD) || Cherry.status.equals(WebSocketClient.Status.UPDATE)) {
          Message.status(Cherry.status);
        }
        else {
          Message.status(WebSocketClient.Status.OFFLINE);
        }
        WebSocketClient.ws.abort();
        WebSocketClient.cancelDataLoop();
      }
      else {
        WebSocketClient.cancelConnectLoop();
      }
    }
  }

}
