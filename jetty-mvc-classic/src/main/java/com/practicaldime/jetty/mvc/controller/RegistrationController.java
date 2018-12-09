package com.practicaldime.jetty.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.practicaldime.jetty.mvc.model.User;
import com.practicaldime.jetty.mvc.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService service;
    
    @RequestMapping(path = {"/register"}, method = RequestMethod.GET)
    public ModelAndView showRegister(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mnv = new ModelAndView("register");
        mnv.addObject("user", new User());
        return mnv;
    }
    
    @RequestMapping(path = {"/register"}, method = RequestMethod.POST)
    public ModelAndView doRegister(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("user") User user){
        service.register(user);
        return new ModelAndView("welcome", "firstname", user.getFirstname());
    }
}
