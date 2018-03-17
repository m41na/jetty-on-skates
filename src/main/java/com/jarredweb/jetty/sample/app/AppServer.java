package com.jarredweb.jetty.sample.app;

import com.jarredweb.jetty.sample.config.JerseyConfig;
import com.jarredweb.jetty.sample.handler.FormHandler;
import com.jarredweb.jetty.sample.handler.HelloHandler;
import com.jarredweb.jetty.sample.resource.EventsSocket;
import com.jarredweb.jetty.sample.tasklet.AbstractTasklet;
import com.jarredweb.jetty.sample.tasklet.TaskletMonitor;
import java.io.IOException;
import java.util.concurrent.Future;
import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppServer extends AbstractTasklet {

    private static final Logger LOG = LoggerFactory.getLogger(AppServer.class);

    private Server server;

    public AppServer(String name) {
        super(name);
    }

    @Override
    public void deploy(Future<?> future) {
        try {
            server = new Server(8080);

            //configure rest servlet
            ServletContextHandler rsContext = getJerseyRestHandler();
            
            //configure websocket servler
            ServletContextHandler wsContext = getWebSocketHandler();
            
            //configure spring webapp servlet
            ServletContextHandler sprContext = getSpringWebAppHandler();
            
            //add context handler on '/hello' context
            ContextHandler hello = new ContextHandler();
            hello.setContextPath("/hello");
            hello.setHandler(new HelloHandler());
            
            //add context handler on '/form' context
            ContextHandler form = new ContextHandler();
            form.setContextPath("/form");
            hello.setHandler(new FormHandler());
            
            //add these to a context handlers collection
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.setHandlers(new Handler[]{hello, form});
            
            //create a resource handler
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setDirectoriesListed(true);
            resource_handler.setWelcomeFiles(new String[]{"index.html"});
            resource_handler.setResourceBase("www");
            
            //add handlers to the server
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{rsContext, wsContext, sprContext, contexts, resource_handler, new DefaultHandler()});
            server.setHandler(handlers);
            
            //initialise web socket servlet
            configureWebSocketHandler(wsContext);
            
            //add shitdown hook
            addRuntimeShutdownHook(server);
            
            //start and access server using http://localhost:8080
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        if(server == null){
            TaskletMonitor.deploy(getName(), this);
        }
    }

    @Override
    public void start() {
        TaskletMonitor.undeploy(getName());
        try{
            server.stop();
            server = null;
        }
        catch(Exception e){
            LOG.error("Problem stopping server", e);
            e.printStackTrace(System.err);
        }
    }

    private static ServletContextHandler getJerseyRestHandler() {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath("/rs");

        //Jersey
        ServletContainer container = new ServletContainer(getResourceConfig());
        ServletHolder jerseyServletHolder = new ServletHolder(container);

        contextHandler.addServlet(jerseyServletHolder, "/*");
        return contextHandler;
    }

    private static ResourceConfig getResourceConfig() {
        return new JerseyConfig();
    }

    private static ServletContextHandler getWebSocketHandler() throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath("/ws");
        return contextHandler;
    }
    
    private static void configureWebSocketHandler(ServletContextHandler handler) throws DeploymentException, ServletException{
        ServerContainer container = WebSocketServerContainerInitializer.configureContext(handler);
        container.addEndpoint(EventsSocket.class);
    }

    private static ServletContextHandler getSpringWebAppHandler() throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath("/guides");

        //Spring
        WebApplicationContext webAppContext = getWebApplicationContext();
        DispatcherServlet dispatcher = new DispatcherServlet(webAppContext);
        ServletHolder springServletHolder = new ServletHolder("mvc-dispatcher", dispatcher);

        contextHandler.addServlet(springServletHolder, "/*");
        contextHandler.addEventListener(new ContextLoaderListener(webAppContext));
        return contextHandler;
    }

    private static WebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setConfigLocation("com.jarredweb.jetty.sample.config");
        return ctx;
    }

    private static void addRuntimeShutdownHook(final Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server.isStarted()) {
                server.setStopAtShutdown(true);
                try {
                    server.stop();
                } catch (Exception e) {
                    LOG.error("Error while shutting down jetty server", e);
                    throw new RuntimeException(e);
                }
            }
        }));
    }
}
