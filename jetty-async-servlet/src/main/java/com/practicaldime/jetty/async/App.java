package com.practicaldime.jetty.async;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {

	public static String api = "/api/*";

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		HandlerList handlers = new HandlerList();

		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");

//		ServletHolder helloHolder = context.addServlet(HelloServlet.class, "/hi");
//		helloHolder.setAsyncSupported(true);

		ServletHolder routeHolder = context.addServlet(RouteServlet.class, api);
		routeHolder.setAsyncSupported(true);

		DefaultServlet defaultServlet = new DefaultServlet();
		ServletHolder defaultHolder = new ServletHolder("default", defaultServlet);
		defaultHolder.setInitParameter("resourceBase", "./www");
		context.addServlet(defaultHolder, "/*");

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setResourceBase("www");

		handlers.setHandlers(new Handler[] { context, resourceHandler });
		server.setHandler(context);
		// Start things up!
		server.start();
		server.join();
	}

//	public static class HelloServlet extends HandlerServletAsync {
//
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public CompletableFuture<HandlerContext> handler(HandlerContext context) {
//			context.set("DATA", ByteBuffer.wrap("Hello from servlet!!!!!".getBytes()));
//			return CompletableFuture.supplyAsync(() -> context);
//		}
//	}

	public static class RouteServlet extends HandlerServletAsync {

		private static final long serialVersionUID = 1L;

		@Override
		public CompletableFuture<HandlerContext> handler(HandlerContext context) {
			return router.apply(context);
		}
	}

	public static MiddlewareChain<HandlerContext> chain() {
		MiddlewareChain<HandlerContext> chain = new MiddlewareChain<>();
		chain.register(router);
		return chain;
	}

	public static Middleware<HandlerContext> hello = new AbstractMiddleware<HandlerContext>() {

		@Override
		public String getName() {
			return "hello handler";
		}

		@Override
		public CompletableFuture<HandlerContext> apply(HandlerContext context) {
			context.set("DATA", ByteBuffer.wrap("Hello from middleware!!!!!".getBytes()));
			return CompletableFuture.supplyAsync(() -> context);
		}
	};

	public static Middleware<HandlerContext> echo = new AbstractMiddleware<HandlerContext>() {

		@Override
		public String getName() {
			return "echo handler";
		}

		@Override
		public CompletableFuture<HandlerContext> apply(HandlerContext context) {
			return CompletableFuture.supplyAsync(() -> context);
		}
	};

	public static Middleware<HandlerContext> router = new AbstractMiddleware<HandlerContext>() {

		@Override
		public String getName() {
			return "routing handler";
		}

		@Override
		public CompletableFuture<HandlerContext> apply(HandlerContext context) {
			HttpServletRequest req = context.getReq();
			String url = req.getRequestURI();
			System.out.printf("URL DETECTED %s%n", url);
			Middleware<HandlerContext> route = routes.get(url);
			if (route != null) {
				return route.apply(context);
			} else {
				return CompletableFuture.failedFuture(new RuntimeException("requested path does not exist"));
			}
		}
	};

	public static Map<String, Middleware<HandlerContext>> routes = new HashMap<>();
	static {
		routes.put(api.replace("*", "hello"), hello);
		routes.put(api.replace("*", "echo"), echo);
	};
}
