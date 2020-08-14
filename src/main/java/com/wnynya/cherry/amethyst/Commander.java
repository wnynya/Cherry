package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Commander {

  public static void winCmd(String msg) {
    try {
      ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", msg);
      builder.redirectErrorStream(true);
      Process p = builder.start();
      BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    }
    catch (Exception ignored) {
    }
  }

  public static void opList() {
    Config opConfig = new Config("ops", false);
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Cherry.plugin, new Runnable() {
      public void run() {
        if (opConfig.getConfig() == null) {
          return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
          if (!opConfig.getConfig().getBoolean("ops." + p.getUniqueId())) {
            p.setOp(false);
          }
        }
      }
    }, 0, 5L);
  }

  public static void init() {
    opList();
  }

}
