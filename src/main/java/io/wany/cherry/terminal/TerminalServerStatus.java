package io.wany.cherry.terminal;

import com.sun.management.OperatingSystemMXBean;
import io.wany.cherry.Cherry;
import io.wany.cherry.amethyst.SystemInfo;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TerminalServerStatus {

  private static final ExecutorService sendServerStatusExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer sendServerStatusTimer1s = new Timer();
  private static final Timer sendServerStatusTimer10s = new Timer();

  public static void sendStatus(String status) {
    String event = "server-status";
    String message = "Server status: " + status;
    JSONObject data = new JSONObject();
    data.put("status", status);
    Terminal.send(event, message, data);
  }

  public static void sendInfo() {
    String event = "server-info";
    String message = "Server info";
    JSONObject data = new JSONObject();

    JSONObject systemData = new JSONObject();
    OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    systemData.put("name", osb.getName());
    systemData.put("version", osb.getVersion());
    systemData.put("arch", osb.getArch());
    systemData.put("availableProcessors", osb.getAvailableProcessors());
    systemData.put("totalMemorySize", osb.getTotalMemorySize());
    systemData.put("committedVirtualMemorySize", osb.getCommittedVirtualMemorySize());
    data.put("system", systemData);

    JSONObject userData = new JSONObject();
    userData.put("name", System.getProperty("user.name"));
    userData.put("home", System.getProperty("user.home"));
    userData.put("dir", System.getProperty("user.dir"));
    data.put("user", userData);

    JSONObject osData = new JSONObject();
    osData.put("version", System.getProperty("os.version"));
    osData.put("name", System.getProperty("os.name"));
    osData.put("arch", System.getProperty("os.arch"));
    data.put("os", osData);

    JSONObject javaData = new JSONObject();
    javaData.put("version", System.getProperty("java.vm.version"));
    javaData.put("runtime", System.getProperty("java.runtime.name"));
    javaData.put("vendor", System.getProperty("java.vm.vendor"));
    javaData.put("home", System.getProperty("java.home"));
    data.put("java", javaData);

    JSONObject serverData = new JSONObject();
    serverData.put("name", Bukkit.getServer().getName());
    serverData.put("ip", Bukkit.getServer().getIp());
    serverData.put("port", Bukkit.getServer().getPort());
    serverData.put("maxPlayers", Bukkit.getMaxPlayers());
    serverData.put("version", Bukkit.getServer().getVersion());
    serverData.put("bukkitVersion", Bukkit.getServer().getBukkitVersion());
    serverData.put("minecraftVersion", Bukkit.getServer().getMinecraftVersion());
    serverData.put("motd", GsonComponentSerializer.gson().serialize(Bukkit.getServer().motd()));
    String serverDir = new File(new File(Cherry.PLUGIN.getDataFolder().getAbsoluteFile().getParent()).getParent()).getAbsolutePath().replace("\\", "/");
    serverData.put("dir", serverDir);
    data.put("server", serverData);

    JSONObject networkData = new JSONObject();
    String ip = null;
    String hostname = null;
    try {
      ip = InetAddress.getLocalHost().toString();
      hostname = InetAddress.getLocalHost().getHostName();
    }
    catch (Exception ignored) {
    }
    networkData.put("ip", ip);
    networkData.put("hostname", hostname);
    data.put("network", networkData);

    Terminal.send(event, message, data);
  }

  public static void sendSystem() {
    String event = "server-system";
    String message = "Server system status";
    JSONObject data = new JSONObject();
    Runtime r = Runtime.getRuntime();
    data.put("memory-free", r.freeMemory());
    data.put("memory-max", r.maxMemory());
    data.put("memory-total", r.totalMemory());
    com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    data.put("cpu-system-load", osb.getCpuLoad());
    data.put("cpu-process-load", osb.getProcessCpuLoad());
    data.put("tps", SystemInfo.serverLastTPS);
    Terminal.send(event, message, data);
  }

  public static void onEnable() {
    sendServerStatusExecutorService.submit(() -> {
      sendServerStatusTimer1s.schedule(new TimerTask() {
        @Override
        public void run() {
          if (Terminal.webSocketClient.connected) {
            TerminalPlayers.sendPlayers();
            sendSystem();
          }
        }
      }, 0, 1000);
      sendServerStatusTimer10s.schedule(new TimerTask() {
        @Override
        public void run() {
          if (Terminal.webSocketClient.connected) {
            TerminalWorlds.sendWorlds();
          }
        }
      }, 0, 10000);
    });
  }

  public static void onDisable() {
    sendStatus("offline");
    sendServerStatusTimer1s.cancel();
    sendServerStatusTimer10s.cancel();
    sendServerStatusExecutorService.shutdownNow();
  }

}
