package io.wany.cherry.terminal;

import io.wany.cherry.Cherry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TerminalConsole {

  public static TerminalConsoleLogFilter logFilter;

  public static void onConsoleLog(String msg, long time, String level, String thread, String logger) {
    String event = "console-log";
    String message = "[" + level + "]: " + msg;
    JSONObject data = new JSONObject();
    data.put("message", msg);
    data.put("time", time);
    data.put("level", level);
    data.put("thread", thread);
    data.put("logger", logger);
    Terminal.send(event, message, data);
  }

  public static void onConsoleCommand(String input) {
    onConsoleLog("> " + input, System.currentTimeMillis(), "INFO", "ConsoleCommand", "ConsoleCommand");
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), input));
  }

  public static void onConsoleTabComplete(String input, String source) {
    String[] inputArgs = input.split(" ");
    String[] inputs = new String[inputArgs.length + 1];
    System.arraycopy(inputArgs, 0, inputs, 0, inputArgs.length);
    inputs[inputArgs.length] = "";
    String command = inputs[0];
    PluginCommand pluginCommand = Bukkit.getServer().getPluginCommand(command);
    String[] args = new String[inputs.length - 1];
    System.arraycopy(inputs, 1, args, 0, inputs.length - 1);
    List<String> comp = new ArrayList<>();
    if (pluginCommand != null) {
      TabCompleter completer = pluginCommand.getTabCompleter();
      if (completer != null) {
        comp = completer.onTabComplete(Bukkit.getConsoleSender(), pluginCommand, pluginCommand.getLabel(), args);
      }
    }
    String event = "console-tabcomplete";
    String message = "Console command tab-complete";
    JSONObject data = new JSONObject();
    data.put("command", command);
    JSONArray completions = new JSONArray();
    completions.putAll(comp);
    data.put("completions", completions);
    Terminal.send(event, message, data, source);
  }

  public static void onLoad() {
    Logger logger = (Logger) LogManager.getRootLogger();
    logFilter = new TerminalConsoleLogFilter();
    logger.addFilter(logFilter);
  }

  public static void onDisable() {
    logFilter.close();
  }

}
