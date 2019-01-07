package com.practicaldime.jesty.app;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.fcgi.server.proxy.FastCGIProxyServlet;
import org.eclipse.jetty.fcgi.server.proxy.TryFilesFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

public class FcgiServe {

	private Server server;
	private final String contextPath = "/";
	private final String resourceBase = "/var/www/wordpress";
	private final String welcomeFiles = "index.php";
	private final String scriptRoot = resourceBase;
	private final String proxyTo = "http://127.0.0.1:9000";

	public void init(String host, int port) throws Exception {
		QueuedThreadPool threadPool = new QueuedThreadPool(500, 5, 3000);
		server = new Server(threadPool);

		// Scheduler
		server.addBean(new ScheduledExecutorScheduler());

		// configure connector
		server.addConnector(createServerConnector(host, port));
		
		// create fcgi handler
		ServletContextHandler fcgiHandler = createFcgiHandler();
		
		// configure handlers list
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { fcgiHandler });

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

	protected ServletContextHandler createFcgiHandler() {
		ServletContextHandler fcgiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		fcgiContext.setContextPath(contextPath);
		fcgiContext.setResourceBase(resourceBase);
		fcgiContext.setWelcomeFiles(new String[] {welcomeFiles});

		// add try filter
		FilterHolder tryHolder = new FilterHolder(new TryFilesFilter());
		tryHolder.setInitParameter("files", "$path /index.php?p=$path");
		fcgiContext.addFilter(tryHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

		// Add default servlet (to serve the html/css/js)
		ServletHolder defHolder = new ServletHolder("default", new DefaultServlet());
		defHolder.setInitParameter("dirAllowed", "false");
		fcgiContext.addServlet(defHolder, "/");

		// add fcgi servlet for php scripts
		ServletHolder fgciHolder = new ServletHolder("fcgi", new FastCGIProxyServlet());
		fgciHolder.setInitParameter("proxyTo", proxyTo);
		fgciHolder.setInitParameter("prefix", "/");
		fgciHolder.setInitParameter("scriptRoot", scriptRoot);
		fgciHolder.setInitParameter("scriptPattern", "(.+?\\\\.php)");
		fcgiContext.addServlet(fgciHolder, "*.php");
		return fcgiContext;
	}

	public static void main(String[] args) {
		try {
			new FcgiServe().init("localhost", 8082);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
