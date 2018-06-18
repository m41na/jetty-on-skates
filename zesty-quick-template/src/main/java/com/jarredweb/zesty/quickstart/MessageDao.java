package com.jarredweb.zesty.quickstart;

import org.springframework.stereotype.Repository;

@Repository
public class MessageDao {
    
    private final String message = "You got this for real!!";
    
    public String getMessage(){
        return message;
    }
}
