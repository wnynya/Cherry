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

  public static List<Log> OfflineLogs = new ArrayList<>();

  public static class Log {
    public String message;
    public long time;
    public String level;
    public String thread;
    public String logger;
    public Log (String message, long time, String level, String thread, String logger) {
      this.message = message;
      this.time = time;
      this.level = level;
      this.thread = thread;
      this.logger = logger;
    }
  }

  public static void onConsoleLog(Log log) {
    String event = "console-log";
    String message = "[" + log.level + "]: " + log.message;
    JSONObject data = new JSONObject();
    data.put("message", log.message);
    data.put("time", log.time);
    data.put("level", log.level);
    data.put("thread", log.thread);
    data.put("logger", log.logger);
    Terminal.send(event, message, data);
  }

  public static void onConsoleCommand(String input) {
    onConsoleLog(new Log("> " + input, System.currentTimeMillis(), "INFO", "ConsoleCommand", "ConsoleCommand"));
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
    Terminal.debug(Terminal.PREFIX + "LogFilter added");
  }

  public static void onDisable() {
    logFilter.close();
  }

}
