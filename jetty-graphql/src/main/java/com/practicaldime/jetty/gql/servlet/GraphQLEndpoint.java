package com.practicaldime.jetty.gql.servlet;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.practicaldime.jetty.gql.dao.LinkRepository;
import com.practicaldime.jetty.gql.dao.Mutation;
import com.practicaldime.jetty.gql.dao.Query;
import com.practicaldime.jetty.gql.dao.SanitizedError;
import com.practicaldime.jetty.gql.dao.SigninResolver;
import com.practicaldime.jetty.gql.dao.UserRepository;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

	private static final long serialVersionUID = 1L;

	private static final LinkRepository linkRepository;
	private static final UserRepository userRepository;

	static {
		MongoClient client = new MongoClient();
		MongoDatabase mongo = client.getDatabase("hackernews");
		linkRepository = new LinkRepository(mongo.getCollection("links"));
		userRepository = new UserRepository(mongo.getCollection("users"));
		Runtime.getRuntime().addShutdownHook(new Thread(() -> client.close()));
	}

	public GraphQLEndpoint() {
		super(buildSchema());
	}

	@Override
	protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
		return errors.stream().filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e)).map(
				e -> e instanceof ExceptionWhileDataFetching ? new SanitizedError((ExceptionWhileDataFetching) e) : e)
				.collect(Collectors.toList());
	}

	private static GraphQLSchema buildSchema() {
		return SchemaParser
				.newParser().file("schema.graphqls").resolvers(new Query(linkRepository),
						new Mutation(linkRepository, userRepository), new SigninResolver())
				.build().makeExecutableSchema();
	}
}
