package com.practicaldime.jetty.sample.app;

import com.practicaldime.jetty.sample.resource.EventsSocket;
import java.net.URI;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.eclipse.jetty.util.component.LifeCycle;

public class EventsClient {

    public static void main(String... args) {
        URI uri = URI.create("ws://localhost:8080/rws/events/");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            try {
                //connect to server
                try (Session session = container.connectToServer(EventsSocket.class, uri)) {
                    //send a message
                    session.getBasicRemote().sendText("Hello");
                }
            } finally {
                //stop container through lifecycle methods
                if (container instanceof LifeCycle) {
                    ((LifeCycle) container).stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
