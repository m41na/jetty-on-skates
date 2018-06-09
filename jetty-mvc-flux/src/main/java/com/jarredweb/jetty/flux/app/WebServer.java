package com.jarredweb.jetty.flux.app;

import com.jarredweb.jetty.flux.handler.HelloJetty;
import com.jarredweb.jetty.flux.handler.HelloServlet;
import com.jarredweb.jetty.flux.handler.ProductHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.*;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.JettyHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

public class WebServer {

    private static final ApplicationContext CTX = new AnnotationConfigApplicationContext(AppConfig.class);
    private Server server;
    private final int port = 8084;
    private final String host = "localhost";

    public void start() throws Exception {
        server = new Server(createThreadPool());
        server.addConnector(createConnector(server));
        server.setHandler(createHandlers());
        addServletHandler(server);
        server.setStopAtShutdown(true);

        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private ThreadPool createThreadPool() {
        QueuedThreadPool pool = new QueuedThreadPool();
        pool.setMinThreads(10);
        pool.setMaxThreads(100);
        return pool;
    }

    private ServerConnector createConnector(Server server) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        connector.setHost(host);
        return connector;
    }

    private Handler createHandlers() {
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{"index.html"});
        resource_handler.setResourceBase("www");

        ContextHandler helloHandler = new ContextHandler("/hello");
        helloHandler.setHandler(new HelloJetty());

        ContextHandlerCollection ctxHandlers = new ContextHandlerCollection();
        ctxHandlers.setHandlers(new Handler[]{helloHandler});

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, ctxHandlers, new DefaultHandler()});
        return handlers;
    }

    private RouterFunction<ServerResponse> routingFunction() {
        ProductHandler handler = CTX.getBean(ProductHandler.class);
        return nest(path("/product"), nest(accept(APPLICATION_JSON), route(GET("/{id}"), handler::getProduct)
                .andRoute(method(HttpMethod.GET), handler::fetchProducts))
                .andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::saveProduct));
    }

    private void addServletHandler(Server server) throws Exception {
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/flux", ServletContextHandler.SESSIONS);
        //add hello servlet
        ServletHolder helloHolder = new ServletHolder(HelloServlet.class);
        contextHandler.addServlet(helloHolder, "/hello/*");

        //create router handler
        RouterFunction<?> router = CTX.getBean(RouterFunction.class);
        HttpHandler httpHandler = toHttpHandler(router);

        // Add router servlet
        ServletHolder routerHolder = new ServletHolder(new JettyHttpHandlerAdapter(httpHandler));
        contextHandler.addServlet(routerHolder, "/route/*");
        // Add default servlet
        contextHandler.addServlet(DefaultServlet.class, "/");
        contextHandler.start();
    }

    private ServletContextHandler addRouterFunctionHandler() {
        RouterFunction<?> router = routingFunction();
        HttpHandler httpHandler = toHttpHandler(router);

        ServletContextHandler routerContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        routerContext.setErrorHandler(null);
        routerContext.setContextPath("/flux");

        // Add router servlet
        ServletHolder routerHolder = new ServletHolder(new ServletHttpHandlerAdapter(httpHandler));
        routerContext.addServlet(routerHolder, "/*");
        // Add default servlet
        routerContext.addServlet(DefaultServlet.class, "/");
        return routerContext;
    }

    public static void main(String[] args) throws Exception {
        WebServer ws = new WebServer();
        ws.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    ws.stop();
                } catch (Exception ex) {
                    Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
            }
        });
    }
}
