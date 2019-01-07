package com.practicaldime.jetty.flux.app;

import com.practicaldime.jetty.flux.handler.HelloFlux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@ComponentScan(basePackages = {"com.practicaldime.jetty.flux"})
public class AppConfig {

    @Bean
    public RouterFunction<ServerResponse> route(@Autowired HelloFlux greetingHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/app/hello"), greetingHandler::hello);
    }
}
