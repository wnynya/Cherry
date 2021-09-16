package io.wany.cherry.amethyst;

import com.profesorfalken.jsensors.model.components.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JSensors {

  public static List<Cpu> cpus = new ArrayList<>();
  public static List<Gpu> gpus = new ArrayList<>();
  public static List<Disk> disks = new ArrayList<>();
  public static List<Mobo> mobos = new ArrayList<>();

  private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

  public static void onEnable() {
    executorService.submit(() -> {
      Components components = com.profesorfalken.jsensors.JSensors.get.components();
      cpus = components.cpus;
      gpus = components.gpus;
      disks = components.disks;
      mobos = components.mobos;
    });
  }

  public static void onDisable() {
    executorService.shutdownNow();
  }

}
