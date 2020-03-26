package com.wnynya.cherry.network.terminal;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.Terminal;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class WebSocketConnection implements java.net.http.WebSocket.Listener {
  private final CountDownLatch latch;

  public WebSocketConnection(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onOpen(java.net.http.WebSocket webSocket) {
    Msg.debug("[CWS] Successfully connected to CWS server");
    WebSocket.isConnected = true;
    Terminal.connected = true;
    java.net.http.WebSocket.Listener.super.onOpen(webSocket);
  }

  @Override
  public CompletionStage<?> onText(java.net.http.WebSocket webSocket, CharSequence data, boolean last) {
    WebSocket.receive(data.toString());
    return java.net.http.WebSocket.Listener.super.onText(webSocket, data, last);
  }

  @Override
  public void onError(java.net.http.WebSocket webSocket, Throwable error) {
    Msg.debug("[CWS] CWS disconnected: E: " + error.getMessage());
    WebSocket.isConnected = false;
    Terminal.connected = false;
    java.net.http.WebSocket.Listener.super.onError(webSocket, error);
  }

  public CompletionStage<?> onClose​(java.net.http.WebSocket webSocket, int statusCode, String reason) {
    Msg.debug("[CWS] CWS disconnected");
    WebSocket.isConnected = false;
    Terminal.connected = false;
    return new CompletableFuture<Void>();
  }

}