package com.practicaldime.jetty.flux.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TestDaoConfig {
 
    @Value("${app.jdbc.driver}")
    private String jdbcDriver;
    @Value("${app.jdbc.url}")
    private String jdbcUrl;
    @Value("${app.jdbc.username}")
    private String jdbcUser;
    @Value("${app.jdbc.password}")
    private String jdbcPass;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean(destroyMethod = "")
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource(jdbcUrl, jdbcUser, jdbcPass);
        ds.setDriverClassName(jdbcDriver);
        return ds;
    }
    
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
    	return new DataSourceTransactionManager(dataSource());	
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
    
    @Bean
    public ProductDao productDao(){
        return new ProductDaoImpl(jdbcTemplate());
    }
}
