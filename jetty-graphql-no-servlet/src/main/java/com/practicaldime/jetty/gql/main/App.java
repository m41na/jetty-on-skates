package com.practicaldime.jetty.gql.main;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.jetty.gql.api.GraphQLInvocationData;
import com.practicaldime.jetty.gql.api.GraphQLRequest;
import com.practicaldime.jetty.gql.api.impl.GraphQLHandler;
import com.practicaldime.jetty.gql.websock.PublishWsAdapter;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.servlet.HandlerConfig;
import com.practicaldime.zesty.servlet.HandlerRequest;
import com.practicaldime.zesty.servlet.HandlerResponse;

public class App {

private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		int port = 1337;
		String host = "localhost";
		
		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/");
		props.put("assets", "src/main/webapp");
		props.put("cors", "true");
		
		AppContext ctx = new AppContext();
		ctx.init();
		//get handler beans
		GraphQLHandler handler = ctx.getBean("", GraphQLHandler.class);
		PublishWsAdapter wsHandler = ctx.getBean("", PublishWsAdapter.class);
		
		HandlerConfig config = handle -> {
			handle.setAsyncSupported(true);
		};

		new AppServer(props).router()
		.websocket("/events", () -> wsHandler)
		.get("/graphql", "", "application/json", config, (HandlerRequest request, HandlerResponse response) -> {
			String query = request.param("query");
			String operationName = request.param("operationName");
			String variablesJson = request.param("variables");	        
			try {
				response.setContentType("application/json");
				byte[] bytes = handler.handle(query, operationName, variablesJson, (header) -> request.header(header)).get(); //consider switching dest non-blocking
				response.bytes(bytes);
				
			} catch (ExecutionException | InterruptedException e) {
				response.status(400);
				response.send(e.getMessage());
			}
			return null;
		})
		.post("/graphql", "application/json", "application/json", config, (HandlerRequest request, HandlerResponse response) -> {
			GraphQLRequest req = request.body(GraphQLRequest.class); 
			try {
				response.setContentType("application/json");
				byte[] bytes = handler.handle(new GraphQLInvocationData(req.getQuery(), req.getOperationName(), req.getVariables(), (header) -> request.header(header))).get();
				response.bytes(bytes);
			} catch (ExecutionException | InterruptedException  e) {
				response.status(400);
				response.send(e.getMessage());
			}
			return null;
		})
		.listen(port, host, (msg) -> {
			LOG.info(msg);
		});
	}
}
