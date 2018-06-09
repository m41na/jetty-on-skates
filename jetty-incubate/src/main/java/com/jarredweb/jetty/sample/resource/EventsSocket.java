package com.jarredweb.jetty.sample.resource;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class EventsSocket {
    
    @OnOpen
    public void onWebSocketConnect(Session sess){
        System.out.println("Socket connected: " + sess);
    }
    
    @OnMessage
    public void onWebSocketText(String message){
        System.out.println("Received TEXT message: " + message);
    }
    
    @OnClose
    public void onWebSocketClose(CloseReason reason){
        System.out.println("Socket closed: " + reason);
    }
    
    @OnError
    public void onWebSocketError(Throwable cause){
        cause.printStackTrace(System.err);
    }
}
