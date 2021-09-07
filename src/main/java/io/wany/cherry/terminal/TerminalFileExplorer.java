package io.wany.cherry.terminal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TerminalFileExplorer {

  private static final HashMap<String, ReceiveFileUpStream> receiveFileUpStreams = new HashMap<>();

  public static void send(String target, File path) {
    if (!path.exists()) {
      sendError(target, "File or directory not exist");
      return;
    }
    if (path.isDirectory()) {
      sendDirectoryOpen(target, path);
    }
    else {
      askFileDownSteam(target, path);
    }
  }

  public static void sendError(String target, String message) {
    String event = "fileexplorer-error";
    JSONObject data = new JSONObject();
    Terminal.send(event, message, data, target);
  }

  public static void sendDirectoryOpen(String target, File dir) {
    if (!dir.exists()) {
      sendError(target, "Directory not exist");
      return;
    }
    if (!dir.isDirectory()) {
      sendError(target, "Path is not a directory");
      return;
    }
    String event = "fileexplorer-directory-open";
    String message = "open directory";
    JSONObject data = new JSONObject();
    data.put("path", dir.getAbsolutePath().replace("\\", "/"));

    JSONArray dataArray = new JSONArray();

    JSONObject thisDir = getFileData(dir);
    thisDir.put("name", ".");
    dataArray.put(thisDir);
    if (dir.getParentFile() != null) {
      JSONObject parentDir = getFileData(dir.getParentFile());
      parentDir.put("name", "..");
      dataArray.put(parentDir);
    }
    else {
      JSONObject parentDir = new JSONObject();
      parentDir.put("path", "root");
      parentDir.put("name", "..");
      parentDir.put("size", 0);
      parentDir.put("type", "dir");
      parentDir.put("hidden", false);
      parentDir.put("read", dir.canRead());
      parentDir.put("write", dir.canWrite());
      parentDir.put("execute", dir.canExecute());
      dataArray.put(parentDir);
    }

    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        JSONObject fileData = getFileData(file);
        dataArray.put(fileData);
      }
    }

    data.put("files", dataArray);
    Terminal.send(event, message, data, target);
  }

  public static JSONObject getFileData(File file) {
    JSONObject fileData = new JSONObject();
    fileData.put("path", file.getAbsolutePath().replace("\\", "/"));
    fileData.put("name", file.getName());
    fileData.put("size", file.length());
    if (file.isDirectory()) {
      fileData.put("type", "directory");
    }
    else if (file.isFile()) {
      fileData.put("type", "file");
    }
    fileData.put("hidden", file.isHidden());
    fileData.put("read", file.canRead());
    fileData.put("write", file.canWrite());
    fileData.put("execute", file.canExecute());
    return fileData;
  }

  public static void askFileDownSteam(String target, File file) {
    if (!file.exists()) {
      sendError(target, "File not exist");
      return;
    }
    if (!file.isFile()) {
      sendError(target, "Path is not a file");
      return;
    }

    String event = "fileexplorer-file-downstream-ask";
    String message = "file down stream ask";
    JSONObject data = new JSONObject();

    data.put("path", file.getAbsolutePath());
    data.put("name", file.getName());

    Terminal.send(event, message, data, target);
  }

  public static void sendFileDownSteam(String target, String id, File file) {
    if (!file.exists()) {
      sendError(target, "File not exist");
      return;
    }
    if (!file.isFile()) {
      sendError(target, "Path is not a file");
      return;
    }
    if (file.length() > (1024 * 1024 * 20)) {
      sendError(target, "File size > 20MB");
      return;
    }

    ExecutorService sendFileStreamExecutorService = Executors.newFixedThreadPool(1);
    sendFileStreamExecutorService.submit(() -> {
      sendFileDownStreamOpen(target, id, file);
      int bufferSize = 100 * 1024;
      BufferedInputStream bufferedInputStream;
      try {
        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        int index = 0;
        while (bufferedInputStream.available() > 0) {
          byte[] bytes = new byte[Math.min(bufferedInputStream.available(), bufferSize)];
          bufferedInputStream.read(bytes, 0, bytes.length);
          char[] hexArray = "0123456789ABCDEF".toCharArray();
          char[] hexChars = new char[bytes.length * 2];
          for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
          }
          String buffer = Base64.getEncoder().encodeToString(bytes);
          int size = index * bufferSize + bytes.length;
          sendFileDownStreamWrite(target, id, size, buffer);
          TimeUnit.MILLISECONDS.sleep(20);
          index++;
        }
      }
      catch (Exception e) {
        sendFileStreamExecutorService.shutdown();
      }
      finally {
        try {
          sendFileDownStreamClose(target, id);
          sendFileStreamExecutorService.shutdown();
        }
        catch (Exception e) {
          sendFileStreamExecutorService.shutdown();
        }
      }
    });
  }

  public static void sendFileDownStreamOpen(String target, String id, File file) {
    String event = "fileexplorer-file-downstream-open";
    String message = "file down stream open";
    JSONObject data = new JSONObject();
    data.put("id", id);
    data.put("name", file.getName());
    data.put("path", file.getAbsolutePath().replace("\\", "/"));
    data.put("totalsize", file.length());
    try {
      data.put("mime", Files.probeContentType(file.toPath()) + "");
    }
    catch (IOException ignored) {
    }
    Terminal.send(event, message, data, target);
  }

  public static void sendFileDownStreamWrite(String target, String id, int size, String buffer) {
    String event = "fileexplorer-file-downstream-write";
    String message = "file down stream write";
    JSONObject data = new JSONObject();
    data.put("id", id);
    data.put("size", size);
    data.put("buffer", buffer);
    Terminal.send(event, message, data, target);
  }

  public static void sendFileDownStreamClose(String target, String id) {
    String event = "fileexplorer-file-downstream-close";
    String message = "file down stream close";
    JSONObject data = new JSONObject();
    data.put("id", id);
    Terminal.send(event, message, data, target);
  }

  public static void receiveFileUpStreamOpen(String source, String id, File file) {
    if (receiveFileUpStreams.containsKey(id)) {
      return;
    }
    try {
      PrintWriter writer = new PrintWriter(file);
      writer.print("");
      writer.close();
    }
    catch (Exception ignored) {
    }
    ReceiveFileUpStream receiveFileUpStream = new ReceiveFileUpStream(id, file);
    receiveFileUpStreams.put(id, receiveFileUpStream);
  }

  public static void receiveFileUpStreamWrite(String source, String id, String buffer) {
    ReceiveFileUpStream receiveFileUpStream = receiveFileUpStreams.get(id);
    if (receiveFileUpStream == null) {
      return;
    }
    int size = buffer.length();
    byte[] bufferData = new byte[size / 2];
    for (int n = 0; n < size; n += 2) {
      bufferData[n / 2] = (byte) ((Character.digit(buffer.charAt(n), 16) << 4) + Character.digit(buffer.charAt(n + 1), 16));
    }
    receiveFileUpStream.write(bufferData);
  }

  public static void receiveFileUpStreamClose(String source, String id) {
    ReceiveFileUpStream receiveFileUpStream = receiveFileUpStreams.get(id);
    if (receiveFileUpStream == null) {
      return;
    }
    receiveFileUpStreams.remove(id);
  }

  public static void delete(File file) {
    if (!file.exists()) {
      return;
    }
    File[] files = file.listFiles();
    if (files != null) {
      for (File f : files) {
        delete(f);
      }
    }
    file.delete();
  }

  public static void rename(File file, File dest) {
    file.renameTo(dest);
  }

  public static void createFile(File file) {
    try {
      file.createNewFile();
    }
    catch (Exception ignored) {
    }
  }

  public static void createDirectory(File file) {
    try {
      file.mkdirs();
    }
    catch (Exception ignored) {
    }
  }

  public static class ReceiveFileUpStream {

    public final String id;
    public final File file;
    OutputStream stream = null;

    public ReceiveFileUpStream(String id, File file) {
      this.id = id;
      this.file = file;
    }

    public void write(byte[] data) {
      try {
        this.stream = new FileOutputStream(this.file, true);
        this.stream.write(data);
        this.stream.close();
      }
      catch (Exception ignored) {
      }
    }

  }

}
