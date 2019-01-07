package com.practicaldime.jetty.gql.dao;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.practicaldime.jetty.gql.model.SigninPayload;
import com.practicaldime.jetty.gql.model.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

	public User user(SigninPayload payload) {
		return payload.getUser();
	}
}