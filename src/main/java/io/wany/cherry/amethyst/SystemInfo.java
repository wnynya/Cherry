package io.wany.cherry.amethyst;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.*;
import com.sun.management.OperatingSystemMXBean;
import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemInfo {

  public static final String userName = System.getProperty("user.name");
  public static final String userHome = System.getProperty("user.home");
  public static final String userDir = System.getProperty("user.dir");

  public static final String osName = System.getProperty("os.name");
  public static final String osArch = System.getProperty("os.arch");
  public static final String osVersion = System.getProperty("os.version");

  public static final String javaVMVersion = System.getProperty("java.vm.version");
  public static final String javaVMVendor = System.getProperty("java.vm.vendor");
  public static final String javaRuntimeName = System.getProperty("java.runtime.name");
  public static final String javaHome = System.getProperty("java.home");

  public static final OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  public static final String osbName = osb.getName();
  public static final String osbVersion = osb.getVersion();
  public static final String osbArch = osb.getArch();
  public static final int osbAvailableProcessors = osb.getAvailableProcessors();
  public static final long osbTotalMemorySize = osb.getTotalMemorySize();
  public static final long osbCommittedVirtualMemorySize = osb.getCommittedVirtualMemorySize();

  private static int serverCurrentTPS = 0;
  public static int serverLastTPS = 0;

  public static List<Cpu> cpus = new ArrayList<>();
  public static List<Gpu> gpus = new ArrayList<>();
  public static List<Disk> disks = new ArrayList<>();
  public static List<Mobo> mobos = new ArrayList<>();

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

  public static void onEnable() {
    bukkitTask1t = Bukkit.getScheduler().runTaskTimer(Cherry.PLUGIN, () -> serverCurrentTPS++, 0L, 1L);
    executorService.submit(() -> {
      timer1s.schedule(new TimerTask() {
        @Override
        public void run() {
          serverLastTPS = serverCurrentTPS;
          serverCurrentTPS = 0;
        }
      }, 0, 1000);
      Components components = JSensors.get.components();
      cpus = components.cpus;
      gpus = components.gpus;
      disks = components.disks;
      mobos = components.mobos;
    });
  }

  public static void onDisable() {
    bukkitTask1t.cancel();
    timer1s.cancel();
    executorService.shutdownNow();
  }

}
