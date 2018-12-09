package com.practicaldime.jetty.sample.async;

public interface Success<T> {
    
    void onSuccess(T result);
}
