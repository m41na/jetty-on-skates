package com.practicaldime.jetty.gql.websock;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.eclipse.jetty.websocket.api.Session;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.google.gson.Gson;
import com.practicaldime.zesty.websock.AppWsAdapter;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;

public class PublishWsAdapter extends AppWsAdapter {

	private final SchemaProvider provider;
	private final Gson gson = new Gson();
	private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

	public PublishWsAdapter(SchemaProvider provider) {
		this.provider = provider;
	}

	@Override
	public String getContext() {
		return "";
	}

	@Override
	public String timestamp() {
		return new SimpleDateFormat("dd MMM, yy 'at' mm:hh:ssa").format(new Date());
	}

	@Override
	public Function<Session, String> idStrategy() {
		return sess -> {
			return "";
		};
	}

	@Override
	public void onConnect() throws IOException {
		log("info", "websocket connection established");
	}

	@Override
	public void onString(String graphqlQuery) throws IOException {
		log("info", "Websocket said {}", graphqlQuery);

		QueryParameters parameters = QueryParameters.from(graphqlQuery);

		ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(parameters.getQuery())
				.variables(parameters.getVariables()).operationName(parameters.getOperationName()).build();

		Instrumentation instrumentation = new ChainedInstrumentation(singletonList(new TracingInstrumentation()));

		//
		// In order to have subscriptions in graphql-java you MUST use the
		// SubscriptionExecutionStrategy strategy.
		//
		GraphQL graphQL = GraphQL.newGraphQL(provider.getGraphQLSchema()).instrumentation(instrumentation).build();

		ExecutionResult executionResult = graphQL.execute(executionInput);

		Publisher<ExecutionResult> eventsStream = executionResult.getData();

		eventsStream.subscribe(new Subscriber<ExecutionResult>() {

			@Override
			public void onSubscribe(Subscription subscription) {
				subscriptionRef.set(subscription);
				subscription.request(1);
			}

			@Override
			public void onNext(ExecutionResult item) {
				Map<String, Object> event = item.getData();
				try {
					sendString(gson.toJson(event));
				} catch (IOException e) {
					log("error", e.getMessage());
				}
				request(1);
			}

			@Override
			public void onError(Throwable throwable) {
				log("error", "Subscription threw an exception: {}", throwable.getMessage());
				getSession().close();
			}

			@Override
			public void onComplete() {
				log("info", "Subscription complete");
				getSession().close();
			}
		});
	}

	@Override
	public void onClose(int statusCode, String reason) throws IOException {
		getSession().close();
	}

	@Override
	public void onError(Throwable cause) throws IOException {
		getSession().close();
	}

	private void request(int n) {
		Subscription subscription = subscriptionRef.get();
		if (subscription != null) {
			subscription.request(n);
		}
	}
}
