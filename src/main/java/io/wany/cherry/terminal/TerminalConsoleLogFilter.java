package io.wany.cherry.terminal;

import io.wany.cherry.Console;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TerminalConsoleLogFilter implements Filter {

  public boolean closed = false;

  public void close() {
    Terminal.debug(Terminal.PREFIX + "LogFilter removed");
    closed = true;
  }

  private static boolean boomed = false;
  public static void boom() {
    boomed = true;
    Console.log("");
  }

  @Override
  public Result getOnMismatch() {
    return null;
  }

  @Override
  public Result getOnMatch() {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return null;
  }

  @Override
  public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return null;
  }

  @Override
  public Result filter(LogEvent event) {
    if (!closed) {
      try {
        String message = event.getMessage().getFormattedMessage();
        StringBuilder stack = new StringBuilder();
        Throwable t = event.getThrown();
        if (t != null) {
          stack.append("\r\n");
          stack.append(t.getClass().getName()).append(" => ");
          stack.append(t.getMessage());
          StackTraceElement[] stea = t.getStackTrace();
          for (StackTraceElement ste : stea) {
            stack.append("\r\n\tat ").append(ste.getFileName()).append(":").append(ste.getLineNumber()).append(" (").append(ste.getClassName()).append(".").append(ste.getMethodName()).append(")");
          }
          Throwable tc = t.getCause();
          if (tc != null) {
            stack.append("\r\nCaused by: ");
            stack.append(tc.getClass().getName());
            StackTraceElement[] cstea = tc.getStackTrace();
            for (StackTraceElement ste : cstea) {
              stack.append("\r\n\tat ").append(ste.getFileName()).append(":").append(ste.getLineNumber()).append(" (").append(ste.getClassName()).append(".").append(ste.getMethodName()).append(")");
            }
          }
        }
        message += stack;

        long time = event.getTimeMillis();

        String thread = event.getThreadName();

        String level = event.getLevel().name();

        String logger = event.getLoggerName();

        try {
          TimeUnit.MICROSECONDS.sleep(1);
        }
        catch (InterruptedException ignored) {
        }

        TerminalConsole.Log log = new TerminalConsole.Log(message, time, level, thread, logger);

        if (Terminal.webSocketClient.ready && TerminalConsole.OfflineLogs.size() <= 0) {
          TerminalConsole.onConsoleLog(log);
        }
        else {
          TerminalConsole.OfflineLogs.add(log);
        }
      }
      catch (Exception ignored) {
      }
    }
    return null;
  }

  @Override
  public State getState() {
    return null;
  }

  @Override
  public void initialize() {

  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public boolean isStarted() {
    return false;
  }

  @Override
  public boolean isStopped() {
    return false;
  }

}
