package com.practicaldime.zesty.quickstart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    
    @Autowired
    private MessageDao dao;
    
    public String getMessage(){
        return dao.getMessage();
    }
}
