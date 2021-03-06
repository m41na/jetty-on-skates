package com.practicaldime.jetty.flux.controller;

import com.practicaldime.jetty.flux.dao.ProductDao;
import com.practicaldime.jetty.flux.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {

    private final ProductDao dao;

    public ProductController(@Autowired ProductDao dao) {
        this.dao = dao;
    }

    @GetMapping("/products/start/{start}/size/{size}")
    public Flux<Product> fetchProducts(@PathVariable("start") int start, @PathVariable("size") int size) {
        return dao.fetchProducts(start, size);
    }

    @PostMapping("/products")
    public Mono<Void> saveProduct(@RequestBody Product product) {
        return dao.saveProduct(Mono.just(product));
    }

    @GetMapping("/products/{id}")
    public Mono<Product> getProduct(Long id) {
        return dao.getProduct(id);
    }
}
