package com.practicaldime.jetty.gql.servlet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.jetty.gql.listener.AppEvent;
import com.practicaldime.jetty.gql.websock.LinkCreated;
import com.practicaldime.jetty.gql.websock.PublishWsAdapter;
import com.practicaldime.jetty.gql.websock.SchemaProvider;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerConfig;

import io.reactivex.subjects.PublishSubject;

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

		PublishSubject<AppEvent<LinkCreated>> publisher = PublishSubject.create();
		SchemaProvider provider = new SchemaProvider(publisher);
		PublishWsAdapter wsHandler = new PublishWsAdapter(provider);

		new AppServer(props).router()
		.servlet("/graphql", config, new GraphQLEndpoint(publisher))
		.websocket("/events", () -> wsHandler)
		.listen(port, host, (msg) -> {
			LOG.info(msg);
		});		
	}
}
