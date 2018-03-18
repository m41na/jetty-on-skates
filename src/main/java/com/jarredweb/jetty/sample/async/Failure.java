package com.jarredweb.jetty.sample.async;

public interface Failure<E> {
    
    void onFailure(E error, Exception cause);
}
