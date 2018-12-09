package com.practicaldime.jetty.sample.app;

import com.practicaldime.zesty.tasklet.TaskletMonitor;

public class AppMonitor {
    
    public static void main(String...args){
        AppServer server = new AppServer("application-server");
        TaskletMonitor.deploy(server.getName(), server);
    }
}
