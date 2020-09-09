package com.wnynya.cherry.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.terminal.TerminalLogFilter;
import com.wnynya.cherry.network.terminal.WebSocketClient8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Terminal {

  private static final ExecutorService terminalExecutorService = Executors.newFixedThreadPool(1);

  public static boolean connected = false;
  private static WebSocketClient8 websocket;
  private static String host;
  private static String name;
  private static String auth;

  /* Connect to websocket server */
  private static final ExecutorService connectExecutorService = Executors.newFixedThreadPool(1);
  private static Timer connectTimer;
  private static void connect() {
    connectExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        connectTimer = new Timer();
        connectTimer.schedule(new TimerTask() {
          public void run() {
            if (!Terminal.connected) {
              try {
                URL url = new URL("http://" + host + "/ping");
                InputStream is = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                  content.append(line);
                }
                bufferedReader.close();

                if (content.toString().equals("pong!")) {
                  websocket = new WebSocketClient8(new URI("ws://" + host + "/terminal"));
                  websocket.connect();
                }
              }
              catch (Exception ignored) {}
            }
          }
        }, 0, 2 * 1000);
      }
    });
  }

  public static void open() {

    Terminal.connected = true;

    JsonObject message = new JsonObject();
    message.addProperty("event", "connect");
    JsonObject data = new JsonObject();
    data.addProperty("server", name);
    data.addProperty("key", auth);
    data.addProperty("pathlimit", Cherry.serverDir.getAbsolutePath());
    message.add("data", data);
    Terminal.send(message);

    DashBoard.lineStatus("online");

  }

  public static void close() {

    Terminal.connected = false;

  }

  private static void send(String msg) {
    if (websocket != null && websocket.isOpen()) {
      websocket.send(msg);
    }
  }
  private static void send(JsonObject msg) {
    String sendMsg = "";
    try {
      sendMsg = msg.toString();
    } catch (Exception e ) { e.printStackTrace();}
    Terminal.send(sendMsg);
  }

  public static void rawMessageHandler(String resData) {

    JsonObject m = new JsonObject();
    JsonParser parser = new JsonParser();
    try {
      m = parser.parse(resData).getAsJsonObject();
    } catch (Exception ignored) {}

    String event = m.get("event").getAsString();
    JsonObject data = m.getAsJsonObject("data");
    String account = m.get("account").getAsString();

    switch (event) {

      case "connect": {
        break;
      }

      case "dashboard-basic": {
        DashBoard.basicInfo(account);
        break;
      }

      case "console-command": {
        String cmd = data.get("message").getAsString();
        Console.command(cmd);
        break;
      }

      case "console-prelog": {
        Console.getLogData(account);
        break;
      }

      case "explorer-get": {
        String path = data.get("path").getAsString();
        Explorer.sendPathData(account, path);
        break;
      }

      case "explorer-filestream-pull-open": {
        Explorer.sendFileStream(account, data);
        break;
      }

      case "explorer-filestream-push-open": {
        Explorer.receiveFileStreamOpen(account, data);
        break;
      }
      case "explorer-filestream-push-write": {
        Explorer.receiveFileStreamWrite(account, data);
        break;
      }
      case "explorer-filestream-push-close": {
        Explorer.receiveFileStreamClose(account, data);
        break;
      }

      case "explorer-delete": {
        String path = data.get("path").getAsString();
        Explorer.delete(path);
        break;
      }

    }

  }

  public static class DashBoard {

    public static void basicInfo(String sendTo) {
      try {
        JsonObject message = new JsonObject();
        message.addProperty("event", "dashboard-basic");
        message.addProperty("account", sendTo);
        JsonObject data = new JsonObject();
        JsonObject systemData = new JsonObject();
        systemData.addProperty("osVersion", System.getProperty("os.version"));
        systemData.addProperty("osName", System.getProperty("os.name"));
        systemData.addProperty("osArch", System.getProperty("os.arch"));
        JsonObject javaData = new JsonObject();
        javaData.addProperty("version", System.getProperty("java.version"));
        javaData.addProperty("home", System.getProperty("java.home"));
        systemData.add("java", javaData);
        JsonObject serverData = new JsonObject();
        serverData.addProperty("ip", Bukkit.getServer().getIp());
        serverData.addProperty("port", Bukkit.getServer().getPort());
        serverData.addProperty("version", Bukkit.getServer().getVersion());
        serverData.addProperty("bukkitversion", Bukkit.getServer().getBukkitVersion());
        serverData.addProperty("minecraftversion", Bukkit.getServer().getMinecraftVersion());
        serverData.addProperty("dir", Cherry.serverDir.getAbsolutePath());
        systemData.add("server", serverData);
        data.add("system", systemData);
        JsonObject userData = new JsonObject();
        userData.addProperty("name", System.getProperty("user.name"));
        userData.addProperty("home", System.getProperty("user.home"));
        userData.addProperty("dir", System.getProperty("user.dir"));
        data.add("user", userData);
        message.add("data", data);
        Terminal.send(message);
      } catch (Exception ignored) {}
    }

    public static void lineStatus(String status) {
      JsonObject message = new JsonObject();
      message.addProperty("event", "dashboard-linestatus");
      JsonObject data = new JsonObject();
      data.addProperty("status", status);
      message.add("data", data);
      Terminal.send(message);
    }

    public static final ExecutorService serverStatusExecutorService = Executors.newFixedThreadPool(1);
    public static Timer serverStatusTimer;
    public static void serverStatus() {
      serverStatusExecutorService.submit(() -> {
        serverStatusTimer = new Timer();
        serverStatusTimer.schedule(new TimerTask() {
          public void run() {
            if (Terminal.connected) {
              JsonObject message = new JsonObject();
              message.addProperty("event", "dashboard-serverstatus");
              JsonObject data = new JsonObject();
              Runtime r = Runtime.getRuntime();
              com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
              data.addProperty("freeMemory", r.freeMemory());
              data.addProperty("maxMemory", r.maxMemory());
              data.addProperty("totalMemory", r.totalMemory());
              data.addProperty("cpuUsage", osb.getSystemCpuLoad());
              data.addProperty("cpuUsageServer", osb.getProcessCpuLoad());
              data.addProperty("tps", Bukkit.getServer().getTPS()[0]);
              message.add("data", data);
              Terminal.send(message);
            }
          }}, 0, 1000);
      });
    }

  }

  public static class Console {

    public static void log (String msg, long time, String thread, String level, String logger) {
      if (!connected) { return; }
      JsonObject message = new JsonObject();
      message.addProperty("event", "console-log");
      JsonObject data = new JsonObject();
      data.addProperty("message", msg);
      data.addProperty("time", time);
      data.addProperty("thread", thread);
      data.addProperty("level", level);
      data.addProperty("logger", logger);
      message.add("data", data);
      Terminal.send(message);
      Terminal.Console.LogData ld = new Terminal.Console.LogData(time, msg, thread, level, logger);
      Terminal.Console.writeLogData(ld);
    }

    public static void command(String msg) {
      msg = msg.replaceAll("&nbsp;", " ");
      msg = msg.replaceAll("&lt;", "<");
      msg = msg.replaceAll("&gt;", ">");
      msg = msg.replaceAll("&amp;", "&");
      msg = msg.replaceAll("&quot;", "\"");
      msg = msg.replaceAll("&apos;", "'");

      String finalMsg = msg;
      Bukkit.getScheduler().runTask(Cherry.plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), finalMsg));
    }

    public static List<LogData> logData = new ArrayList<>();

    public static class LogData {
      public long time;
      public String message;
      public String thread;
      public String level;
      public String logger;
      public LogData(long time, String message, String thread, String level, String logger) {
        this.time = time;
        this.message = message;
        this.thread = thread;
        this.level = level;
        this.logger = logger;
      }
    }

    public static void writeLogData(LogData ld) {
      while (logData.size() > 1000) {
        logData.remove(0);
      }
      logData.add(ld);
    }

    public static void getLogData(String sendTo) {
      try {
        JsonObject message = new JsonObject();
        message.addProperty("event", "console-prelog");
        message.addProperty("account", sendTo);
        JsonObject data = new JsonObject();
        JsonArray logs = new JsonArray();
        for (LogData ld : logData) {
          JsonObject ldata = new JsonObject();
          ldata.addProperty("time", ld.time);
          ldata.addProperty("message", ld.message);
          ldata.addProperty("thread", ld.thread);
          ldata.addProperty("level", ld.level);
          ldata.addProperty("logger", ld.logger);
          logs.add(ldata);
        }
        data.add("logs", logs);
        message.add("data", data);
        Terminal.send(message);
      } catch (Exception ignored) {}
    }

  }

  public static class Explorer {

    public static void sendPathData(String account, String path) {

      if (path.equals("root")) {
        sendRootDirData(account);
        return;
      }

      File file = new File(path);
      // Check file exist
      if (!file.exists()) {
        alertError(account, "file or dir not exist.");
        return;
      }

      if (file.isDirectory()) {
        sendDirData(account, new File(path));
      }
    }

    public static void sendRootDirData(String account) {
      JsonObject message = new JsonObject();
      JsonObject data = new JsonObject();
      message.addProperty("event", "explorer-directory");
      data.addProperty("path", "root");
      JsonArray diri = new JsonArray();
      JsonObject fi;
      FileSystemView fsv = FileSystemView.getFileSystemView();
      File[] rs = File.listRoots();
      for (File r : rs) {
        fi = new JsonObject();
        fi.addProperty("path", r.getAbsolutePath());
        fi.addProperty("name", fsv.getSystemTypeDescription(r));
        fi.addProperty("size", r.length());
        fi.addProperty("type", "drive");
        fi.addProperty("hidden", false);
        fi.addProperty("read", r.canRead());
        fi.addProperty("write", r.canWrite());
        fi.addProperty("execute", r.canExecute());
        diri.add(fi);
      }
      data.add("files", diri);
      message.add("data", data);
      message.addProperty("account", account);
      Terminal.send(message);
    }

    public static void sendDirData(String account, File dir) {
      JsonObject message = new JsonObject();
      JsonObject data = new JsonObject();
      message.addProperty("event", "explorer-directory");
      data.addProperty("path", dir.getAbsolutePath());

      JsonArray diri = new JsonArray();
      JsonObject fi = new JsonObject();
      fi.addProperty("path", dir.getAbsolutePath());
      fi.addProperty("name", ".");
      fi.addProperty("size", 0);
      fi.addProperty("type", "dir");
      fi.addProperty("hidden", false);
      fi.addProperty("read", dir.canRead());
      fi.addProperty("write", dir.canWrite());
      fi.addProperty("execute", dir.canExecute());
      diri.add(fi);

      if (dir.getParentFile() != null) {
        fi = new JsonObject();
        File parentFile = dir.getParentFile();
        fi.addProperty("path", parentFile.getAbsolutePath());
        fi.addProperty("name", "..");
        fi.addProperty("size", 0);
        fi.addProperty("type", "dir");
        fi.addProperty("hidden", false);
        fi.addProperty("read", dir.canRead());
        fi.addProperty("write", dir.canWrite());
        fi.addProperty("execute", dir.canExecute());
        diri.add(fi);
      }
      else {
        fi = new JsonObject();
        fi.addProperty("path", "root");
        fi.addProperty("name", "..");
        fi.addProperty("size", 0);
        fi.addProperty("type", "dir");
        fi.addProperty("hidden", false);
        fi.addProperty("read", dir.canRead());
        fi.addProperty("write", dir.canWrite());
        fi.addProperty("execute", dir.canExecute());
        diri.add(fi);
      }

      File[] files = dir.listFiles();
      if (files != null) {
        for (File subFile : files) {
          fi = new JsonObject();
          fi.addProperty("path", subFile.getAbsolutePath());
          fi.addProperty("name", subFile.getName());
          fi.addProperty("size", subFile.length());
          if (subFile.isDirectory()) {
            fi.addProperty("type", "dir");
          } else if (subFile.isFile()) {
            fi.addProperty("type", "file");
          }
          fi.addProperty("hidden", subFile.isHidden());
          fi.addProperty("read", dir.canRead());
          fi.addProperty("write", dir.canWrite());
          fi.addProperty("execute", dir.canExecute());
          diri.add(fi);
        }
      }

      data.add("files", diri);
      message.add("data", data);
      message.addProperty("account", account);
      Terminal.send(message);
    }

    public static void sendFileStream(String account, JsonObject data) {
      String id = data.get("id").getAsString();
      String path = data.get("path").getAsString();
      File file = new File(path);

      if (!file.exists()) {
        return;
      }

      ExecutorService sendFileStreamExecutorService = Executors.newFixedThreadPool(1);
      sendFileStreamExecutorService.submit(new Runnable() {
        @Override
        public void run() {
          String name = file.getName();
          JsonObject message = new JsonObject();
          JsonObject data = new JsonObject();
          message.addProperty("event", "explorer-filestream-pull-open");
          data.addProperty("id", id);
          data.addProperty("name", name);
          data.addProperty("path", file.getAbsolutePath().replaceAll("/\\/", "/"));
          data.addProperty("totalsize", file.length());
          try {
            data.addProperty("mime", Files.probeContentType(file.toPath()) + "");
          } catch (IOException ignored) {}
          message.add("data", data);
          message.addProperty("account", account);
          Terminal.send(message);

          int bufferSize = 100 * 1024;
          BufferedInputStream bufferedInputStream = null;
          try {
            bufferedInputStream = new BufferedInputStream( new FileInputStream(file) );
            int index = 0;
            while (bufferedInputStream.available() > 0) {
              byte[] bytes = new byte[Math.min(bufferedInputStream.available(), bufferSize)];
              bufferedInputStream.read(bytes, 0, bytes.length);
              char[] hexArray = "0123456789ABCDEF".toCharArray();
              char[] hexChars = new char[bytes.length * 2];
              for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
              }
              String buffer = Base64.getEncoder().encodeToString(bytes);
              message = new JsonObject();
              data = new JsonObject();
              message.addProperty("event", "explorer-filestream-pull-write");
              data.addProperty("id", id);
              data.addProperty("sendsize", index * bufferSize + bytes.length);
              data.addProperty("buffer", buffer);
              message.add("data", data);
              message.addProperty("account", account);
              Terminal.send(message);
              TimeUnit.MILLISECONDS.sleep(20);
              index++;
            }
          } catch (Exception e) {
            e.printStackTrace();
            sendFileStreamExecutorService.shutdown();
          } finally {
            try {
              bufferedInputStream.close();
              message = new JsonObject();
              data = new JsonObject();
              message.addProperty("event", "explorer-filestream-pull-close");
              data.addProperty("id", id);
              message.add("data", data);
              message.addProperty("account", account);
              Terminal.send(message);
              sendFileStreamExecutorService.shutdown();
            } catch (Exception e) {
              e.printStackTrace();
              sendFileStreamExecutorService.shutdown();
            }
          }
        }
      });
    }

    public static class FileData {

      public final String id;
      public final File path;
      OutputStream stream = null;

      public FileData(String id, File path) {
        this.id = id;
        this.path = path;
      }

      public void write(byte[] data) {
        try {
          stream = new FileOutputStream(this.path, true);
          stream.write(data);
          stream.close();
        }
        catch (Exception e) { e.printStackTrace(); }
      }

      public void delete() {
        try {
          this.path.delete();
        } catch (Exception ignored) {}
      }

    }

    private static final HashMap<String, FileData> pushingFileData = new HashMap<>();
    private static final HashMap<File, String> pushingFilePathData = new HashMap<>();
    public static void receiveFileStreamOpen(String account, JsonObject data) {
      String id = data.get("id").getAsString();
      File path = new File(data.get("path").getAsString());
      if (pushingFilePathData.containsKey(path)) {
        return;
      }
      FileData fd = new FileData(id, path);
      fd.delete();
      pushingFileData.put(id, fd);
      pushingFilePathData.put(path, id);
    }
    public static void receiveFileStreamWrite(String account, JsonObject data) {
      String id = data.get("id").getAsString();
      FileData fd = pushingFileData.get(id);
      if (fd == null) { return; }
      String bufferString = data.get("buffer").getAsString();
      int size = bufferString.length();
      byte[] bufferData = new byte[size / 2];
      for (int n = 0; n < size; n += 2) {
        bufferData[n / 2] = (byte) ((Character.digit(bufferString.charAt(n), 16) << 4) + Character.digit(bufferString.charAt(n + 1), 16));
      }
      fd.write(bufferData);
    }
    public static void receiveFileStreamClose(String account, JsonObject data) {
      String id = data.get("id").getAsString();
      FileData fd = pushingFileData.get(id);
      if (fd == null) { return; }

      File parent = fd.path.getParentFile();
      Explorer.sendPathData(account, parent.getAbsolutePath());

      pushingFileData.remove(id);
      pushingFilePathData.remove(fd.path);
    }

    public static void delete(String path) {
      File file = new File(path);
      try {
        file.delete();
      } catch (Exception ignored) {}
    }

  }

  public static void alertError(String account, String msg) {
    JsonObject message = new JsonObject();
    JsonObject data = new JsonObject();
    message.addProperty("event", "error");
    data.addProperty("style", "alert");
    data.addProperty("message", msg);
    message.add("data", data);
    message.addProperty("account", account);
    Terminal.send(message);
  }

  public static void enable() {

    if (!Cherry.config.getBoolean("terminal.enable")) {
      return;
    }

    terminalExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        host = Cherry.config.getString("terminal.host");
        name = Cherry.config.getString("terminal.name");
        auth = Cherry.config.getString("terminal.auth");

        Terminal.connect();

        DashBoard.serverStatus();

        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new TerminalLogFilter());
      }
    });

  }

  public static void disable() {

    DashBoard.lineStatus("offline");

    if (!Cherry.config.getBoolean("terminal.enable")) {
      return;
    }

    TerminalLogFilter.shutdown = true;

    connectTimer.cancel();
    connectExecutorService.shutdown();

    DashBoard.serverStatusTimer.cancel();
    DashBoard.serverStatusExecutorService.shutdown();

    if (Terminal.connected || (websocket != null && websocket.isOpen())) {
      websocket.close();
      websocket = null;
    }

    try {
      TimeUnit.MICROSECONDS.sleep(1);
    } catch (InterruptedException ignored) {
    }

    terminalExecutorService.shutdown();

  }

}
