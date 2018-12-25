package com.practicaldime.jesty.todos.spring;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.practicaldime.zesty.basics.AppConfig;

public class TodosDaoConfig {

	private static final Logger LOG = LoggerFactory.getLogger(TodosDaoConfig.class);
	//instance variables
	private final AppConfig config = AppConfig.instance();
	private final DataSource dataSource = dataSource();
	private final NamedParameterJdbcTemplate template = jdbcTemplate();
	private final PlatformTransactionManager transaction = txManager();

	private DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(config.properties().getProperty("jdbc.driverClassName"));
		dataSource.setUrl(config.properties().getProperty("jdbc.url"));
		dataSource.setUsername(config.properties().getProperty("jdbc.username"));
		dataSource.setPassword(config.properties().getProperty("jdbc.password"));
		dataSource.setDefaultAutoCommit(Boolean.FALSE);
		LOG.debug("created datasource object");
		return dataSource;
	}

	private NamedParameterJdbcTemplate jdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	private PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	public NamedParameterJdbcTemplate getTemplate() {
		return template;
	}
	
	public PlatformTransactionManager getTransaction() {
		return transaction;
	}

	public TransactionStatus startTransaction() {
		return startTransaction(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
	}
	
	public TransactionStatus startTransaction(int propagation) {
		return startTransaction(propagation, TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
	}
	
	public TransactionStatus startTransaction(int propagation, int isolation ) {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setName("TodoTransction");
		definition.setPropagationBehavior(propagation);
		definition.setIsolationLevel(isolation);
		return transaction.getTransaction(definition);
	}

	public void commitTransaction(TransactionStatus status) {
		transaction.commit(status);
	}

	public void rollbackTransaction(TransactionStatus status) {
		transaction.rollback(status);
	}
}
