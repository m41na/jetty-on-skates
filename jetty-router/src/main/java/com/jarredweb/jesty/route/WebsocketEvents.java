package com.jarredweb.jesty.route;

import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.Session;

public interface WebsocketEvents {
    
    void onConnect(Session sess) throws IOException;
    
    void onMessage(String message, Session sess) throws IOException;
    
    void onClose(CloseReason reason, Session session) throws IOException;
    
    void onError(Throwable cause);
    
    String dateTime();
    
    <T>void send(T message);
}
