package com.jarredweb.jetty.sample.app;

import com.jarredweb.jetty.sample.tasklet.TaskletMonitor;

public class AppMonitor {
    
    public static void main(String...args){
        AppServer server = new AppServer("application-server");
        TaskletMonitor.deploy(server.getName(), server);
    }
}
