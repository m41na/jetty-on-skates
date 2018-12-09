package com.practicaldime.jetty.sample.config;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig{
    
    public JerseyConfig(){
        packages("com.practicaldime.jetty.sample.resource");
    }
}
