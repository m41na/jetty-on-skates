package com.jarredweb.jetty.mvc.dao;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource(){
    	BasicDataSource ds = new BasicDataSource();
    	ds.setUrl(jdbcUrl);
    	ds.setUsername(jdbcUser);
    	ds.setPassword(jdbcPass);
        ds.setDriverClassName(jdbcDriver);
        return ds;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
    
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
    	return new DataSourceTransactionManager(dataSource());	
    }
    
    @Bean
    public UserDao userDao(){
        return new UserDaoImpl(jdbcTemplate());
    }
}
