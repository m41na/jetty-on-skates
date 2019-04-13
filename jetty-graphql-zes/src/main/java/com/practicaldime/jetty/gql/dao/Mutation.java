package com.practicaldime.jetty.gql.dao;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.practicaldime.jetty.gql.model.AuthContext;
import com.practicaldime.jetty.gql.model.AuthData;
import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.SigninPayload;
import com.practicaldime.jetty.gql.model.User;
import com.practicaldime.jetty.gql.model.Vote;

import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;

public class Mutation implements GraphQLMutationResolver {

	private final LinkRepository linkRepository;
	private final UserRepository userRepository;
	private final VoteRepository voteRepository;

	public Mutation(LinkRepository linkRepository, UserRepository userRepository, VoteRepository voteRepository) {
		this.linkRepository = linkRepository;
		this.userRepository = userRepository;
		this.voteRepository = voteRepository;
	}

	public Link createLink(String url, String description, DataFetchingEnvironment env) {
		AuthContext context = env.getContext();
		Link newLink = new Link(url, description, context.getUser().getId());
		linkRepository.saveLink(newLink);
		return newLink;
	}

	public User createUser(String name, AuthData auth) {
		User newUser = new User(name, auth.getEmail(), auth.getPassword());
		return userRepository.saveUser(newUser);
	}

	public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
		User user = userRepository.findByEmail(auth.getEmail());
		if (user.getPassword().equals(auth.getPassword())) {
			return new SigninPayload(user.getId(), user);
		}
		throw new GraphQLException("Invalid credentials");
	}
	
	public Vote createVote(String linkId, String userId) {
	    ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
	    return voteRepository.saveVote(new Vote(now, userId, linkId));
	}
}
