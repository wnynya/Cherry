package io.wany.cherry.realworldsync;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherSync {

  public static String NAME = "WeatherSync";
  public static String PREFIX = RealWorldSync.PREFIX + "[" + NAME +"]: ";
  public static boolean ENABLE = false;
  public static String location;

  private final World world;
  private Timer timer = new Timer();

  public WeatherSync(World world) {
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
        }, 0, 1000 * 60 * 5);
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
        }, 0, 1000 * 60);
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
        }, 0, 1000 * 60);
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
  private static final List<WeatherSync> weatherSyncs = new ArrayList<>();

  public static void onEnable() {
    if (!Cherry.CONFIG.getBoolean("realworldsync.weather.enable")) {
      Console.debug(PREFIX + NAME + " Disabled");
      return;
    }
    Console.debug(PREFIX + "Enabling " + NAME);
    ENABLE = true;

    location = Cherry.CONFIG.getString("realworldsync.weather.location");
    if (location == null || location.equals("")) {
      Console.debug(PREFIX + "ERROR: No location");
      return;
    }
    Console.debug(PREFIX + "Sync with the weather in " + location + "");

    for (World world : Bukkit.getWorlds()) {
      if (world.isNatural()) {
        weatherSyncs.add(new WeatherSync(world));
      }
    }
    executorService.submit(() -> mainTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        Weather weather = Weather.UNKNOWN;
        try {
          URL url = new URL( "https://api.wany.io/terminal/weather/" + location);

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
            JSONObject sys = (JSONObject) current.get("sys");
            JSONObject weatherData = (JSONObject) ((JSONArray) current.get("weather")).get(0);
            long weatherID = (long) weatherData.get("id");
            String weatherName = (String) weatherData.get("main");
            String city = (String) current.get("name");
            String country = (String) sys.get("country");
            country = new Locale("", country).getDisplayCountry(new Locale("en"));

            Console.debug(PREFIX + "Current weather in " + city + ", " + country + ": " + weatherID + ", " + weatherName.toUpperCase());

            if (200 <= weatherID && weatherID <= 299) { //Thunderstorm
              weather = Weather.STORM;
            }
            else if (300 <= weatherID && weatherID <= 399) { // Drizzle
              weather = Weather.STORM;
            }
            else if (500 <= weatherID && weatherID <= 599) { // Rain
              weather = Weather.RAIN;
            }
            else if (600 <= weatherID && weatherID <= 699) { // Snow
              weather = Weather.RAIN;
            }
            else if (700 <= weatherID && weatherID <= 799) { // Atmosphere
              weather = Weather.CLEAR;
            }
            else if (800 == weatherID) { // Clear
              weather = Weather.CLEAR;
            }
            else if (801 <= weatherID && weatherID <= 899) { // Clouds
              weather = Weather.CLEAR;
            }
          }
          else {
            Console.debug(PREFIX + "ERROR: Fail to sync with the weather in " + location + ": " + responseCode);
          }
          connection.disconnect();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        for (WeatherSync weatherSync : weatherSyncs) {
          weatherSync.set(weather);
        }
      }
    }, 0, 1000 * 60 * 10));
  }

  public static void onDisable() {
    mainTimer.cancel();
    for (WeatherSync weatherSync : weatherSyncs) {
      weatherSync.close();
    }
    executorService.shutdownNow();
  }
  
}
