package com.jarredweb.jetty.flux.handler;

import com.jarredweb.jetty.flux.dao.ProductDao;
import com.jarredweb.jetty.flux.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ProductHandlerImpl implements ProductHandler {

    private final ProductDao dao;

    public ProductHandlerImpl(@Autowired ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public Mono<ServerResponse> getProduct(ServerRequest request) {
        Long prodId = Long.valueOf(request.pathVariable("id"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Product> productMono = this.dao.getProduct(prodId);
        return productMono.flatMap(product
                -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(product))).switchIfEmpty(notFound);
    }

    @Override
    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        Mono<Product> product = request.bodyToMono(Product.class);
        return ServerResponse.ok().build(this.dao.saveProduct(product));
    }

    @Override
    public Mono<ServerResponse> fetchProducts(ServerRequest request) {
        Flux<Product> products = this.dao.fetchProducts(0, 100);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, Product.class);
    }
}
