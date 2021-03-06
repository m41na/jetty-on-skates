package com.practicaldime.jetty.flux.dao;

import com.practicaldime.jetty.flux.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDao {
    
    Mono<Product> getProduct(Long id);
    
    Flux<Product> fetchProducts(int start, int size);
    
    Mono<Void> saveProduct(Mono<Product> product);
}
