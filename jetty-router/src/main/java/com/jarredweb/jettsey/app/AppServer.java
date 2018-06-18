package com.jarredweb.jettsey.app;

import com.jarredweb.jettsey.handler.HelloHandler;
import com.jarredweb.jettsey.route.AppRoute;
import com.jarredweb.jettsey.route.AppRoutes;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppServer {

    private static final Logger LOG = LoggerFactory.getLogger(AppServer.class);

    private Server server;
    private AppRoutes routes;
    private String status = "stopped";
    
    public String status(){
        return "server status is " + status;
    }

    public AppServer router() {
        this.routes = new AppRoutes();
        return this;
    }
    
    //************* GETS *****************//   
    
    public AppServer get(String path, Handler handler) {
        return get(path, "", "", handler);
    }

    public AppServer get(String path, ScriptObjectMirror mirror) {
        return get(path, "", "", new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                mirror.call(this, request, response);
                baseRequest.setHandled(true);
            }
        });
    }

    public AppServer get(String path, String accepts, String type, Handler handler) {
        ContextHandler context = new ContextHandler();
        context.setContextPath(path);
        context.setHandler(handler);
        routes.addHandler(new AppRoute(path, "get", accepts, type), context);
        return this;
    }
    
    //************* POSTS *****************//
    
    public AppServer post(String path, Handler handler) {
        return post(path, "", "", handler);
    }

    public AppServer post(String path, ScriptObjectMirror mirror) {
        return post(path, "", "", new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                mirror.call(this, request, response);
                baseRequest.setHandled(true);
            }
        });
    }

    public AppServer post(String path, String accepts, String type, Handler handler) {
        ContextHandler context = new ContextHandler();
        context.setContextPath(path);
        context.setHandler(handler);
        routes.addHandler(new AppRoute(path, "post", accepts, type), context);
        return this;
    }
    
    //************* START *****************//

    public void start() {
        try {
            status = "starting";
            server = new Server(8080);

            //create handlers collection
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.setHandlers(routes.getHandlers().toArray(new Handler[routes.getHandlers().size()]));

            //create a resource handler
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setDirectoriesListed(true);
            resource_handler.setWelcomeFiles(new String[]{"index.html"});
            resource_handler.setResourceBase("www");

            //create redirect handler
            RewriteHandler rewrite = new RewriteHandler();
            rewrite.setRewriteRequestURI(true);
            rewrite.setRewritePathInfo(true);
            rewrite.setOriginalPathAttribute("requestedPath");

            RewriteRegexRule oldToNew = new RewriteRegexRule();
            oldToNew.setRegex("/jambo");
            oldToNew.setReplacement("/hello");
            rewrite.addRule(oldToNew);
            rewrite.setHandler(contexts);

            //add handlers to the server
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{rewrite, resource_handler, new DefaultHandler()});
            server.setHandler(handlers);

            //add shutdown hook
            addRuntimeShutdownHook(server);

            //start and access server using http://localhost:8080
            server.start();
            status = "running";
            server.join();
            status = "stopped";
        } catch (Exception e) {
            e.printStackTrace(System.err);
            status = "stopped";
            System.exit(1);
        }
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

    public static void main(String... args) {
        new AppServer().router().get("/hello", new HelloHandler()).start();
    }
}
