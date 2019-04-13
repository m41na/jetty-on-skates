package com.practicaldime.jetty.gql.servlet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerConfig;
import com.practicaldime.zesty.websock.AppWsPolicy;
import com.practicaldime.zesty.websock.AppWsHandler;

public class App {

private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		int port = 1337;
		String host = "localhost";
		
		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/");
		props.put("assets", "src/main/webapp");
		props.put("cors", "true");
		
		HandlerConfig config = handler -> {
			handler.setAsyncSupported(true);
		};

		new AppServer(props).router()
		.servlet("/graphql", config, new GraphQLEndpoint())
		.websocket("/events", () -> new AppWsHandler("events"), () -> AppWsPolicy.defaultConfig())
		.listen(port, host, (msg) -> {
			LOG.info(msg);
		});		
	}
}
