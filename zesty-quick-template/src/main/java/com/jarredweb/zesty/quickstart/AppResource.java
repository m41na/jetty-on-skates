package com.jarredweb.zesty.quickstart;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("/")
public class AppResource {    
    
    @Inject
    private AppService service;
    
    @GET
    public String helloText(){
        return service.getMessage();
    }
    
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_HTML)
    public Response helloHTML(){
        Map<String, Object> model = new HashMap<>();
        model.put("message", service.getMessage());
        return Response.ok(new Viewable("/hello", model)).build();
    }
}
