package com.practicaldime.jetty.gql.servlet;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.practicaldime.jetty.gql.dao.LinkRepository;
import com.practicaldime.jetty.gql.dao.LinkResolver;
import com.practicaldime.jetty.gql.dao.Mutation;
import com.practicaldime.jetty.gql.dao.Query;
import com.practicaldime.jetty.gql.dao.SanitizedError;
import com.practicaldime.jetty.gql.dao.SigninResolver;
import com.practicaldime.jetty.gql.dao.UserRepository;
import com.practicaldime.jetty.gql.dao.VoteRepository;
import com.practicaldime.jetty.gql.dao.VoteResolver;
import com.practicaldime.jetty.gql.model.AuthContext;
import com.practicaldime.jetty.gql.model.Scalars;
import com.practicaldime.jetty.gql.model.User;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

	private static final long serialVersionUID = 1L;
	
	private static final LinkRepository linkRepository;
	private static final UserRepository userRepository;
	private static final VoteRepository voteRepository;
	
	static {
		MongoClient client = new MongoClient();
		MongoDatabase mongo = client.getDatabase("hackernews");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
        voteRepository = new VoteRepository(mongo.getCollection("votes"));
        Runtime.getRuntime().addShutdownHook(new Thread(()->client.close()));
	}

	public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
	    return SchemaParser.newParser()
	        .file("schema.graphqls")
	        .resolvers(
	        		new Query(linkRepository), 
	        		new Mutation(linkRepository, userRepository, voteRepository), 
	        		new SigninResolver(),
	        		new LinkResolver(userRepository),
	        		new VoteResolver(linkRepository, userRepository))
	        .scalars(Scalars.dateTime)
	        .build()
	        .makeExecutableSchema();
	}
    
    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request
            .map(req -> req.getHeader("Authorization"))
            .filter(id -> !id.isEmpty())
            .map(id -> id.replace("Bearer ", ""))
            .map(userRepository::findById)
            .orElse(null);
        return new AuthContext(user, request, response);
    }
    
    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream()
            .filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e))
            .map(e -> e instanceof ExceptionWhileDataFetching ? new SanitizedError((ExceptionWhileDataFetching) e) : e)
            .collect(Collectors.toList());
    }
}
