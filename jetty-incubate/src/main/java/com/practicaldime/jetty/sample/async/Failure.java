package com.practicaldime.jetty.sample.async;

public interface Failure<E> {
    
    void onFailure(E error, Exception cause);
}
