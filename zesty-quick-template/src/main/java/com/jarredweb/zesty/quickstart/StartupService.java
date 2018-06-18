package com.jarredweb.zesty.quickstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StartupService {
    
    private static final Logger LOG = LoggerFactory.getLogger(StartupService.class);
    
    public void initialize(){
        LOG.info("Initializing application");
    }
    
    //...add other methods as need be
}
