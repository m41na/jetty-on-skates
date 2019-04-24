package com.practicaldime.jesty.wsock2;

import java.net.URI;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class EventClient {

    public static void main(String[] args) {

        WebSocketClient client = new WebSocketClient();

        EventSocket socket = new EventSocket();
        try {
            client.start();

            URI uri = URI.create("ws://localhost:8080/events");
            URI echoUri = new URI(uri.toString());
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            System.out.printf("Connecting dest : %s%n", echoUri);

            Thread.sleep(4000);
        } catch (Exception t) {
            t.printStackTrace(System.err);
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
