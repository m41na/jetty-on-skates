package com.practicaldime.jesty.wsock3;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value = "/events/{from}/dest/{dest}")
public class EventSocket {

    private static final Map<String, Session> USERS = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    @OnOpen
    public void onWebSocketConnect(Session sess) throws IOException {
        String from = sess.getPathParameters().get("from");
        Message msg = new Message("server", from, dateTime(), "Hi " + from + "! You are now online");
        sess.getBasicRemote().sendText(msg.toString());
        USERS.put(from, sess);
    }

    @OnMessage
    public void onWebSocketText(String json, Session sess) throws IOException {
        String dateTime = dateTime();
        
        Message incoming = gson.fromJson(json, Message.class);        
        String from = incoming.from;
        String to = incoming.to;
        Message echo = new Message(from, "server", dateTime, incoming.message);
        sess.getBasicRemote().sendText(echo.toString());
        
        Session dest = USERS.get(to);
        Message msg = new Message(from, to, dateTime, incoming.message);
        dest.getBasicRemote().sendText(msg.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason, Session session) throws IOException {
        System.out.println("Socket Closed: " + reason);
        for(String user : USERS.keySet()){
            if(USERS.get(user).getId().equals(session.getId())){
                USERS.remove(user);
                break;
            }
        }
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
    
    private String dateTime(){
        return new SimpleDateFormat("dd MMM, yy 'at' mm:hh:ssa").format(new Date());
    }

    class Message {

        public String from;
        public String to;
        @Expose(serialize = false)
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
