package com.jarredweb.jesty.route;

import com.jarredweb.jesty.servlet.HandlerStatus;
import java.util.Map;

public interface RouteResponse {
    
    void header(String header, String value);
    
    void status(int status);
    
    void sendStatus(int status);
    
    void send(String payload);
    
    void json(Object payload);
    
    void jsonp(Object payload);
    
    <T>void xml(Object payload, Class<T> template);
    
    <T>void content(T payload, BodyWriter<T> writer);
    
    void render(String template, Map<String, Object>  model);
    
    void next(String path);
    
    void redirect(String path);
    
    void type(String mimetype);
    
    void cookie(String name, String value);
    
    void attachment(String filename);
    
    void download(String path, String filename, String mimeType, HandlerStatus status);
    
    byte[] getContent();
}
