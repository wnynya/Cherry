package io.wany.cherry.amethyst;

import com.sun.management.OperatingSystemMXBean;
import io.wany.cherry.Cherry;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemInfo {

  private final String userName = System.getProperty("user.name");
  private final String userHome = System.getProperty("user.home");
  private final String userDir = System.getProperty("user.dir");

  private final String osName = System.getProperty("os.name");
  private final String osArch = System.getProperty("os.arch");
  private final String osVersion = System.getProperty("os.version");

  private final String javaVMVersion = System.getProperty("java.vm.version");
  private final String javaVMVendor = System.getProperty("java.vm.vendor");
  private final String javaRuntimeName = System.getProperty("java.runtime.name");
  private final String javaHome = System.getProperty("java.home");

  private final OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  private final String osbName = osb.getName();
  private final String osbVersion = osb.getVersion();
  private final String osbArch = osb.getArch();
  private final int osbAvailableProcessors = osb.getAvailableProcessors();
  private final long osbTotalMemorySize = osb.getTotalMemorySize();
  private final long osbCommittedVirtualMemorySize = osb.getCommittedVirtualMemorySize();

  private static int serverCurrentTPS = 0;
  private static int serverLastTPS = 0;

  private static final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static BukkitTask bukkitTask1t;
  private static final Timer timer1s = new Timer();

  public static long getFolderByteSize(File folder) {
    long length = 0;
    File[] files = folder.listFiles();
    if (files == null) {
      return 0;
    }
    for (File file : files) {
      if (file.isFile()) {
        length += file.length();
      }
      else {
        length += getFolderByteSize(file);
      }
    }
    return length;
  }

  public static String getFriendlyByteSize(long size) {
    if (size > 1024) {
      size = size / 1024;
    }
    else {
      return Cherry.COLOR + size + "&rB";
    }
    if (size > 1024) {
      size = size / 1024;
    }
    else {
      return Cherry.COLOR + size + "&rK";
    }
    if (size > 1024) {
      size = size / 1024;
    }
    else {
      return Cherry.COLOR + size + "&rM";
    }
    if (size > 1024) {
      size = size / 1024;
    }
    else {
      return Cherry.COLOR + size + "&rG";
    }
    if (size > 1024) {
      size = size / 1024;
    }
    else {
      return Cherry.COLOR + size + "&rT";
    }
    return Cherry.COLOR + size + "&rP";
  }

  public static void onLoad() {
    executorService.submit(() -> {
      bukkitTask1t = Bukkit.getScheduler().runTaskTimerAsynchronously(Cherry.PLUGIN, () -> serverCurrentTPS++, 0L, 1L);
      timer1s.schedule(new TimerTask() {
        @Override
        public void run() {
          serverLastTPS = serverCurrentTPS;
        }
      }, 1000);
    });
  }

  public static void onDisable() {

  }

}
