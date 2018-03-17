package com.jarredweb.jetty.sample.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.jarredweb.jetty.sample.*")
public class AppConfig {}
