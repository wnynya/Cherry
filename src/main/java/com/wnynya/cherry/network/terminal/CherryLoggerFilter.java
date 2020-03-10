package com.wnynya.cherry.network.terminal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CherryLoggerFilter implements Filter {

  SimpleDateFormat formatter = new SimpleDateFormat("dd-MM hh:mm:ss");

  public ArrayList<String> messages = new ArrayList<>();

  @Override
  public Result getOnMismatch() {
    return null;
  }

  @Override
  public Result getOnMatch() {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object... objects) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
    return null;
  }

  @Override
  public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, org.apache.logging.log4j.message.Message message, Throwable throwable) {
    return null;
  }

  @Override
  public Result filter(LogEvent logEvent) {
    String message = logEvent.getMessage().getFormattedMessage();
    try {
      TimeUnit.MICROSECONDS.sleep(1);
    }
    catch (InterruptedException e) {
    }
    WebSocketClient.Message.output(message);
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