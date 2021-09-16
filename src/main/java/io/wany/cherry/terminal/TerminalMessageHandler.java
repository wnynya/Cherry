package io.wany.cherry.terminal;

import io.wany.cherry.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.json.JSONObject;

import java.io.File;

public class TerminalMessageHandler {

  public static void onMessage(String s) {
    JSONObject object;
    object = new JSONObject(s);
    String event = object.getString("event");
    //String message = object.getString("message");
    String source = null;
    try {
      source = object.getString("source");
    }
    catch (Exception ignored) {
    }
    JSONObject data;
    try {
      data = object.getJSONObject("data");
    }
    catch (Exception e) {
      return;
    }

    try {

      switch (event) {
        case "init" -> {
          Terminal.debug(Terminal.PREFIX + "Send verification key");
          Terminal.webSocketClient.send("init", "init", new JSONObject("{\"key\":\"" + Terminal.key + "\"}"));
          Terminal.webSocketClient.ready = false;
        }
        case "init-data" -> {
          Terminal.debug(Terminal.PREFIX + "Connection established");
          Terminal.webSocketClient.ready = true;
          TerminalServerStatus.sendStatus("online");
          TerminalServerStatus.sendInfo();
          TerminalServerStatus.sendSystem();
          TerminalPlayers.sendPlayers();

          while(TerminalConsole.OfflineLogs.size() > 0) {
            TerminalConsole.Log log = TerminalConsole.OfflineLogs.get(0);
            TerminalConsole.onConsoleLog(log);
            TerminalConsole.OfflineLogs.remove(0);
          }
        }
        case "init-null" -> {
          Terminal.debug(Terminal.PREFIX + "Connection refused");
          Terminal.debug(Terminal.PREFIX + "Delete verification key");
          Config config = new Config(".key");
          config.set("key", null);
          config.file().deleteOnExit();
          Terminal.key = null;
          Terminal.webSocketClient.disconnect();
          Terminal.webSocketClient.ready = false;
          Terminal.webSocketClient.connected = false;
        }
        case "console-command" -> {
          String input = data.getString("command");
          TerminalConsole.onConsoleCommand(input);
        }
        case "console-tabcomplete" -> {
          String input = data.getString("command");
          TerminalConsole.onConsoleTabComplete(input, source);
        }
        case "fileexplorer-open" -> {
          String path = data.getString("path");
          File file = new File(path);
          TerminalFileExplorer.send(source, file);
        }
        case "fileexplorer-directory-open" -> {
          String path = data.getString("path");
          File file = new File(path);
          TerminalFileExplorer.sendDirectoryOpen(source, file);
        }
        case "fileexplorer-file-downstream-open" -> {
          String path = data.getString("path");
          File file = new File(path);
          String id = data.getString("id");
          TerminalFileExplorer.sendFileDownSteam(source, id, file);
        }
        case "fileexplorer-file-upstream-open" -> {
          String path = data.getString("path");
          File file = new File(path);
          String id = data.getString("id");
          TerminalFileExplorer.receiveFileUpStreamOpen(source, id, file);
        }
        case "fileexplorer-file-upstream-write" -> {
          String id = data.getString("id");
          String buffer = data.getString("buffer");
          TerminalFileExplorer.receiveFileUpStreamWrite(source, id, buffer);
        }
        case "fileexplorer-file-upstream-close" -> {
          String id = data.getString("id");
          TerminalFileExplorer.receiveFileUpStreamClose(source, id);
        }
        case "fileexplorer-delete" -> {
          String path = data.getString("path");
          File file = new File(path);
          TerminalFileExplorer.delete(file);
        }
        case "fileexplorer-rename" -> {
          String path = data.getString("path");
          File file = new File(path);
          String destPath = data.getString("dest");
          File dest = new File(destPath);
          TerminalFileExplorer.rename(file, dest);
        }
        case "fileexplorer-create-file" -> {
          String path = data.getString("path");
          File file = new File(path);
          TerminalFileExplorer.createFile(file);
        }
        case "fileexplorer-create-directory" -> {
          String path = data.getString("path");
          File file = new File(path);
          TerminalFileExplorer.createDirectory(file);
        }
        case "worlds-world-gamerule" -> {
          String worldName = data.getString("world");
          World world = Bukkit.getWorld(worldName);
          String key = data.getString("gamerule");
          GameRule<?> gameRule = GameRule.getByName(key);
          String val = data.getString("value");
          String type = data.getString("type");
          Object value = null;
          if (type.equals("boolean")) {
            value = Boolean.parseBoolean(val);
          }
          if (type.equals("number")) {
            value = Integer.parseInt(val);
          }
          TerminalWorlds.setGameRule(world, gameRule, value);
        }
      }
    }
    catch (Exception ignored) {
    }

  }

}
