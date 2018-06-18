package com.jarredweb.xmlstore.app;

import com.jarredweb.zesty.http.app.ZestyRunner;
import com.jarredweb.xmlstore.service.StartupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class XmlStoreApp extends ZestyRunner{
    
    private static final Logger LOG = LoggerFactory.getLogger(ZestyRunner.class);
    
    @Override
    public void initApplication(ApplicationContext ctx) {
        StartupService startup = ctx.getBean(StartupService.class);
        startup.initialize();
    }

    public static void main(String... args) {
        String configClass = XmlStoreConfig.class.getName();
        LOG.info("loading configuration for {} from {}", XmlStoreApp.class.getName(), configClass);
        System.setProperty("context.lookup", configClass);
        new XmlStoreApp().packages("com.jarredweb.xmlstore.resource").create(args);
    }
}
