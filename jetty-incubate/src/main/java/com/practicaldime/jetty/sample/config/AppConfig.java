package com.practicaldime.jetty.sample.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.practicaldime.jetty.sample.*")
public class AppConfig {}
