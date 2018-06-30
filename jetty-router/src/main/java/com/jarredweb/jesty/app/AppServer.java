package com.jarredweb.jesty.app;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jarredweb.jesty.route.AppRoute;
import com.jarredweb.jesty.route.AppRoutes;
import com.jarredweb.jesty.servlet.HandlerFilter;
import com.jarredweb.jesty.servlet.HandlerRequest;
import com.jarredweb.jesty.servlet.HandlerResponse;
import com.jarredweb.jesty.servlet.HandlerServlet;
import com.jarredweb.jesty.servlet.HealthServlet;
import com.jarredweb.jesty.servlet.ReRouteFilter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ScriptFunction;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

public class AppServer {

    private static final Logger LOG = LoggerFactory.getLogger(AppServer.class);

    private Server server;
    private AppRoutes routes;
    private String status = "stopped";
    private String assets = "www";
    private final Map<String, Object> locals = new HashMap<>();
    private final Map<String, ScriptObjectMirror> context = new HashMap<>();
    private final LifecycleSubscriber lifecycle = new LifecycleSubscriber();
    private final ServletContextHandler servlets = new ServletContextHandler(ServletContextHandler.SESSIONS);

    public String status() {
        return "server status is " + status;
    }

    public void use(String name, ScriptObjectMirror component) {
        this.context.put(name, component);
    }

    public void assets(String path) {
        this.assets = path;
    }

    public Set<String> locals() {
        return locals.keySet();
    }

    public Object locals(String param) {
        return locals.get(param);
    }

    public AppServer lifecycle(String event, ScriptObjectMirror callback) {
        this.lifecycle.subscribe(event, callback);
        return this;
    }

    public AppServer router() {
        this.routes = AppRoutes.instance();
        return this;
    }

    public AppServer filter(HandlerFilter filter) {
        FilterHolder holder = new FilterHolder(filter);
        servlets.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
        return this;
    }

    public AppServer route(String method, String path, HandlerServlet handler) {
        switch (method.toLowerCase()) {
            case "get":
                return get(path, "", "", handler);
            case "post":
                return post(path, "", "", handler);
            case "put":
                return put(path, "", "", handler);
            case "delete":
                return delete(path, "", "", handler);
            case "options":
                return options(path, "", "", handler);
            case "trace":
                return trace(path, "", "", handler);
            case "head":
                return head(path, "", "", handler);
            case "all":
                return all(path, "", "", handler);
            default:
                throw new UnsupportedOperationException(method + " is not a supported method");
        }
    }

    //************* HEAD *****************//   
    public AppServer head(String path, HandlerServlet handler) {
        return head(path, "", "", handler);
    }

