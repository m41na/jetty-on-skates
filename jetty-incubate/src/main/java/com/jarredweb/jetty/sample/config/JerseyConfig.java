package com.jarredweb.jetty.sample.config;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig{
    
    public JerseyConfig(){
        packages("com.jarredweb.jetty.sample.resource");
    }
}
