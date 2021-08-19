package io.wany.cherry;

import io.wany.cherry.amethyst.PluginLoader;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class Updater {

  private static final Cherry plugin = Cherry.PLUGIN;
  private static final String pluginAPI = plugin.getDescription().getAPIVersion();
  private static final String userAgent = "Updater";
  private static final ExecutorService updaterExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer updaterLoopTimer = new Timer();
  public static Updater defaultUpdater;
  private final String api; //https://cherry.wany.io/api/builds
  private final String channel;
  private String version;
  private File file;
  private String url;

  public Updater(String api, String channel) {
    this.api = api;
    this.channel = channel;
  }

  public static void onEnable() {
    String apiURLString = "https://api.wany.io/cherry/builds";
    String channel = Cherry.CONFIG.getString("updater.channel"); //OR Config
    defaultUpdater = new Updater(apiURLString, channel);
    if (!Cherry.CONFIG.getBoolean("updater.auto")) {
      return;
    }
    updaterExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        switch (channel) {
          case "release": {
            updaterLoopTimer.schedule(new TimerTask() {
              @Override
              public void run() {
                defaultUpdater.updateLatest(false);
              }
            }, 0, 1000 * 60 * 60);
            break;
          }
          case "dev": {
            updaterLoopTimer.schedule(new TimerTask() {
              @Override
              public void run() {
                defaultUpdater.updateLatest(false);
              }
            }, 0, 1000 * 2);
            break;
          }
        }
      }
    });
  }

  public static void onDisable() {
    if (Cherry.CONFIG.getBoolean("updater.enable")) {
      return;
    }
    updaterLoopTimer.cancel();
    updaterExecutorService.shutdownNow();
  }

  public void download() throws IOException {
    if (this.version == null) {
      return;
    }

    String name = plugin.getDescription().getName();
    if (Cherry.NIGHT && Cherry.CONFIG.getString("night-name") != null) {
      name = Cherry.CONFIG.getString("night-name");
    }
    File file = new File(plugin.getDataFolder() + "/" + name+ ".jar.temp");
    if (file.exists()) {
      file.delete();
    }

    URL url = new URL(this.url);

    file.getParentFile().mkdirs();
    file.createNewFile();

    BufferedInputStream bis = new BufferedInputStream(url.openStream());
    FileOutputStream fis = new FileOutputStream(file);
    byte[] buffer = new byte[1024];
    int count = 0;
    while ((count = bis.read(buffer, 0, 1024)) != -1) {
      fis.write(buffer, 0, count);
    }

    fis.close();
    bis.close();
    this.file = file;
  }

  public void update() {
    if (this.file == null || this.version == null) {
      return;
    }
    String name = plugin.getDescription().getName();
    if (Cherry.NIGHT && Cherry.CONFIG.getString("night-name") != null) {
      name = Cherry.CONFIG.getString("night-name");
    }
    File pluginsDir = new File("plugins");
    File pluginFile = new File(pluginsDir, name + "-" + this.version + ".jar");

    byte[] data = new byte[0];
    try {
      data = Files.readAllBytes(this.file.toPath());
    }
    catch (IOException e) {
    }
    Path path = pluginFile.toPath();
    file.delete();

    try {
      Files.write(path, data);
    }
    catch (IOException e) {
    }
    Bukkit.getScheduler().runTask(plugin, () -> {
      PluginLoader.unload();
      if (!pluginFile.getPath().equals(Cherry.FILE.getPath())) {
        Cherry.FILE.delete();
      }
      PluginLoader.load(pluginFile);
    });
  }

  public void getLatestVersion() {

    this.version = null;
    this.url = null;
    try {
      URL url = new URL(this.api + "/" + this.channel + "/latest" + "?api=" + pluginAPI);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", userAgent);
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        Reader inputReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
        BufferedReader streamReader = new BufferedReader(inputReader);
        String streamLine;
        StringBuilder content = new StringBuilder();
        while ((streamLine = streamReader.readLine()) != null) {
          content.append(streamLine);
        }
        streamReader.close();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(content.toString());
        JSONObject data = (JSONObject) object.get("data");
        this.version = data.get("version").toString();
        this.url = data.get("url").toString();
        /*JSONObject data = new JSONObject(content).getJSONObject("data");
        version = data.getString("version");*/
      }
      connection.disconnect();
    }
    catch (Exception ignored) {
    }

  }

  public void updateLatest(boolean force) {
    try {
      this.getLatestVersion();
      if (this.version == null) {
        return;
      }
      if (!force && this.version.equals(plugin.getDescription().getVersion())) {
        return;
      }
      this.download();
      this.update();
    }
    catch (Exception ignored) {
    }
  }

}
