package com.jarredweb.simplest.app;

import com.jarredweb.webjar.http.app.AppRunnerBuilder;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("/")
public class AppRunner {
    
    public static void main(String... args) {
        new AppRunnerBuilder().create(args, AppRunner.class);
    }
    
    @GET
    public String helloText(){
        return "You got this";
    }
    
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_HTML)
    public Response helloHTML(){
        Map<String, Object> model = new HashMap<>();
        model.put("message", "You got this!!");
        return Response.ok(new Viewable("/hello", model)).build();
    }
}
