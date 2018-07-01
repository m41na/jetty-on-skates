package com.jarredweb.jesty.extras;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class AppWsServlet extends WebSocketServlet{

    private final AppWsProvider provider;

    public AppWsServlet(AppWsProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(30000);
        //factory.register(AppWebSocket.class);
        factory.setCreator(new AppWsCreator(provider));
    }    
}