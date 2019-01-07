package com.practicaldime.jesty.app;

import java.util.Map;

import com.google.common.collect.Maps;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerRequest;
import com.practicaldime.zesty.servlet.HandlerResponse;
import com.practicaldime.zesty.servlet.HandlerServlet;
import com.practicaldime.zesty.websock.AppWsEvents;

public class ZestyWs {

	public static void main(String... args) {
		// start server
		int port = 8080;
		String host = "localhost";

		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/app");
		props.put("assets", "www/wsock2");
		props.put("engine", "freemarker");

		AppServer app = new AppServer(props);
		app.router().get("/hi", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.render("index", Maps.newHashMap());
			}
		}).websocket("/events/*", AppWsEvents::new).listen(port, host, (msg) -> System.out.println(msg));
	}
}
