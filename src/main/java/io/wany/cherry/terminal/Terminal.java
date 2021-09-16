package io.wany.cherry.terminal;

import io.wany.cherry.Cherry;
import io.wany.cherry.Config;
import io.wany.cherry.Console;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Terminal {

  public static String NAME = "Terminal";
  public static String COLORHEX = "#00FFC8";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[" + NAME + "]:&r ";

  //public static boolean ENABLE = true;
  public static boolean DEBUG = false;

  public static Status STATUS = Status.UNLOAD;

  public enum Status {
    UNLOAD,
    NORMAL,
    UPDATE,
    RELOAD,
    STOP
  }

  private static final ExecutorService connectExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer connectTimer = new Timer();
  public static WebSocketClient webSocketClient;
  protected static String key = null;

  public static void send(String event, String message, JSONObject data) {
    if (webSocketClient != null && webSocketClient.ready) {
      webSocketClient.send(event, message, data);
    }
  }

  public static void send(String event, String message, JSONObject data, String target) {
    if (webSocketClient != null && webSocketClient.ready) {
      webSocketClient.send(event, message, data, target);
    }
  }

  public static class WebSocketClient {

    private final String urlString;
    public boolean connected = false;
    public boolean reconnect = true;
    public boolean ready = false;
    private WebSocket ws = null;

    public WebSocketClient(String urlString) {
      this.urlString = urlString;
    }

    public void connect() {
      try {
        this.disconnect();
        if (Config.exist(".terminal")) {
          key = new Config(".terminal").getString("key");
          if (!Config.exist(".key")) {
            new Config(".key").set("key", key);
          }
        }
        if (Config.exist(".key")) {
          key = new Config(".key").getString("key");
        }
        if (key == null) {
          Terminal.debug(PREFIX + "No verification key found");
          try {
            URL url = new URL("https://api.wany.io/cherry/terminal/plugin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Cherry Terminal");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write("{}".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            Terminal.debug(PREFIX + "Generate new verification key");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
              Reader inputReader = new InputStreamReader(connection.getInputStream());
              BufferedReader streamReader = new BufferedReader(inputReader);
              String streamLine;
              StringBuilder content = new StringBuilder();
              while ((streamLine = streamReader.readLine()) != null) {
                content.append(streamLine);
              }
              streamReader.close();
              JSONObject object = new JSONObject(content.toString());
              JSONObject data = object.getJSONObject("data");
              key = data.getString("key");
              Terminal.debug(PREFIX + "New verification key generated (" + key.length() + ")");
              new Config(".key").set("key", key);
            }
            connection.disconnect();
          }
          catch (SocketTimeoutException ignored) {
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        this.ws = HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(URI.create(this.urlString), new WebSocketListener(this)).join();
      }
      catch (CompletionException ignored) {
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void disconnect() {
      if (this.ws != null) {
        ws.abort();
      }
    }

    public void send(String event, String message, JSONObject data) {
      JSONObject object = new JSONObject();
      object.put("event", event);
      object.put("message", message);
      object.put("data", data);
      String string = object.toString();
      this.sendMessage(string);
    }

    public void send(String event, String message, JSONObject data, String target) {
      JSONObject object = new JSONObject();
      object.put("event", event);
      object.put("message", message);
      object.put("data", data);
      object.put("target", target);
      String string = object.toString();
      this.sendMessage(string);
    }

    public void sendMessage(String message) {
      if (this.ws != null && this.connected && message != null) {
        this.ws.sendText(message, true);
      }
    }

    public void close() {
      this.reconnect = false;
      this.disconnect();
    }

    private class WebSocketListener implements WebSocket.Listener {

      private final WebSocketClient client;
      private StringBuilder message = new StringBuilder();

      public WebSocketListener(WebSocketClient client) {
        this.client = client;
      }

      @Override
      public void onOpen(WebSocket webSocket) {
        Terminal.debug(PREFIX + "Connection opened");
        client.connected = true;
        WebSocket.Listener.super.onOpen(webSocket);
      }

      @Override
      public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        Terminal.debug(PREFIX + "Connection closed");
        client.connected = false;
        client.ready = false;
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
      }

      @Override
      public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        if (last) {
          message.append(data);
          TerminalMessageHandler.onMessage(message.toString());
          message = new StringBuilder();
        }
        else {
          message.append(data);
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
      }

      @Override
      public void onError(WebSocket webSocket, Throwable error) {
        Terminal.debug(PREFIX + "Connection error");
        error.printStackTrace();
        client.connected = false;
        client.ready = false;
        WebSocket.Listener.super.onError(webSocket, error);
      }

    }

  }
  
  public static void debug(String message) {
    if (DEBUG) {
      Console.debug(message);
    }
  }

  public static void onLoad() {
    STATUS = Status.NORMAL;
    Terminal.debug(PREFIX + "Load Terminal");
    webSocketClient = new WebSocketClient("wss://protocol.wany.io/cherry/terminal/plugin");
    connectExecutorService.submit(() -> connectTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (!webSocketClient.connected && webSocketClient.reconnect) {
          try {
            webSocketClient.connect();
          }
          catch (Exception e) {
            Terminal.debug(PREFIX + "Connect Failed: " + e);
          }
        }
        else if (!Terminal.webSocketClient.ready) {
          webSocketClient.disconnect();
          webSocketClient.connected = false;
        }
      }
    }, 0, 1000));
    TerminalConsole.onLoad();
  }

  public static void onEnable() {
    Terminal.debug(PREFIX + "Enabling Terminal");
    TerminalServerStatus.onEnable();
  }

  public static void onDisable() {
    if (STATUS == Status.NORMAL) {
      STATUS = Status.STOP;
    }
    long delay = 0;
    if (Terminal.STATUS == Terminal.Status.STOP) {
      delay = 500;
    }
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        Terminal.debug(PREFIX + "Disabling Terminal");
        TerminalConsole.onDisable();
        TerminalServerStatus.onDisable();
        connectTimer.cancel();
        connectExecutorService.shutdownNow();
        webSocketClient.close();
        webSocketClient.disconnect();
      }
    }, delay);
  }

}
