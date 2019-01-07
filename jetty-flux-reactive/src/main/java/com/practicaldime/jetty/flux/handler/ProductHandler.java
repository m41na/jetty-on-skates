package com.practicaldime.jetty.flux.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ProductHandler {

    Mono<ServerResponse> getProduct(ServerRequest request);

    Mono<ServerResponse> saveProduct(ServerRequest request);

    Mono<ServerResponse> fetchProducts(ServerRequest request);
}
