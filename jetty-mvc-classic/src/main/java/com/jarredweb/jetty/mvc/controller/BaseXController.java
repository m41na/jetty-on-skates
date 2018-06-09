package com.jarredweb.jetty.mvc.controller;

import java.io.InputStream;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseXController {
    
    private static final Logger LOG = LoggerFactory.getLogger(BaseXController.class);

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, path = {"/basex/{source}"}, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody String sendXmlData(@PathVariable("source") String source) {
        InputStream is = BaseXController.class.getResourceAsStream("/xmldata/" + source + ".xml");
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.POST, path = {"/basex/{source}"}, consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelMap acceptXmlData(@PathVariable("source") String source, @RequestBody String xml) {
        LOG.info(xml);
        ModelMap model = new ModelMap();
        model.addAttribute("result",0);
        return model;
    }
}
