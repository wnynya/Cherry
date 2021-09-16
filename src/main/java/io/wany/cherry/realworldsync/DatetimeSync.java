package io.wany.cherry.realworldsync;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatetimeSync {

  public static String NAME = "DatetimeSync";
  public static String PREFIX = RealWorldSync.PREFIX + "[" + NAME +"]: ";
  public static boolean ENABLE = false;
  public static ZonedDateTime timezone;

  private final World world;
  private Timer timer = new Timer();

  public DatetimeSync(World world) {
    this.world = world;
  }

  public void sync() {
    if (Boolean.TRUE.equals(this.world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))) {
      this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
      Console.debug(PREFIX + "Set " + this.world.getName() + " world's gamerule doDaylightCycle false");
    }
    this.timer = new Timer();
    this.timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (timezone == null) {
          return;
        }
        int day = timezone.getDayOfYear();
        int hour = timezone.getHour();
        int minute = timezone.getMinute();
        int second = timezone.getSecond();
        setTime(day, hour, minute, second);
      }
    }, 0, 1000);
  }

  public void close() {
    this.timer.cancel();
  }

  public void setTime(int day, int hour, int minute, int second) {
    long time = day * 24000L;
    time += (hour * 1000L) - 6000;
    time += minute / 60.0 * 1000L;
    time += second / 3600.0 * 1000L;
    long finalTime = time;
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> world.setFullTime(finalTime));
  }

  private static final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static final Timer mainTimer = new Timer();
  private static final List<DatetimeSync> datetimeSyncs = new ArrayList<>();

  public static void onEnable() {
    if (!Cherry.CONFIG.getBoolean("realworldsync.datetime.enable")) {
      Console.debug(PREFIX + NAME + " Disabled");
      return;
    }
    Console.debug(PREFIX + "Enabling " + NAME);
    ENABLE = true;

    String timezoneString = Cherry.CONFIG.getString("realworldsync.datetime.timezone");
    if (timezoneString == null || timezoneString.equals("")) {
      Console.debug(PREFIX + "ERROR: No timezone");
      return;
    }
    Console.debug(PREFIX + "Sync with the datetime in " + timezoneString + "");

    executorService.submit(() -> mainTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(timezoneString);
        timezone = instant.atZone(zoneId);
      }
    }, 0, 1000));

    for (World world : Bukkit.getWorlds()) {
      if (world.isNatural()) {
        DatetimeSync datetimeSync = new DatetimeSync(world);
        datetimeSyncs.add(datetimeSync);
        datetimeSync.sync();
      }
    }
  }

  public static void onDisable() {
    mainTimer.cancel();
    executorService.shutdownNow();
    for (DatetimeSync datetimeSync : datetimeSyncs) {
      datetimeSync.close();
    }
  }

}
