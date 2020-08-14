package com.wnynya.cherry.network.terminal;

import com.wnynya.cherry.network.Terminal;
import org.java_websocket.client.*;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClient8 extends WebSocketClient {

  public WebSocketClient8 ( URI uri ) {
    super(uri);
  }

  @Override
  public void onOpen( ServerHandshake handshakedata ) {
    Terminal.open();
  }

  @Override
  public void onMessage( String message ) {
    System.out.println( "received: " + message );
  }

  @Override
  public void onClose( int code, String reason, boolean remote ) {
    Terminal.close();
    System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
  }

  @Override
  public void onError( Exception ex ) {
    ex.printStackTrace();
    // if the error is fatal then onClose will be called additionally
  }

}
