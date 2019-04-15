package com.practicaldime.jetty.gql.websock;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.practicaldime.jetty.gql.listener.AppEvent;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.reactivex.subjects.PublishSubject;

public class SchemaProvider {

	private final GraphQLSchema graphQLSchema;
	private final PublishSubject<AppEvent<LinkCreated>> publisher;

	public SchemaProvider(PublishSubject<AppEvent<LinkCreated>> publisher) {
		this.graphQLSchema = buildSchema();
		this.publisher = publisher;
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
	public DataFetcher<PublishSubject<AppEvent<LinkCreated>>> linkCreatedEvent() {
		return environment -> {
			return publisher;
		};
	}
}
