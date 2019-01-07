package com.practicaldime.jetty.flux.dao;

import com.practicaldime.jetty.flux.model.Product;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import reactor.core.publisher.Mono;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDaoConfig.class, loader = AnnotationConfigContextLoader.class)
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/create-tables.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/populate-data.sql")
})
@TestPropertySource("classpath:config/test-app-dao.properties")
public class ProductDaoImplTest {
    
    @Autowired
    public ProductDao dao;

    @Test
    public void testGetProduct() {
        dao.getProduct(1l).subscribe(System.out::println);
    }

    @Test
    public void testFetchProducts() {
        dao.fetchProducts(0, 100).subscribe(System.out::println);
    }

    @Test
    public void testSaveProduct() {
        Product item = new Product();
        item.setName("bouncy");
        item.setDescr("A fun product for all");
        item.setImgUrl("bouncy.png");
        item.setPrice(new BigDecimal(200.95));
        dao.saveProduct(Mono.just(item)).subscribe(System.out::println);
    }    
}
