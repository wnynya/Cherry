package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Msg;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class WebSocketConnection implements WebSocket.Listener {
  private final CountDownLatch latch;

  public WebSocketConnection(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onOpen(WebSocket webSocket) {
    System.out.println("onOpen using subprotocol " + webSocket.getSubprotocol());
    WebSocket.Listener.super.onOpen(webSocket);
  }

  @Override
  public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
    System.out.println("onText received " + data);
    latch.countDown();
    return WebSocket.Listener.super.onText(webSocket, data, last);
  }

  @Override
  public void onError(WebSocket webSocket, Throwable error) {
    System.out.println("Bad day! " + webSocket.toString());
    WebSocket.Listener.super.onError(webSocket, error);
  }
}