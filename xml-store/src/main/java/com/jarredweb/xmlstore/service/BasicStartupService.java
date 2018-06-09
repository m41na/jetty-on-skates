package com.jarredweb.xmlstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BasicStartupService implements StartupService{

    private static final Logger LOG = LoggerFactory.getLogger(BasicStartupService.class);

    @Override
    public void initialize() {
        LOG.debug("Not yet implemented. Override when needed");
    }

    @Override
    public void onInitialized() {
        LOG.debug("Not yet implemented. Override when needed");
    }    
}
