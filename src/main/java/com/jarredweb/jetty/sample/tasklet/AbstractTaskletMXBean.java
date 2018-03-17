package com.jarredweb.jetty.sample.tasklet;

public interface AbstractTaskletMXBean {
    
    String getName();
    
    void setName(String name);
    
    void start();
    
    void stop();
}
