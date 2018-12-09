package com.practicaldime.jetty.mvc.controller;

import com.practicaldime.jetty.mvc.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserService service;

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public String sendTime(Model model) {
        DateTimeFormatter fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        LocalDateTime date = LocalDateTime.now();
        model.addAttribute("date", date.format(fmt));
        return "home";
    }

    @RequestMapping(path = {"/users"}, method = RequestMethod.GET)
    public String usersList(@RequestParam("start") int start, @RequestParam("size") int size, @ModelAttribute("model") ModelMap model) {
        model.addAttribute("users", service.retrieve(start, size));
        return "users-list";
    }
}
