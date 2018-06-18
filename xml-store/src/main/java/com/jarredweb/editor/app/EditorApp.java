package com.jarredweb.editor.app;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorApp {

    private final Logger LOGGER = LoggerFactory.getLogger(EditorApp.class);

    public void start(int port, String host) throws Exception {
        Server server = new Server();
        LOGGER.info("starting server on '{}' at port {}", host, port);

        //add connector
        ServerConnector http = new ServerConnector(server);
        http.setHost(host);
        http.setPort(port);
        http.setIdleTimeout(3000l);
        server.addConnector(http);

        //add resource handler
        ResourceHandler assetsHandler = new ResourceHandler();
        assetsHandler.setDirAllowed(true);
        assetsHandler.setWelcomeFiles(new String[]{"index.html"});
        assetsHandler.setResourceBase("www");

        //add context handler
        ContextHandler xmlHandler = new ContextHandler();
        xmlHandler.setContextPath("/catalog");
        xmlHandler.setHandler(new CatalogHandler());
        
        //add context handler
        ContextHandler blogHandler = new ContextHandler();
        blogHandler.setContextPath("/bloggin");
        blogHandler.setHandler(new BlogginHandler());

        //add handlers to server
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{assetsHandler, xmlHandler, blogHandler, new DefaultHandler()});
        server.setHandler(handlers);

        //fire up server
        server.start();
        //start handling requests
        server.join();
    }

    public static void main(String... args) throws Exception {
        new EditorApp().start(7080, "0.0.0.0");
    }
}
