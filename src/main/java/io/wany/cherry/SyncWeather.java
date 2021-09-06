package io.wany.cherry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncWeather {

  private final World world;
  private Timer timer = new Timer();

  public SyncWeather(World world) {
    this.world = world;
  }

  public void set(Weather weather) {
    switch (weather) {
      case CLEAR, UNKNOWN -> {
        this.timer.cancel();
        this.clear();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
          @Override
          public void run() {
            clear();
          }
        }, 0, 1000 * 10);
      }
      case RAIN -> {
        this.timer.cancel();
        this.rain();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
          @Override
          public void run() {
            rain();
          }
        }, 0, 1000 * 10);
      }
      case STORM -> {
        this.timer.cancel();
        this.storm();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
          @Override
          public void run() {
            storm();
          }
        }, 0, 1000 * 10);
      }
    }
  }

  public void close() {
    this.timer.cancel();
  }

  public void clear() {
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
      world.setWeatherDuration(20 * 60 * 60);
      world.setThunderDuration(20 * 60 * 60);
      world.setStorm(false);
      world.setThundering(false);
    });
  }

  public void rain() {
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
      world.setWeatherDuration(20 * 60 * 60);
      world.setThunderDuration(20 * 60 * 60);
      world.setStorm(true);
      world.setThundering(false);
    });
  }

  public void storm() {
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
      world.setWeatherDuration(20 * 60 * 60);
      world.setThunderDuration(20 * 60 * 60);
      world.setStorm(true);
      world.setThundering(true);
    });
  }

  public enum Weather {
    CLEAR,
    RAIN,
    STORM,
    UNKNOWN
  }

  private static final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static final Timer mainTimer = new Timer();
  private static final List<SyncWeather> syncWeathers = new ArrayList<>();

  public static void onEnable() {
    for (World world : Bukkit.getWorlds()) {
      syncWeathers.add(new SyncWeather(world));
    }
    executorService.submit(() -> mainTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        Weather weather = Weather.UNKNOWN;
        try {
          URL url = new URL( "https://api.wany.io/terminal/weather/Busan,KR");

          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");
          connection.setRequestProperty("User-Agent", "Cherry");
          connection.setConnectTimeout(2000);
          connection.setReadTimeout(2000);

          int responseCode = connection.getResponseCode();
          if (responseCode == 200) { // OK
            Reader inputReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader streamReader = new BufferedReader(inputReader);
            String streamLine;
            StringBuilder content = new StringBuilder();
            while ((streamLine = streamReader.readLine()) != null) {
              content.append(streamLine);
            }
            streamReader.close();
            connection.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(content.toString());
            JSONObject data = (JSONObject) object.get("data");
            JSONObject current = (JSONObject) data.get("current");
            JSONObject weatherData = (JSONObject) ((JSONArray) current.get("weather")).get(0);
            long id = (long) weatherData.get("id");
            if (200 <= id && id <= 299) { //Thunderstorm
              weather = Weather.STORM;
            }
            else if (300 <= id && id <= 399) { // Drizzle
              weather = Weather.STORM;
            }
            else if (500 <= id && id <= 599) { // Rain
              weather = Weather.RAIN;
            }
            else if (600 <= id && id <= 699) { // Snow
              weather = Weather.RAIN;
            }
            else if (700 <= id && id <= 799) { // Atmosphere
              weather = Weather.CLEAR;
            }
            else if (800 == id) { // Clear
              weather = Weather.CLEAR;
            }
            else if (801 <= id && id <= 899) { // Clouds
              weather = Weather.CLEAR;
            }
          }
          connection.disconnect();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        for (SyncWeather syncWeather : syncWeathers) {
          syncWeather.set(weather);
        }
      }
    }, 0, 1000 * 60 * 10));
  }

  public static void onDisable() {
    mainTimer.cancel();
    for (SyncWeather syncWeather : syncWeathers) {
      syncWeather.close();
    }
    executorService.shutdownNow();
  }

}
