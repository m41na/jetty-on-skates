package com.practicaldime.jesty.route2;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import com.practicaldime.zesty.route.AppRoute;
import com.practicaldime.zesty.route.AppRoutes;

public class RoutesServer {

    public static void main(String[] args) throws Exception {
        // Setup Threadpool
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(500);

        // Server
        Server server = new Server(threadPool);
        
        // Scheduler
        server.addBean(new ScheduledExecutorScheduler());

        // HTTP connector
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080);
        http.setIdleTimeout(5000);

        // Set the connector
        server.addConnector(http);

        // Add context handlers
        ContextHandler context = new ContextHandler("en");
        context.setContextPath("/en");
        context.setHandler(new HelloHandler("Root Hello"));

        ContextHandler contextFR = new ContextHandler("/fr");
        contextFR.setHandler(new HelloHandler("Bonjoir"));

        ContextHandler contextIT = new ContextHandler("/it");
        contextIT.setHandler(new HelloHandler("Bongiorno"));

        ContextHandler contextV = new ContextHandler("/");
        contextV.setVirtualHosts(new String[]{"127.0.0.2"});
        contextV.setHandler(new HelloHandler("Virtual Hello"));

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{context, contextFR, contextIT,
            contextV});
        
        //try using router
        AppRoutes routes = AppRoutes.instance();
        AppRoute helloRoute = new AppRoute("/hello/:lang", "get", "", "");
        routes.addRoute(helloRoute);
        RouteHandler helloHandler = new RouteHandler(helloRoute);
        
        AppRoute jumpRoute = new AppRoute("/jump/:lang", "get", "", "");
        routes.addRoute(jumpRoute);
        RouteHandler jumpHandler = new RouteHandler(jumpRoute);
        
        RouteHandlers routesList = new RouteHandlers(routes);
        routesList.setHandlers(new Handler[]{helloHandler, jumpHandler});

        //Configure resource handler
        ResourceHandler resource_handler = new ResourceHandler();

        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{"index.html"});
        resource_handler.setResourceBase("www");

        // Add the ResourceHandler to the server.
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{routesList, contexts, resource_handler, new DefaultHandler()});
        server.setHandler(handlers);

        server.start();
        server.dumpStdErr();
        server.join();
    }
}
