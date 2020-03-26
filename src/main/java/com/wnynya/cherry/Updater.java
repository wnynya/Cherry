package com.wnynya.cherry;

import com.wnynya.cherry.amethyst.PluginLoader;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Updater {



  private static ExecutorService updaterExecutorService = Executors.newFixedThreadPool(1);
  private static boolean updating = false;
  /**
   * 플러그인을 업데이트합니다.
   *
   * @param version 업데이트할 버전
   */
  public static void updateCherry(String version) throws Exception {

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
          downloadCherry(version);

          // 다운로드한 파일 확인
          File tempFile = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");
          if (!tempFile.exists()) {
            throw new Exception(tempFile.toString() + " 파일을 찾을 수 없습니다.");
          }

          // 플러그인 언로드
          PluginLoader.unload();

          // 파일 교체
          File pluginsDir = new File("plugins");
          File pluginFile = new File(pluginsDir, "Cherry.jar");
          File originalFile = new File(pluginsDir , Cherry.file.getName());

          byte[] data = Files.readAllBytes(tempFile.toPath());
          Path path = pluginFile.toPath();

          if (originalFile.exists()) {
            //Files.delete(origin.toPath()); <- 이게 좋음
            originalFile.delete();
          }

          Files.write(path, data);

          // 임시 파일 삭제
          tempFile.delete();

          // 플러그인 로드
          PluginLoader.load(pluginFile);

          updating = false;
        }
        catch (Exception e) {
          try {
            throw e;
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
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
  private static void downloadCherry(String version) throws IOException {

    try {
      File file = new File(Cherry.getPlugin().getDataFolder() + "/Cherry.jar.temp");

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
   * 플러그인이 최신 버전인지 확인합니다.
   */
  public static VersionInfo checkCherry() {

    VersionInfo vi;
    try {
      String type = Cherry.config.getString("updater.type");
      URL url = new URL("http://cherry.wnynya.com/build/check/" + type + "?a=" + Cherry.getPlugin().getDescription().getAPIVersion() + "&s=updater");
      InputStream is = url.openStream();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

      StringBuilder content = new StringBuilder();

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line).append("\n");
      }
      bufferedReader.close();

      JSONObject data = (JSONObject) new JSONParser().parse(String.valueOf(content));
      String latestVersion = data.get("latest").toString();
      if (Cherry.getPlugin().getDescription().getVersion().equals(latestVersion)) {
        vi = new VersionInfo(VersionInfo.State.LATEST, data.get("latest").toString());
      }
      else {
        vi = new VersionInfo(VersionInfo.State.OUTDATED, data.get("latest").toString());
      }
    }
    catch (Exception e) {
      //e.printStackTrace();
      vi = new VersionInfo(VersionInfo.State.ERROR, e.toString());
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

          String type = Cherry.config.getString("updater.type");
          boolean showMsg = Cherry.config.getBoolean("updater.show-msg");

          if (type == null) { return; }

          if (type.equals("dev")) {

            // 업데이트 체크 (dex, 10초)
            loopTimer.schedule(new TimerTask() {

              @Override
              public void run() {

                VersionInfo vi = checkCherry();

                if (vi.getState().equals(VersionInfo.State.OUTDATED)) {

                  if (showMsg) {
                    Msg.info("[Dev] Plugin outdated. update plugin." + Cherry.getPlugin().getDescription().getVersion());
                  }

                  // 업데이트
                  try {
                    updateCherry(vi.getVersion());
                  }
                  catch (Exception e) {
                    e.printStackTrace();
                    Msg.info("[Dev] Update Failed. " + Cherry.getPlugin().getDescription().getVersion());
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

                VersionInfo vi = checkCherry();

                if (vi.getState().equals(VersionInfo.State.OUTDATED)) {

                  if (showMsg) {
                    Msg.info("Plugin outdated. update plugin." + Cherry.getPlugin().getDescription().getVersion());
                  }

                  // 업데이트
                  try {
                    updateCherry(vi.getVersion());
                  }
                  catch (Exception e) {
                    e.printStackTrace();
                    Msg.info("Update Failed. " + Cherry.getPlugin().getDescription().getVersion());
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

    if (!Cherry.config.getBoolean("updater.enable")) {
      Msg.debug("[UPDATER] Updater Disabled");
      return;
    }

    if (!Cherry.config.getBoolean("updater.auto")) {
      Msg.debug("[UPDATER] Auto-Updater Disabled");
      return;
    }

    Msg.debug("[UPDATER] Enabling Updater v1.0");

    Bukkit.getScheduler().runTaskLater(Cherry.getPlugin(), new Runnable() {
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
