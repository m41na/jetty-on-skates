package com.practicaldime.jetty.sample.async;

public interface Result<T> {
    
    Result<T> handle(Handler<T> handler);
    
    Result<T> onSuccess(Success<T> success);
    
    Result<T> onFailure(Failure<String> failure);
    
    void onComplete(Complete complete);
}
