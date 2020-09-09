package com.wnynya.cherry.network.terminal;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.Terminal;
import org.java_websocket.client.*;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClient8 extends WebSocketClient {

  public WebSocketClient8 ( URI uri ) { super(uri); }

  @Override
  public void onOpen( ServerHandshake handshakeData ) { Terminal.open(); }

  @Override
  public void onMessage( String message ) { Terminal.rawMessageHandler(message); }

  @Override
  public void onClose( int code, String reason, boolean remote ) { Terminal.close(); }

  @Override
  public void onError( Exception ex ) {
    Msg.error(ex.toString());
  }

}
