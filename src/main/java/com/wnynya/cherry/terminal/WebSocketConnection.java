package com.wnynya.cherry.terminal;

import com.wnynya.cherry.terminal.WebSocketClient;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class WebSocketConnection implements WebSocket.Listener {
  private final CountDownLatch latch;

  public WebSocketConnection(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onOpen(WebSocket webSocket) {
    WebSocketClient.isConnected = true;
    WebSocketClient.cancelConnectLoop();
    WebSocketClient.startDataLoop();
    WebSocket.Listener.super.onOpen(webSocket);
  }

  @Override
  public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
    WebSocketClient.receive(data.toString());
    return WebSocket.Listener.super.onText(webSocket, data, last);
  }

  @Override
  public void onError(WebSocket webSocket, Throwable error) {
    WebSocketClient.isConnected = false;
    WebSocketClient.startConnectLoop();
    WebSocketClient.cancelDataLoop();
    WebSocket.Listener.super.onError(webSocket, error);
  }

  public CompletionStage<?> onClose​(WebSocket webSocket, int statusCode, String reason) {
    WebSocketClient.isConnected = false;
    WebSocketClient.startConnectLoop();
    WebSocketClient.cancelDataLoop();
    return new CompletableFuture<Void>();
  }

}