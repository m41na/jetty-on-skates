package com.jarredweb.jetty.sample.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/yo")
public class JerseyResource {
    
    @GET
    public String hello(){
        return "Jersey here!";
    }
}
