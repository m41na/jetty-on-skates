package com.jarredweb.jesty.app;

import java.util.Map;

public class AppProvider {

    private final String status;

    public AppProvider() {
        this.status = "now ready";
    }

    public String status() {
        return "provider is " + status;
    }

    public AppServer provide(Map<String, String> props) {
        return new AppServer(props);
    }
}