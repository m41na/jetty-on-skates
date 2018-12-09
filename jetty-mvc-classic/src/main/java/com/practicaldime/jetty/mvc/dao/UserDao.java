package com.practicaldime.jetty.mvc.dao;

import com.practicaldime.jetty.mvc.model.Login;
import com.practicaldime.jetty.mvc.model.User;
import java.util.List;

public interface UserDao {
    
    void register(User user);
    
    User validateUser(Login login);
    
    List<User> retrieve(int start, int size);
}
