package com.jarredweb.jetty.flux.dao;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config/app-dao.properties")
public class DaoConfig {
 
    @Value("${app.jdbc.driver}")
    String jdbcDriver;
    @Value("${app.jdbc.url}")
    String jdbcUrl;
    @Value("${app.jdbc.username}")
    String jdbcUser;
    @Value("${app.jdbc.password}")
    String jdbcPass;

    @Value("classpath:sql/create-table.sql")
    private Resource schemaScript;

    @Value("classpath:sql/insert-data.sql")
    private Resource dataScript;
    
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
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }
}
