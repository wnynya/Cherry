package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Updater {

  /**
   * 플러그인이 최신 버전인지 확인합니다.
   */
  public static VersionInfo checkCherry() {
    StringBuilder content = new StringBuilder();
    try {
      String type = Cherry.config.getString("updater.type");
      URL url = new URL("http://cherry.wnynya.com/check/" + type + "?a=" + Cherry.getPlugin().getDescription().getAPIVersion() + "&s=updater");

      URLConnection urlConnection = url.openConnection();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line).append("\n");
      }
      bufferedReader.close();

      JSONParser parser = new JSONParser();
      Object obj = parser.parse(String.valueOf(content));
      JSONObject data = (JSONObject) obj;

      String latestVersion = data.get("latest").toString();
      if (Cherry.getPlugin().getDescription().getVersion().equals(latestVersion)) {
        return new VersionInfo(VersionInfo.State.LATEST, "");
      }
      else {
        return new VersionInfo(VersionInfo.State.OUTDATED, data.get("latest").toString());
      }
    }
    catch (Exception e) {
      return new VersionInfo(VersionInfo.State.ERROR, e.toString());
    }
  }

  /**
   * 플러그인 파일을 다운로드합니다.
   *
   * @param version 다운로드할 버전
   */
  public static void downloadCherry(String version) {
    File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
    try {
      String type = Cherry.config.getString("updater.type");
      URL url = new URL("http://cherry.wnynya.com/get/" + version + "?s=updater");
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
      e.printStackTrace();
    }

  }

  /**
   * 플러그인을 업데이트합니다.
   *
   * @param version 업데이트할 버전
   */
  public static boolean updateCherry(String version) {
    downloadCherry(version);
    File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
    if (!file.exists()) {
      return false;
    }

    Cherry.unload();

    File plugins = new File("plugins");
    File cherryJar = new File(plugins, "Cherry.jar");

    try {
      File origin = new File(plugins, Cherry.fileName);

      byte[] data = Files.readAllBytes(file.toPath());
      Path path = cherryJar.toPath();

      if (origin.exists()) {
        if (!origin.delete()) {
          return false;
        }
      }

      Files.write(path, data);

    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    file.delete();

    Cherry.load(cherryJar);
    return true;
  }

  public static void init() {
    loop();
  }

  public static void loop() {
    String type = Cherry.config.getString("updater.type");
    Timer timer = new Timer();

    if (Cherry.config.getBoolean("updater.auto")) {

      if (type.equals("release")) {
        timer.schedule(new TimerTask() {
          public void run() {
            if (Cherry.config.getBoolean("updater.show-msg")) {
              Msg.info("Check for plugin updates.");
            }
            VersionInfo vi = checkCherry();
            if (vi.getState().equals(VersionInfo.State.OUTDATED)) {
              if (Cherry.config.getBoolean("updater.show-msg")) {
                Msg.info("Plugin outdated. update plugin.");
              }
              updateCherry(vi.getVersion());
              timer.cancel();
              if (Cherry.config.getBoolean("updater.show-msg")) {
                Msg.info("Update Complete.");
              }
            }
            File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
            if (file.exists()) {
              file.delete();
            }
          }
        }, 0, 60 * 60 * 1000);
      }

      else if (type.equals("dev")) {
        timer.schedule(new TimerTask() {
          public void run() {
            //Msg.info("Check for plugin updates.");
            VersionInfo vi = checkCherry();
            if (vi.getState().equals(VersionInfo.State.OUTDATED)) {
              if (Cherry.config.getBoolean("updater.show-msg")) {
                Msg.info("[Dev] Plugin outdated. update plugin.");
              }
              Cherry.reloading = true;
              updateCherry(vi.getVersion());
              timer.cancel();
              Cherry.reloading = false;
              if (Cherry.config.getBoolean("updater.show-msg")) {
                Msg.info("[Dev] Update Complete.");
              }
            }
            File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
            if (file.exists()) {
              file.delete();
            }
          }
        }, 0, 10 * 1000);
      }

    }

  }

  public static class VersionInfo {
    private String version;
    private State state;

    VersionInfo(State state, String version) {
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
