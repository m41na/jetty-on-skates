package com.jarredweb.jesty.todos;

import java.util.Date;

public class Account {
    
    public String username;
    public String password;
    public Date createdOn;
    public User user;

    public Account() {
        super();
    }

    public Account(String username, String password, Date createdOn, User user) {
        this.username = username;
        this.password = password;
        this.createdOn = createdOn;
        this.user = user;
    }
}
