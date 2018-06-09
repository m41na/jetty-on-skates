package com.jarredweb.jetty.sample.async;

public interface Handler<T> {
    
    T handle();
    
    void subscribe(Result<T> subscribe);
}
