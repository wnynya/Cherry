package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Msg;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;

public class WebSocketClient {

  public static void init() {

    try {
      CountDownLatch latch = new CountDownLatch(1);

      WebSocket ws = HttpClient
        .newHttpClient()
        .newWebSocketBuilder()
        .buildAsync(URI.create("ws://wnynya.com:5252/aaa"), new WebSocketConnection(latch))
        .join();

      ws.sendText("Hello!", true);

      latch.await();
    } catch (Exception e) {
      Msg.info(e.toString());
    }

  }

}
