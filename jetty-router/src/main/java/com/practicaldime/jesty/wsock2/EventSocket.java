package com.practicaldime.jesty.wsock2;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EventSocket extends WebSocketAdapter{

    private static final Map<String, Session> USERS = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    @Override
    public void onWebSocketConnect(Session sess){
        super.onWebSocketConnect(sess);
        try {
            String from = null, to = null;
            String url = sess.getUpgradeRequest().getRequestURI().toString();
            Pattern pattern = Pattern.compile("(/events/(.+?)/dest/(.*))$");
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()){
                from = matcher.group(2);
                to = matcher.group(3);
            }

            Message msg = new Message("server", from, dateTime(), "Hi " + from + "! You are now online");
            sess.getRemote().sendString(msg.toString());
            USERS.put(from, sess);
        } catch (IOException ex) {
            Logger.getLogger(EventSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onWebSocketText(String json) {
        super.onWebSocketText(json);
        try {
            String dateTime = dateTime();
            
            Message incoming = gson.fromJson(json, Message.class);
            String from = incoming.from;
            String to = incoming.to;
            Message echo = new Message(from, "server", dateTime, incoming.message);
            getRemote().sendString(echo.toString());
            
            Session dest = USERS.get(to);
            Message outgoing = new Message(from, to, dateTime, incoming.message);
            dest.getRemote().sendString(outgoing.toString());
        } catch (IOException ex) {
            Logger.getLogger(EventSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
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
