package com.practicaldime.zesty.quickstart;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans(value = { @ComponentScan(basePackageClasses = AppRunner.class)})
public class AppConfig {
    
}
