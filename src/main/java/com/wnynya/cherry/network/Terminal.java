package com.wnynya.cherry.network;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.terminal.TerminalLogFilter;
import com.wnynya.cherry.network.terminal.WebSocketClient8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Terminal {

  public static boolean connected = false;
  private static WebSocketClient8 websocket;

  public static void open() {

    Msg.info("open conn");

    Terminal.connected = true;
    websocket.send("hello");

  }

  public static void close() {

    Msg.info("close conn");

    Terminal.connected = false;

  }

  private static void send(String msg) {
    if (websocket.isOpen()) {
      websocket.send(msg);
    }
  }

  public static class Message {

    public static void log (String msg) {
      if (!connected) { return; }
      Terminal.send(msg);
    }

  }

  /* Connect to websocket server */
  private static final ExecutorService connectExecutorService = Executors.newFixedThreadPool(1);
  private static Timer connectTimer;
  private static void connect() {
    connectExecutorService.submit(new Runnable() {
      @Override
      public void run() {
        connectTimer = new Timer();
        connectTimer.schedule(new TimerTask() {
          public void run() {
            if (!Terminal.connected) {
              try {
                websocket = new WebSocketClient8(new URI("ws://cherry.wnynya.com:99/"));
                websocket.connect();
              } catch (Exception ignored) {}
            }
          }
        }, 0, 2 * 1000);
      }
    });
  }

  public static void enable() {

    Terminal.connect();

    Logger logger = (Logger) LogManager.getRootLogger();
    logger.addFilter(new TerminalLogFilter());

  }

  public static void disable() {

    TerminalLogFilter.shutdown = true;

    connectTimer.cancel();
    connectExecutorService.shutdown();

    websocket.close();

  }

}
