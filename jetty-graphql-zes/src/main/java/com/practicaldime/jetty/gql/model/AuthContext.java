package com.practicaldime.jetty.gql.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.servlet.GraphQLContext;

public class AuthContext extends GraphQLContext {
    
    private final User user;
    
    public AuthContext(User user, HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
