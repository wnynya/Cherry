package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Speedometer {

  private static HashMap<Player, Location> lastLoc = new HashMap<>();

  public static List<Player> players = new ArrayList<>();

  public static void loop() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Cherry.getPlugin(), new Runnable() {
      public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
          if (lastLoc.containsKey(p)) {
            Location ll = lastLoc.get(p);
            Location cl = p.getLocation();
            double length = ll.distance(cl);
            double speed = length * 20;
            Msg.actionBar(p, Msg.n2s("&6" + (Math.round(speed*100) / 100.0) + " B/s"));
            lastLoc.replace(p, cl);
          }
          else {
            lastLoc.put(p, p.getLocation());
          }
        }
      }
    }, 0, 1L);
  }
}
