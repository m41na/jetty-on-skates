package com.jarredweb.jettsey.route;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppRoute {
    
    public String pathId;
    public final String path;
    public final String method;
    public final String accepts;
    public final String contentType;
    public final Map<String, String> headers = new HashMap<>();
    public final Map<String, String> pathParams = new HashMap<>();

    public AppRoute(String path, String method, String accepts, String contentType) {
        this.path = path;
        this.method = method;
        this.accepts = accepts;
        this.contentType = contentType;
    }
    
    public void setId(){
        this.pathId = UUID.randomUUID().toString();
    }
}