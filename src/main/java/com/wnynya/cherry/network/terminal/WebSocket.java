package com.wnynya.cherry.network.terminal;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocket {

  public static java.net.http.WebSocket ws;
  public static boolean isConnected = false;

  public static void send(String msg) {
    if (msg == null) {
      return;
    }
    if (isConnected) {
      ws.sendText(msg, true);
    }
  }

  /*public static class Message {

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

  }*/

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
        if (Cherry.config.getBoolean("terminal.log.console.msg.enable")) {
          Bukkit.getServer().getConsoleSender().sendMessage(com.wnynya.cherry.Msg.n2s("[WEB]: " + id + ": " + text));
        }
        if (Cherry.config.getBoolean("terminal.log.player.msg.enable")) {
          com.wnynya.cherry.Msg.allP(com.wnynya.cherry.Msg.n2s("&e&l[WEB]:&r&f " + id + ": " + text));
        }
        //Message.webChat((String) data.get("text"));
        break;
      }

      case "web-command": {
        JSONObject data = (JSONObject) msg.get("data");
        String command = (String) data.get("command");
        String name = data.get("name").toString();
        String id = data.get("id").toString();
        if (Cherry.config.getBoolean("terminal.log.console.command.enable")) {
          Bukkit.getServer().getConsoleSender().sendMessage(com.wnynya.cherry.Msg.n2s("[WEB]: " + id + ": " + command));
        }
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
        //Message.webCommand(command);
        break;
      }

    }

  }

  public static void connect() {
    try {
      Msg.debug("[CWS] Try connect to CWS server");
      CountDownLatch latch = new CountDownLatch(1);

      ws = HttpClient.newHttpClient()
        .newWebSocketBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .subprotocols("cwt-server")
        .buildAsync(Terminal.uri, new WebSocketConnection(latch))
        .join();

      Terminal.Msg.init();

      //latch.await();
    }
    catch (Exception e) {
      Msg.info("[CWT] Connect Failed.");
    }
  }

  private static ExecutorService connectorExecutorService = Executors.newFixedThreadPool(1);
  private static Timer connectTimer;
  public static void connectLoop() {
    connectorExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        connectTimer = new Timer();
        connectTimer.schedule(new TimerTask() {
          public void run() {
            if (!Terminal.connected) {
              connect();
            }
          }
        }, 0, 2 * 1000);
      }
    });
  }

  public static void enable() {

    try {
      connectLoop();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new CherryLoggerFilter());

  }

  public static void disable() {
    if (WebSocket.isConnected) {
      if (Cherry.status.equals(Terminal.Status.RELOAD) || Cherry.status.equals(Terminal.Status.UPDATE)) {
        Terminal.Msg.serverStatus(Cherry.status);
      }
      else {
        Terminal.Msg.serverStatus(Terminal.Status.OFFLINE);
      }
      WebSocket.ws.abort();
    }

    connectTimer.cancel();
    connectorExecutorService.shutdownNow();
  }

}
