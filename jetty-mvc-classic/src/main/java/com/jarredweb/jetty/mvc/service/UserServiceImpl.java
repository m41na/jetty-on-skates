package com.jarredweb.jetty.mvc.service;

import com.jarredweb.jetty.mvc.dao.UserDao;
import com.jarredweb.jetty.mvc.model.Login;
import com.jarredweb.jetty.mvc.model.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements UserService{
    
    private final UserDao dao;

    public UserServiceImpl(@Autowired UserDao dao) {
        this.dao = dao;
    }
    
    @Override
    public void register(User user) {
        dao.register(user);
    }

    @Override
    public User validateUser(Login login) {
        return dao.validateUser(login);
    }

    @Override
    public List<User> retrieve(int start, int size) {
        return dao.retrieve(start, size);
    }    
}
