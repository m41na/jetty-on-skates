package com.jarredweb.jetty.flux.app;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
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
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import com.jarredweb.jetty.flux.handler.ProductHandler;
import javax.servlet.Servlet;

public class WebServer {

    private static final ApplicationContext CTX = new AnnotationConfigApplicationContext(AppConfig.class);
    private Server server;
    private final int port = 8084;
    private final String host = "localhost";

    public void start() throws Exception {
        server = new Server(createThreadPool());
        server.addConnector(createConnector(server));

        //**
        ServletHolder helloServlet = new ServletHolder(createHelloServlet());
        ServletHolder productServlet = new ServletHolder(createProductServlet());

        ServletContextHandler contextHandler = new ServletContextHandler(server, "");
        contextHandler.setResourceBase("./www");
        contextHandler.setWelcomeFiles(new String[]{"index.html"});
        contextHandler.setInitParameter("dirAllowed", "false");
        contextHandler.addServlet(DefaultServlet.class, "/*");
        contextHandler.addServlet(helloServlet, "/app/*");
        contextHandler.addServlet(productServlet, "/product/*");
        contextHandler.start();
        //**
        
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

    private Servlet createHelloServlet() throws Exception {
        //create router handler
        RouterFunction<?> router = CTX.getBean(RouterFunction.class);
        HttpHandler httpHandler = toHttpHandler(router);
        return new JettyHttpHandlerAdapter(httpHandler);
    }

    private Servlet createProductServlet() {
        RouterFunction<?> router = routingFunction();
        HttpHandler httpHandler = toHttpHandler(router);
        return new JettyHttpHandlerAdapter(httpHandler);
    }

    private RouterFunction<ServerResponse> routingFunction() {
        ProductHandler handler = CTX.getBean(ProductHandler.class);
        return nest(path("/product"), nest(accept(APPLICATION_JSON), route(GET("/{id}"), handler::getProduct)
                .andRoute(method(HttpMethod.GET), handler::fetchProducts))
                .andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::saveProduct));
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
