package com.jarredweb.jettsey.app;

import com.jarredweb.jettsey.handler.FormHandler;
import com.jarredweb.jettsey.handler.HelloHandler;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
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

    public static void main(String... args) {
        new AppServer().start();
    }

    public void start() {
        try {
            server = new Server(8080);

            //add context handler on '/hello' context
            ContextHandler hello = new ContextHandler();
            hello.setContextPath("/hello");
            hello.setHandler(new HelloHandler());

            //add context handler on '/form' context
            ContextHandler form = new ContextHandler();
            form.setContextPath("/form");
            form.setHandler(new FormHandler());

            //add these to a context handlers collection
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.setHandlers(new Handler[]{hello, form});

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
            server.join();
        } catch (Exception e) {
            e.printStackTrace(System.err);
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
}
