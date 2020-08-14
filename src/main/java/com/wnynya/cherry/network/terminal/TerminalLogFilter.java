package com.wnynya.cherry.network.terminal;

import com.wnynya.cherry.network.Terminal;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class TerminalLogFilter implements Filter {

  public static boolean shutdown = false;

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
    if (!shutdown) {
      String message = event.getMessage().getFormattedMessage();
      long time = event.getTimeMillis();
      String name = event.getThreadName();
      try { TimeUnit.MICROSECONDS.sleep(1); } catch (InterruptedException ignored) { }
      Terminal.Message.log(name + "/" + time + "/" + message);
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
