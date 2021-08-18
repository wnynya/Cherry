package io.wany.cherry.amethyst;

import io.wany.cherry.Cherry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class Songs {

  private static final ExecutorService songsExecutorService = Executors.newFixedThreadPool(1);
  private static final Timer songsLoopTimer = new Timer();
  public static List<String> list = new ArrayList<>();

  public static File download(String name) throws IOException {
    return download(name, false);
  }

  public static File download(String name, boolean force) throws IOException {
    try {
      if (!name.endsWith(".nbs")) {
        name += ".nbs";
      }
      File dir = new File(Cherry.DIR + "/songs");
      dir.mkdirs();
      File file = new File(dir + "/" + name);
      if (!force && file.exists()) {
        return file;
      }
      else {
        file.delete();
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdirs();
        }
        file.createNewFile();
      }

      URL url = new URL(tranformStyle("https://cucumbery.com/api/songs/" + name + "/download"));
      HttpURLConnection connection = (HttpURLConnection) (url).openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Cherry");
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "Cp1252"));
      FileOutputStream fis = new FileOutputStream(file);
      OutputStreamWriter osw = new OutputStreamWriter(fis, "Cp1252");
      BufferedWriter writer = new BufferedWriter(osw);
      char[] buffer = new char[1024];
      int count = 0;
      while ((count = bufferedReader.read(buffer, 0, 1024)) != -1) {
        writer.write(buffer, 0, count);
      }
      writer.close();
      osw.close();
      fis.close();
      bufferedReader.close();

      return file;
    }
    catch (Exception e) {
      throw e;
    }
  }

  public static void updateList() {
    try {
      URL url = new URL("https://cucumbery.com/api/songs");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Cherry");
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
        JSONArray array = (JSONArray) object.get("data");
        list.clear();
        for (int i = 0; i < array.size(); i++) {
          String songName = array.get(i).toString();
          list.add(songName);
        }
      }
      connection.disconnect();
    }
    catch (Exception ignored) {

    }
  }

  public static String getRandomSongName() {
    return list.get(new Random().nextInt(list.size()));
  }

  public static String tranformStyle(String source) throws UnsupportedEncodingException {
    char[] arr = source.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arr.length; i++) {
      char temp = arr[i];
      if (isSpecial(temp)) {
        sb.append(URLEncoder.encode("" + temp, "UTF-8"));
        continue;
      }
      sb.append(arr[i]);
    }
    String re = sb.toString();
    re = re.replace(" ", "%20");
    return re;
  }

  public static boolean isSpecial(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.HANGUL_JAMO || ub == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_A || ub == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_B || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO || ub == Character.UnicodeBlock.HANGUL_SYLLABLES || ub == Character.UnicodeBlock.HIRAGANA || ub == Character.UnicodeBlock.KATAKANA || ub == Character.UnicodeBlock.KANA_EXTENDED_A || ub == Character.UnicodeBlock.KANA_SUPPLEMENT || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_E || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_G || ub == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT || ub == Character.UnicodeBlock.CJK_STROKES || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

  public static void onEnable() {
    songsExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        songsLoopTimer.schedule(new TimerTask() {
          @Override
          public void run() {
            updateList();
          }
        }, 0, 2000);
      }
    });
  }

  public static void onDisable() {
    songsLoopTimer.cancel();
    songsExecutorService.shutdownNow();
  }

}
