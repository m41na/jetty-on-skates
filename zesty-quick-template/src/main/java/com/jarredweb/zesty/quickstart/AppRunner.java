package com.jarredweb.zesty.quickstart;

import com.jarredweb.zesty.http.app.ZestyRunner;
import org.springframework.context.ApplicationContext;

public class AppRunner extends ZestyRunner{

    @Override
    public void initApplication(ApplicationContext ctx) {
        StartupService startup = ctx.getBean(StartupService.class);
        startup.initialize();
    }
    
    @Override
    public void startApplication(String... args) {
        String configClass = AppConfig.class.getName();
        System.setProperty("context.lookup", configClass);
        this.packages("com.jarredweb.zesty.quickstart").create(args);
    }
    
    public static void main(String... args) {
        new AppRunner().startApplication(args);
    }
}
