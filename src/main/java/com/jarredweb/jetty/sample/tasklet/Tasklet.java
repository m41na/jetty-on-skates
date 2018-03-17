package com.jarredweb.jetty.sample.tasklet;

import java.util.concurrent.Future;

public interface Tasklet {
    
    void init();
    
    void deploy(Future<?> future);
    
    void undeploy();
    
    void destroy();
}
