package com.practicaldime.jetty.gql.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthContext  {
    
    private final User user;
    
    public AuthContext(User user, HttpServletRequest request, HttpServletResponse response) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
