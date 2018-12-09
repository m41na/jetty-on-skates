package com.practicaldime.jesty.app;

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
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import com.practicaldime.jesty.servlet.HealthServlet;

public class SampleServe {

	private Server server;

	public void init(String host, int port, String resourceBase, String appContext) throws Exception {
		QueuedThreadPool threadPool = new QueuedThreadPool(500, 5, 3000);
		server = new Server(threadPool);

		// Scheduler
		server.addBean(new ScheduledExecutorScheduler());

		// configure connector
		server.addConnector(createServerConnector(host, port));

		// configure resource handler
		ResourceHandler resHandler = createResourceHandler(resourceBase);
		ServletContextHandler servletResHander = createServletResourceHandler(resourceBase, appContext);
		servletResHander.addServlet(new ServletHolder("health",new HealthServlet()), "/health");

		// configure context handlers
		ContextHandlerCollection contextHandlers = new ContextHandlerCollection();

		// create context for resource handler
		ContextHandler ctxResContext = new ContextHandler();
		ctxResContext.setContextPath("/");
		ctxResContext.setHandler(resHandler);
		contextHandlers.addHandler(ctxResContext);

		// configure handlers list
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandlers, servletResHander, new DefaultHandler() });

		// add server handlers
		server.setHandler(handlers);

		// start and access server using http://localhost:8080
		server.start();
		server.join();
	}

	private ServerConnector createServerConnector(String host, int port) {
		ServerConnector http = new ServerConnector(server);
		http.setHost(host);
		http.setPort(port);
		http.setIdleTimeout(3000);
		return http;
	}

	private ResourceHandler createResourceHandler(String resourceBase) {
		ResourceHandler appResources = new ResourceHandler();
		appResources.setDirectoriesListed(false);
		appResources.setWelcomeFiles(new String[] { "index.html" });
		appResources.setResourceBase(resourceBase);
		return appResources;
	}

	private ServletContextHandler createServletResourceHandler(String resourceBase, String appContext) {
		ServletContextHandler rootResources = new ServletContextHandler(ServletContextHandler.SESSIONS);
		rootResources.setContextPath(appContext);
		rootResources.setWelcomeFiles(new String[] { "index.html" });
		// DefaultServlet should be named 'default'
		ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
		defaultServlet.setInitParameter("resourceBase", resourceBase);
		defaultServlet.setInitParameter("dirAllowed", "false");
		rootResources.addServlet(defaultServlet, "/*");
		return rootResources;
	}

	public static void main(String[] args) {
		try {
			new SampleServe().init("localhost", 8080, "www", "/app");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
