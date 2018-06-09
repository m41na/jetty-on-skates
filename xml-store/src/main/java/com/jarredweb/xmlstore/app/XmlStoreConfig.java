package com.jarredweb.xmlstore.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans(value = { @ComponentScan(basePackages = {"com.jarredweb.xmlstore"})})
public class XmlStoreConfig {
    
}
