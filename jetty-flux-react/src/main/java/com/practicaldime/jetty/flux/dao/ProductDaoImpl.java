package com.practicaldime.jetty.flux.dao;

import com.practicaldime.jetty.flux.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductDaoImpl implements ProductDao{
    
    private final JdbcTemplate jdbcTemplate;

    public ProductDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mono<Product> getProduct(Long id) {
        String sql = "select * from tbl_product where item_id=" + id;
        List<Product> products = jdbcTemplate.query(sql, new ProductMapper());
        return products.size() > 0? Mono.just(products.get(0)) : Mono.empty();
    }

    @Override
    public Flux<Product> fetchProducts(int start, int size) {
        String sql = "select * from tbl_product limit " + size + " offset " + start;
        List<Product> products = jdbcTemplate.query(sql, new ProductMapper());
        return Flux.fromIterable(products);
    }

    @Override
    public Mono<Void> saveProduct(Mono<Product> product) {
        Mono<Product> mono = product.doOnNext(item-> {
            String sql = "insert into tbl_product (item_name, item_descr, item_img, item_price) values (?,?,?,?)";
            KeyHolder genKey = new GeneratedKeyHolder();
            PreparedStatementCreator psc = (Connection con) -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, item.getName());
                ps.setString(2, item.getDescr());
                ps.setString(3, item.getImgUrl());
                ps.setBigDecimal(4, item.getPrice());
                return ps;
            };
            int rows = jdbcTemplate.update(psc, genKey);
            assert rows == 1;
            Long generatedId = genKey.getKey().longValue();
            item.setId(generatedId);
        });
        return mono.thenEmpty(Mono.empty());
    }    
}

class ProductMapper implements RowMapper<Product>{

    @Override
    public Product mapRow(ResultSet rs, int i) throws SQLException {
        Product prd = new Product();
        prd.setId(rs.getLong("item_id"));
        prd.setName(rs.getString(("item_name")));
        prd.setDescr(rs.getString("item_descr"));
        prd.setImgUrl(rs.getString("item_img"));
        prd.setPrice(rs.getBigDecimal("item_price"));
        return prd;
    }    
}
