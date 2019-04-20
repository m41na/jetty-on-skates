package com.practicaldime.jetty.gql.api.impl;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.practicaldime.jetty.gql.dao.LinkRepository;
import com.practicaldime.jetty.gql.dao.UserRepository;
import com.practicaldime.jetty.gql.dao.VoteRepository;
import com.practicaldime.jetty.gql.model.AuthContext;
import com.practicaldime.jetty.gql.model.AuthData;
import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.Scalars;
import com.practicaldime.jetty.gql.model.SigninPayload;
import com.practicaldime.jetty.gql.model.User;
import com.practicaldime.jetty.gql.model.Vote;

import graphql.GraphQL;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider implements Supplier<GraphQL> {

	private GraphQL graphQL;
	@Autowired
	private LinkRepository linksRepo;
	@Autowired
	private UserRepository usersRepo;
	@Autowired
	private VoteRepository votesRepo;
	private final DataFetcher<List<Link>> linksFetcher = env -> linksRepo.getAllLinks(env.getArgument("filter"), env.getArgument("skip"), env.getArgument("first"));

	public void init() throws IOException {
		URL url = Resources.getResource("schema.graphqls");
		String sdl = Resources.toString(url, Charsets.UTF_8);
		GraphQLSchema graphQLSchema = buildSchema(sdl);
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	private GraphQLSchema buildSchema(String sdl) {
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
		return graphQLSchema;
	}

	private RuntimeWiring buildWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", builder -> builder
						.dataFetcher("allLinks", linksFetcher)
						.dataFetcher("postedBy", env -> {
							Link link = env.getContext();
							if (link.getUserId() == null) {
					            return null;
					        }
					        return usersRepo.findById(link.getUserId());
						}))
				.type("Mutations", builder -> builder
						.dataFetcher("createUser", env -> {
							AuthData auth = env.getArgument("authProvider");
							User newUser = new User(env.getArgument("name"), auth.getEmail(), auth.getPassword());
							return usersRepo.saveUser(newUser);
						})
						.dataFetcher("createLink", env -> {
							AuthContext context = env.getContext();
							Link newLink = new Link(env.getArgument("url"), env.getArgument("description"), context.getUser().getId());
							linksRepo.saveLink(newLink);
							return newLink;
						})
						.dataFetcher("signinUser", env -> {
							AuthData auth = env.getArgument("auth");
							User user = usersRepo.findByEmail(auth.getEmail());
							if (user.getPassword().equals(auth.getPassword())) {
								return new SigninPayload(user.getId(), user);
							}
							throw new GraphQLException("Invalid credentials");
						})
						.dataFetcher("createVote", env -> {
							ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
						    return votesRepo.saveVote(new Vote(now, env.getArgument("userId"), env.getArgument("linkId")));
						})
						.dataFetcher("SigninPayload", env -> {
							SigninPayload payload = env.getContext();
							return payload.getUser();
						}))
				.scalar(Scalars.dateTime).build();
	}
	
	@Override
	@Bean
	public GraphQL get() {
		if(this.graphQL == null) {
			try {
				init();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return this.graphQL;
	}
}