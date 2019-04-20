package com.practicaldime.jetty.gql.websock;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.reactivestreams.Publisher;

import com.practicaldime.jetty.gql.listener.AppEventListener;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.reactivex.BackpressureStrategy;

public class SchemaProvider {

	private final GraphQLSchema graphQLSchema;
	
	private final AppEventListener<LinkCreated> listener;

	public SchemaProvider(AppEventListener<LinkCreated> listener) {
		this.listener = listener;
		this.graphQLSchema = buildSchema();
	}

	public AppEventListener<LinkCreated> getListener() {
		return listener;
	}

	private GraphQLSchema buildSchema() {
		Reader streamReader = loadSchemaFile("events.graphqls");
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(streamReader);

		RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring("Subscription").dataFetcher("linkCreated", linkCreatedEvent())).build();

		return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
	}

	private Reader loadSchemaFile(String name) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
		return new InputStreamReader(stream);
	}

	public GraphQLSchema getGraphQLSchema() {
		return graphQLSchema;
	}

	//The data fetcher must return a Publisher of events when using graphql subscriptions
	public DataFetcher<Publisher<LinkCreated>> linkCreatedEvent() {
		return environment -> listener.getObservable().toFlowable(BackpressureStrategy.BUFFER);
	}
}
