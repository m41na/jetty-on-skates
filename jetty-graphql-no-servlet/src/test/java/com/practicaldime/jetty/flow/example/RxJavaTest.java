package com.practicaldime.jetty.flow.example;

import org.junit.Test;
import static org.junit.Assert.*;

import io.reactivex.Observable;

public class RxJavaTest {

	String result="";

    // Simple subscription to a fix value
    @Test
    public void returnAValue(){
        result = "";
        Observable<String> observer = Observable.just("Hello"); // provides datea
        observer.subscribe(s -> result = s); // Callable as subscriber
        assertTrue(result.equals("Hello"));
    }
}
