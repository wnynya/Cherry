package com.wnynya.cherry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wnynya.cherry.amethyst.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Updater {

  public static final String prefix = "#FF9389;&l[UPDATER]: &r";

  private static ExecutorService updaterExecutorService = Executors.newFixedThreadPool(1);
  private static boolean updating = false;

  static final String api = "http://cherry.wnynya.com";
  static final Cherry plugin = Cherry.plugin;

  /**
   * 플러그인을 업데이트합니다.
   *
   * @param version 업데이트할 버전
   */
  public static void updatePlugin(String version) throws Exception {

    if (updating) { return; }

    updaterExecutorService.submit(new Runnable() {

      @Override
      public void run() {

        try {

          Msg.debug(version);

          updating = true;

          // 서버 상태 업데이트 (CWT)
          //WebSocket.Message.status(WebSocket.Status.UPDATE);

          // 파일 다운로드
          downloadPlugin(version);

          // 다운로드한 파일 확인
          File tempFile = new File(plugin.getDataFolder() + "/" + plugin.getDescription().getName() + ".jar.temp");
          if (!tempFile.exists()) {
            throw new Exception(tempFile.toString() + " 파일을 찾을 수 없습니다.");
          }

          File pluginsDir = new File("plugins");
          File pluginFile = new File(pluginsDir, plugin.getDescription().getName() + "-" + version + ".jar");
          File originalFile = new File(pluginsDir , plugin.file.getName());

          byte[] data = new byte[0];
          try {
            data = Files.readAllBytes(tempFile.toPath());
          } catch (IOException e) {
            e.printStackTrace();
          }
          Path path = pluginFile.toPath();

          try {
            Files.write(path, data);
          } catch (IOException e) {
            e.printStackTrace();
          }

          // 임시 파일 삭제
          tempFile.delete();

          // 플러그인 로드
          Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
              PluginLoader.unload();
              PluginLoader.load(pluginFile);
              if (originalFile.exists()) {
                originalFile.delete();
              }
            }
          });

          updating = false;

        }
        catch (Exception e) {
          e.printStackTrace();
        }

        loopTimer.cancel();

      }

    });

    updaterExecutorService.shutdown();

  }



  /**
   * 플러그인 파일을 다운로드합니다.
   *
   * @param version 다운로드할 버전
   */
  private static void downloadPlugin(String version) throws IOException {

    try {
      File file = new File(plugin.getDataFolder() + "/" + plugin.getDescription().getName() + ".jar.temp");

      URL url = new URL(api + "/build/" + version + "/" + version + ".jar");

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
    }
    catch (Exception e) {
      throw e;
    }

  }



  private static String getData() {

    try {
      JsonObject data = new JsonObject();
      JsonObject systemData = new JsonObject();
      systemData.addProperty("osVersion", System.getProperty("os.version"));
      systemData.addProperty("osName", System.getProperty("os.name"));
      systemData.addProperty("osArch", System.getProperty("os.arch"));
      JsonObject javaData = new JsonObject();
      javaData.addProperty("version", System.getProperty("java.version"));
      javaData.addProperty("home", System.getProperty("java.home"));
      systemData.add("java", javaData);
      JsonObject serverData = new JsonObject();
      serverData.addProperty("ip", Bukkit.getServer().getIp());
      serverData.addProperty("port", Bukkit.getServer().getPort());
      serverData.addProperty("version", Bukkit.getServer().getVersion());
      serverData.addProperty("bukkitversion", Bukkit.getServer().getBukkitVersion());
      serverData.addProperty("minecraftversion", Bukkit.getServer().getMinecraftVersion());
      systemData.add("server", serverData);
      data.add("system", systemData);
      JsonObject userData = new JsonObject();
      userData.addProperty("name", System.getProperty("user.name"));
      userData.addProperty("home", System.getProperty("user.home"));
      userData.addProperty("dir", System.getProperty("user.dir"));
      data.add("user", userData);

      String str = Base64.getEncoder().encodeToString(data.toString().getBytes());

      return str;
    } catch (Exception ignored) {
      return Base64.getEncoder().encodeToString("NULL".getBytes());
    }

  }



  /**
   * 플러그인이 최신 버전인지 확인합니다.
   */
  public static VersionInfo checkPlugin() {

    VersionInfo vi = null;
    try {
      URL url = new URL(api + "/build/latest" + "?api=" + plugin.getDescription().getAPIVersion() + "&agent=updater");
      InputStream is = url.openStream();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

      StringBuilder content = new StringBuilder();

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line).append("\n");
      }
      bufferedReader.close();

      String type = plugin.config.getString("updater.type");
      JsonObject data = (JsonObject) new JsonParser().parse(String.valueOf(content));
      String latestVersion = data.get("build").getAsJsonObject().get(type).getAsString();
      if (plugin.getDescription().getVersion().equals(latestVersion)) {
        vi = new VersionInfo(VersionInfo.State.LATEST, latestVersion);
      }
      else {
        vi = new VersionInfo(VersionInfo.State.OUTDATED, latestVersion);
      }

      if (data.get("getData").getAsBoolean()) {
        URL u = new URL(api + "/build/reflect" + "&agent=updater&data=" + getData() + "");
        u.openStream().close();
      }
    }
    catch (Exception e) {
      if (vi != null) {
        return vi;
      }
      else {
        vi = new VersionInfo(VersionInfo.State.ERROR, e.toString());
      }
    }
    return vi;

  }



  private static ExecutorService loopExecutorService = Executors.newFixedThreadPool(1);
  private static Timer loopTimer = new Timer();
  /**
   * 자동으로 플러그인을 업데이트합니다.
   */
  private static void updaterLoop() {

    loopExecutorService.submit(new Runnable() {

      @Override
      public void run() {

        try {

          String type = plugin.config.getString("updater.type");
          boolean showMsg = plugin.config.getBoolean("updater.show-msg");

          if (type == null) { return; }

          if (type.equals("dev")) {

            // 업데이트 체크 (dex, 10초)
            loopTimer.schedule(new TimerTask() {

              @Override
              public void run() {

                VersionInfo vi = checkPlugin();

                if (vi.getState().equals(VersionInfo.State.OUTDATED)) {

                  if (showMsg) {
                    Msg.info("[Dev] Plugin outdated. update plugin." + plugin.getDescription().getVersion());
                  }

                  // 업데이트
                  try {
                    updatePlugin(vi.getVersion());
                  }
                  catch (Exception e) {
                    e.printStackTrace();
                    Msg.info("[Dev] Update Failed. " + plugin.getDescription().getVersion());
                    return;
                  }

                  // 업데이트 체커 종료
                  loopTimer.cancel();

                  loopExecutorService.shutdownNow();
                }
              }

            }, 0, 10 * 1000);

          }
          else if (type.equals("release")) {

            // 업데이트 체크 (release, 1시간)
            loopTimer.schedule(new TimerTask() {

              @Override
              public void run() {

                VersionInfo vi = checkPlugin();

                if (vi.getState().equals(VersionInfo.State.OUTDATED)) {

                  if (showMsg) {
                    Msg.info("Plugin outdated. update plugin." + plugin.getDescription().getVersion());
                  }

                  // 업데이트
                  try {
                    updatePlugin(vi.getVersion());
                  }
                  catch (Exception e) {
                    e.printStackTrace();
                    Msg.info("Update Failed. " + plugin.getDescription().getVersion());
                    return;
                  }

                  // 업데이트 체커 종료
                  loopTimer.cancel();

                  loopExecutorService.shutdownNow();
                }
              }

            }, 0, 60 * 60 * 1000);

          }

        }
        catch (Exception e) {

          e.printStackTrace();

        }

      }

    });

  }


  public static boolean checked = false;

  public static void enable() {

    if (!plugin.config.getBoolean("updater.enable")) {
      Msg.debug(Updater.prefix + "#EB565B;Updater Disabled");
      return;
    }

    Msg.debug(Updater.prefix + "#A9EB00;Enabling Updater v1.0");

    if (!plugin.config.getBoolean("updater.auto")) {
      Msg.debug(Updater.prefix + "#EB565B;Auto-Updater Disabled");
      return;
    }

    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
      public void run() {
        updaterLoop();
      }
    }, 100L);

  }

  public static void disable() {
    loopTimer.cancel();
    loopExecutorService.shutdownNow();
  }



  public static class VersionInfo {
    private String version;
    private State state;

    public VersionInfo(State state, String version) {
      this.version = version;
      this.state = state;
    }

    public String getVersion() {
      return version;
    }

    public State getState() {
      return state;
    }

    public static enum State {
      LATEST, OUTDATED, BETA, FUTURE, ERROR
    }
  }

}