    public AppServer head(String path, ScriptObjectMirror mirror) {
        return head(path, "", "", new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer head(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "head", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    //************* TRACE *****************//   
    public AppServer trace(String path, HandlerServlet handler) {
        return trace(path, "", "", handler);
    }

    public AppServer trace(String path, ScriptObjectMirror mirror) {
        return trace(path, "", "", new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer trace(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "trace", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    //************* OPTIONS *****************//   
    public AppServer options(String path, HandlerServlet handler) {
        return trace(path, "", "", handler);
    }

    public AppServer options(String path, ScriptObjectMirror mirror) {
        return options(path, "", "", new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer options(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "options", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    //************* GET *****************//   
    public AppServer get(String path, HandlerServlet handler) {
        return get(path, "", "", handler);
    }

    public AppServer get(String path, ScriptObjectMirror mirror) {
        return get(path, "", "", mirror);
    }

    public AppServer get(String path, String accepts, String type, ScriptObjectMirror mirror) {
        return get(path, accepts, type, new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer get(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "get", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    //************* POST *****************//
    public AppServer post(String path, HandlerServlet handler) {
        return post(path, "", "", handler);
    }

    public AppServer post(String path, ScriptObjectMirror mirror) {
        return post(path, "", "", mirror);
    }

    public AppServer post(String path, String accepts, String type, ScriptObjectMirror mirror) {
        return post(path, accepts, type, new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer post(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "post", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        ServletHolder sholder = new ServletHolder(handler);
        //for multipart/form-data, customize the servlet holder
        if (type.toLowerCase().contains("multipart/form-data")) {
            MultipartConfigElement mpce = new MultipartConfigElement("temp", 1024 * 1024 * 50, 1024 * 1024, 5);
            sholder.getRegistration().setMultipartConfig(mpce);
        }
        servlets.addServlet(sholder, route.pathId);
        return this;
    }

    //************* PUT *****************//
    public AppServer put(String path, HandlerServlet handler) {
        return put(path, "", "", handler);
    }

    public AppServer put(String path, ScriptObjectMirror mirror) {
        return put(path, "", "", mirror);
    }

    public AppServer put(String path, String accepts, String type, ScriptObjectMirror mirror) {
        return put(path, accepts, type, new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer put(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "put", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    //************* DELETE *****************//
    public AppServer delete(String path, HandlerServlet handler) {
        return delete(path, "", "", handler);
    }

    public AppServer delete(String path, ScriptObjectMirror mirror) {
        return delete(path, "", "", mirror);
    }

    public AppServer delete(String path, String accepts, String type, ScriptObjectMirror mirror) {
        return delete(path, accepts, type, new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer delete(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "delete", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    //************* ALL *****************//   
    public AppServer all(String path, HandlerServlet handler) {
        return all(path, "", "", handler);
    }

    public AppServer all(String path, ScriptObjectMirror mirror) {
        return all(path, "", "", mirror);
    }

    public AppServer all(String path, String accepts, String type, ScriptObjectMirror mirror) {
        return all(path, accepts, type, new HandlerServlet() {
            @Override
            public void handle(HandlerRequest request, HandlerResponse response) {
                mirror.call(this, request, response);
            }
        });
    }

    public AppServer all(String path, String accepts, String type, HandlerServlet handler) {
        AppRoute route = new AppRoute(path, "*", accepts, type);
        route.setId();
        routes.addRoute(route);
        //add servlet handler
        servlets.addServlet(new ServletHolder(handler), route.pathId);
        return this;
    }

    private AppServer websocket(String ctx) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(ctx.startsWith("/") ? ctx : "/" + ctx);
        server.setHandler(context);
    }

    //************* START *****************//
    public void listen(int port, String host) {
        listen(port, host, null);
    }

    public void listen(int port, String host, ScriptFunction result) {
        try {
            status = "starting";
            //create server with thread pool
            QueuedThreadPool threadPool = new QueuedThreadPool(500, 5, 3000);
            server = new Server(threadPool);

            // Scheduler
            server.addBean(new ScheduledExecutorScheduler());

            //configure connector
            ServerConnector http = new ServerConnector(server);
            http.setHost(host);
            http.setPort(port);
            http.setIdleTimeout(3000);
            server.addConnector(http);

            //TODO: configure secure connector
            //add servlet handler
            servlets.setContextPath("/");
            servlets.addFilter(new FilterHolder(new ReRouteFilter(this.routes)), "/*", EnumSet.of(DispatcherType.REQUEST));

            //create health servlet
            HealthServlet health = new HealthServlet();

            //add health servlet GET routes
            AppRoute healthGet = new AppRoute("/health", "get", "", "text/html");
            healthGet.setId();
            routes.addRoute(healthGet);
            servlets.addServlet(new ServletHolder(health), healthGet.pathId);

            //add health servlet POST routes
            AppRoute healthPost = new AppRoute("/health/post", "post", "", "multipart/form-data");
            healthPost.setId();
            routes.addRoute(healthPost);
            servlets.addServlet(new ServletHolder(health), healthPost.pathId);

            //create a resource handler
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setDirectoriesListed(true);
            resource_handler.setWelcomeFiles(new String[]{"index.html"});
            resource_handler.setResourceBase(assets);

            //add handlers to the server
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resource_handler, this.servlets, new DefaultHandler()});
            server.setHandler(handlers);

            //add shutdown hook
            addRuntimeShutdownHook(server);

            //start and access server using http://localhost:8080
            server.start();
            status = "running";
            if (result != null) {
                result.getBoundInvokeHandle(this).invoke();
            }
            server.join();
            status = "stopped";
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            status = "stopped";
            System.exit(1);
        }
    }

    private ServletContextHandler configureDefaultServlet() {
        ServletContextHandler defaultHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        defaultHandler.setContextPath("/");
        defaultHandler.setResourceBase("www");
        defaultHandler.setWelcomeFiles(new String[]{"index.html"});
        defaultHandler.setInitParameter("dirAllowed", "false");
        return defaultHandler;
    }

    private void addRuntimeShutdownHook(final Server server) {
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

    class LifecyclePublisher implements LifeCycle.Listener {

        private final LifecycleSubscriber subscriber;

        public LifecyclePublisher(LifecycleSubscriber subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void lifeCycleStarting(LifeCycle event) {
            subscriber.onStarting();
        }

        @Override
        public void lifeCycleStarted(LifeCycle event) {
            subscriber.onStarted();
        }

        @Override
        public void lifeCycleFailure(LifeCycle event, Throwable cause) {
            subscriber.onFailed(cause);
        }

        @Override
        public void lifeCycleStopping(LifeCycle event) {
            subscriber.onStopping();
        }

        @Override
        public void lifeCycleStopped(LifeCycle event) {
            subscriber.onStopped();
        }
    }

    class LifecycleSubscriber {

        private final Map<String, ScriptObjectMirror> subscribers;

        public LifecycleSubscriber() {
            this.subscribers = new HashMap<>();
        }

        public void subscribe(String event, ScriptObjectMirror callback) {
            if (subscribers.keySet().contains(event)) {
                this.subscribers.put(event, callback);
            } else {
                LOG.error("There is no such event as {}", event);
            }
        }

        public void onStarting() {
            subscribers.get("starting").call(this);
        }

        public void onStarted() {
            subscribers.get("started").call(this);
        }

        public void onStopping() {
            subscribers.get("stopping").call(this);
        }

        public void onStopped() {
            subscribers.get("stopped").call(this);
        }

        public void onFailed(Throwable thr) {
            subscribers.get("started").call(this, thr);
        }
    }
}