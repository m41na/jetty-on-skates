package com.jarredweb.jetty.mvc.controller;

import com.jarredweb.jetty.mvc.model.Login;
import com.jarredweb.jetty.mvc.model.User;
import com.jarredweb.jetty.mvc.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController  {

    @Autowired
    private UserService service;
    
    @RequestMapping(path = {"/login"}, method = RequestMethod.GET)
    public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mnv = new ModelAndView("login");
        mnv.addObject("login", new Login());
        return mnv;
    }
    
    @RequestMapping(path = {"/login"}, method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("login") Login login){
        ModelAndView mnv;
        User user = service.validateUser(login);
        
        if(null != user){
            mnv = new ModelAndView("welcome");
            mnv.addObject("firstname", user.getFirstname());
        }
        else{
            mnv = new ModelAndView("login");
            mnv.addObject("message", "Username or Password is wrong!");
        }
        return mnv;
    }
}
