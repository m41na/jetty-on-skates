package com.practicaldime.jetty.sample.async;

public interface Handler<T> {
    
    T handle();
    
    void subscribe(Result<T> subscribe);
}
