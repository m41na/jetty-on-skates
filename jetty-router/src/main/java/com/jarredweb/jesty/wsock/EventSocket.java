package com.jarredweb.jesty.wsock;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class EventSocket {

    private static final Map<String, Session> USERS = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();
    private Session session;

    @OnWebSocketConnect
    public void onWebSocketConnect(Session sess) throws IOException {
        String from = "guest";
        Message msg = new Message("server", from, dateTime(), "Hi " + from + "! You are now online");
        sess.getRemote().sendString(msg.toString());
        this.session = sess;
        USERS.put(from, sess);
    }

    @OnWebSocketMessage
    public void onWebSocketText(String json) throws IOException {
        String dateTime = dateTime();
        
        Message incoming = gson.fromJson(json, Message.class);        
        String from = incoming.from;
        String to = incoming.to;
        Message echo = new Message(from, "server", dateTime, incoming.message);
        this.session.getRemote().sendString(echo.toString());
        
        Session dest = USERS.get(to);
        Message outgoing = new Message(from, to, dateTime, incoming.message);
        dest.getRemote().sendString(outgoing.toString());
    }

    @OnWebSocketClose
    public void onWebSocketClose(int statusCode, String reason) throws IOException {
        System.out.println("Socket Closed: " + reason);
        for(String user : USERS.keySet()){
            if(USERS.get(user).getRemoteAddress().equals(session.getRemoteAddress())){
                USERS.remove(user);
                break;
            }
        }
        this.session = null;
    }

    @OnWebSocketError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
    
    private String dateTime(){
        return new SimpleDateFormat("dd MMM, yy 'at' mm:hh:ssa").format(new Date());
    }

    class Message {

        public String from;
        public String to;
        @Expose(deserialize = false)
        public String time;
        public String message;

        public Message(String from, String to, String time, String message) {
            this.from = from;
            this.to = to;
            this.time = time;
            this.message = message;
        }

        @Override
        public String toString() {
            return gson.toJson(this);
        }
    }
}
