package com.practicaldime.jetty.sample.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MruCacheStorage;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@EnableWebMvc
public class WebMvcConfig {
    
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(simpleModule());
        return mapper;
    }
    
    private SimpleModule simpleModule(){
        //register custom serializer
        SimpleModule module = new SimpleModule("DefaultModule");
        //customize module
        return module;
    }
    
    @Bean
    public ViewResolver ftlViewResolver(){
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setOrder(1);
        return resolver;
    }
    
    @Bean
    public freemarker.template.Configuration ftlConfiguration() throws IOException{
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        //specify templaet source
        FileTemplateLoader ftl = new FileTemplateLoader(new File("static/ftl"));
        ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "templates/ftl");
        MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[]{ftl, ctl});
        cfg.setTemplateLoader(mtl);
        //set preferred charset which template files use
        cfg.setDefaultEncoding("UTF-8");
        //set how errors will appear
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        //don't log exceptions inside freemarker that it will throw at you anyway
        cfg.setLogTemplateExceptions(false);
        //caching strategy
        cfg.setCacheStorage(new MruCacheStorage(20, 250));
        return cfg;
    }
}
