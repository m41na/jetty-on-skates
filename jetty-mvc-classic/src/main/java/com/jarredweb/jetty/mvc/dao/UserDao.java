package com.jarredweb.jetty.mvc.dao;

import com.jarredweb.jetty.mvc.model.Login;
import com.jarredweb.jetty.mvc.model.User;
import java.util.List;

public interface UserDao {
    
    void register(User user);
    
    User validateUser(Login login);
    
    List<User> retrieve(int start, int size);
}
