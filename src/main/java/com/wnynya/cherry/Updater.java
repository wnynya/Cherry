package com.wnynya.cherry;

import com.wnynya.cherry.terminal.WebSocketClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Updater {

  private static boolean updated = false;

  /**
   * 플러그인이 최신 버전인지 확인합니다.
   */
  public static VersionInfo checkCherry() {

    try {

      String type = Cherry.config.getString("updater.type");
      URL url = new URL("http://cherry.wnynya.com/build/check/" + type + "?a=" + Cherry.getPlugin().getDescription().getAPIVersion() + "&s=updater");

      URLConnection urlConnection = url.openConnection();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

      StringBuilder content = new StringBuilder();
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        content.append(line).append("\n");
      }

      bufferedReader.close();

      JSONObject data  = (JSONObject) new JSONParser().parse(String.valueOf(content));

      String latestVersion = data.get("latest").toString();

      if (Cherry.getPlugin().getDescription().getVersion().equals(latestVersion)) {
        return new VersionInfo(VersionInfo.State.LATEST, data.get("latest").toString());
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
  public static void downloadCherry(String version) throws IOException {

    File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");

    try {

      String type = Cherry.config.getString("updater.type");

      URL url = new URL("http://cherry.wnynya.com/build/get/" + type + "/" + version + "?s=updater");

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

  /**
   * 플러그인을 업데이트합니다.
   *
   * @param version 업데이트할 버전
   */
  public static void updateCherry(String version) throws Exception {

    WebSocketClient.Message.status(WebSocketClient.Status.UPDATE);

    try {
      downloadCherry(version);
    }
    catch (Exception e) {
      throw e;
    }

    File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
    if (!file.exists()) {
      throw new Exception(file.toString() + " 파일을 찾을 수 없습니다.");
    }

    Cherry.unload();
    File plugins = new File("plugins");
    File cherryJar = new File(plugins, "Cherry.jar");

    try {
      File origin = new File(plugins, Cherry.fileName);

      byte[] data = Files.readAllBytes(file.toPath());
      Path path = cherryJar.toPath();

      if (origin.exists()) {
        //Files.delete(origin.toPath()); <- 이게 좋음
        origin.delete();
      }

      Files.write(path, data);
    }
    catch (Exception e) {
      throw e;
    }

    updated = true;

    file.delete();

    timer.cancel();

    Cherry.load(cherryJar);
  }

  public static void init() {
    loop();
  }


  private static Timer timer = new Timer();

  public static void loop() {
    String type = Cherry.config.getString("updater.type");

    boolean showMsg = Cherry.config.getBoolean("updater.show-msg");

    if (Cherry.config.getBoolean("updater.auto")) {

      String ver = Cherry.getPlugin().getDescription().getVersion();

      if (type.equals("release")) {
        timer.schedule(new TimerTask() {
          public void run() {
            if (showMsg) { Msg.info("Check for plugin updates."); }

            VersionInfo vi = checkCherry();

            if (vi.getState().equals(VersionInfo.State.OUTDATED)) {

              if (showMsg) { Msg.info("Plugin outdated. update plugin."); }
              try {
                updateCherry(vi.getVersion());
              }
              catch (Exception e) {
                if (showMsg) { Msg.info("Update Failed."); }
              }
              timer.cancel();

              if (showMsg) { Msg.info("Update Complete."); }

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

            if (!updated) {

              VersionInfo vi = checkCherry();

              if (vi.getState().equals(VersionInfo.State.OUTDATED)) {

                if (showMsg) {
                  Msg.info("[Dev] Plugin outdated. update plugin.");
                }
                try {
                  updateCherry(vi.getVersion());
                }
                catch (Exception e) {
                  e.printStackTrace();
                  if (showMsg) {
                    Msg.info("[Dev] Update Failed.");
                  }
                  return;
                }
                timer.cancel();

                if (showMsg) {
                  Msg.info("[Dev] Update Complete.");
                }

              }

              File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
              if (file.exists()) {
                file.delete();
              }

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