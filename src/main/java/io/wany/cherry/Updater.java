package io.wany.cherry;

import io.wany.cherry.amethyst.PluginLoader;
import io.wany.cherry.amethyst.RandomString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class Updater {

  public static String NAME = "Updater";
  public static String COLORHEX = "#00FFC8";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[" + NAME + "]:&r ";

  private static final Cherry plugin = Cherry.PLUGIN;
  private static final String pluginAPI = plugin.getDescription().getAPIVersion();
  private static final String userAgent = NAME;
  public static Updater defaultUpdater;

  public static class NotFoundException extends RuntimeException {}
  public static class InternalServerErrorException extends RuntimeException {}
  public static class UnknownException extends Exception {}

  private final String api; //https://cherry.wany.io/api/builds
  private final Channel channel;
  private final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static final Timer timer = new Timer();

  public Updater(@NotNull String api, @NotNull Channel channel) {
    this.api = api;
    this.channel = channel;
  }

  public Version getLatestVersion() throws IOException, ParseException, NotFoundException, InternalServerErrorException, UnknownException {

    URL url = new URL(this.api + "/" + this.channel.name + "/latest" + "?api=" + pluginAPI);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", userAgent);
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
      String versionName = data.get("version").toString();
      String versionStorage = data.get("url").toString();
      URL versionStorageURL = new URL(versionStorage);
      return new Version(versionName, versionStorageURL);
    }
    else if (responseCode == 404) { // Not Found
      connection.disconnect();
      throw new NotFoundException();
    }
    else if (responseCode == 500) { // Internal Server Error
      connection.disconnect();
      throw new InternalServerErrorException();
    }
    connection.disconnect();
    throw new UnknownException();

  }

  public void auto() {
    try {
      Version version = this.getLatestVersion();
      if (plugin.getDescription().getVersion().equals(version.name)) {
        return;
      }
      version.download();
      version.update();
    }
    catch (Exception ignored) {}
  }

  public void openAutomation() {
    this.executorService.submit(() -> {
      this.timer.schedule(new TimerTask() {
        @Override
        public void run() {
          auto();
        }
      }, 0, this.channel.checkInterval);
    });
  }

  public void closeAutomation() {
    this.timer.cancel();
    this.executorService.shutdownNow();
  }

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
    return true;
  }

  public String getChannelName() {
    return this.channel.name;
  }

  public static class Version {

    public final String name;
    public final URL storage;
    private File file = null;

    private Version(String name, URL storage) {
      this.name = name;
      this.storage = storage;
    }

    @SuppressWarnings("all")
    public File download() throws SecurityException, FileNotFoundException, IOException {
      String name = new RandomString(6, new SecureRandom(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").nextString();

      File file = new File(plugin.getDataFolder() + "/" + name + ".temp");

      try {
        if (file.exists()) {
          file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();
      } catch (SecurityException exception) {
        file.delete();
        throw exception;
      } catch (IOException exception) {
        file.delete();
        throw exception;
      }

      try {
        BufferedInputStream bis = new BufferedInputStream(this.storage.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
          fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
      } catch (SecurityException exception) {
        file.delete();
        throw exception;
      } catch (FileNotFoundException exception) {
        file.delete();
        throw exception;
      } catch (IOException exception) {
        file.delete();
        throw exception;
      }

      this.file = file;
      return file;
    }

    @SuppressWarnings("all")
    public void update() throws IOException {
      if (this.file == null || this.name == null) {
        return;
      }

      String name = plugin.getDescription().getName();

      if (Cherry.NIGHT && Cherry.CONFIG.getString("night-name") != null) {
        name = Cherry.CONFIG.getString("night-name");
      }

      File pluginsDir = new File("plugins");
      File pluginFile = new File(pluginsDir, name + "-" + this.name + ".jar");

      byte[] data = new byte[0];
      data = Files.readAllBytes(this.file.toPath());
      Path path = pluginFile.toPath();
      this.file.delete();
      Files.write(path, data);

      Bukkit.getScheduler().runTask(plugin, () -> {
        PluginLoader.unload();
        if (!pluginFile.getPath().equals(Cherry.FILE.getPath())) {
          Cherry.FILE.delete();
        }
        PluginLoader.load(pluginFile);
      });
    }

  }

  public static class Channel {

    public final String name;
    public final long checkInterval;

    public Channel(String name, long checkInterval) {
      this.name = name;
      this.checkInterval = checkInterval;
    }

  }


  public static void onEnable() {
    String apiURLString = "https://api.wany.io/cherry/builds";
    String channelName = Cherry.CONFIG.getString("updater.channel");
    if (channelName == null) {
      channelName = "release";
    }
    long checkInterval = 1000;
    if (channelName.equals("release")) {
      checkInterval = 1000 * 60 * 60;
    }
    Channel channel = new Channel(channelName, checkInterval);
    defaultUpdater = new Updater(apiURLString, channel);
    if (!Cherry.CONFIG.getBoolean("updater.auto")) {
      return;
    }
    defaultUpdater.openAutomation();
  }

  public static void onDisable() {
    defaultUpdater.closeAutomation();
  }

}
