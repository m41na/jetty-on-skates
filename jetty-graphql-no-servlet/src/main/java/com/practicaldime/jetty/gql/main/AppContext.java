package com.practicaldime.jetty.gql.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.practicaldime.jetty.gql.dao.LinkRepository;
import com.practicaldime.jetty.gql.dao.LinkRepositoryImpl;
import com.practicaldime.jetty.gql.dao.LinkRepositoryPub;
import com.practicaldime.jetty.gql.dao.UserRepository;
import com.practicaldime.jetty.gql.dao.VoteRepository;
import com.practicaldime.jetty.gql.listener.AppEventListener;
import com.practicaldime.jetty.gql.websock.LinkCreated;
import com.practicaldime.jetty.gql.websock.PublishWsAdapter;
import com.practicaldime.jetty.gql.websock.SchemaProvider;

@Configuration
@ComponentScan(basePackages= {"com.practicaldime.jetty.gql.api.impl"})
public class AppContext {

private AnnotationConfigApplicationContext ctx;
	
	public void init() {
		ctx = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getBean(String name, Class<T> type){
		if(type == null) {
			return (T) ctx.getBean(name);
		}
		else if(name == null || name.trim().length() == 0) {
			return ctx.getBean(type);
		}
		else {
			return ctx.getBean(name, type);
		}
	}
	
	@Bean
	public MongoDatabase mongo() {
		MongoClient client = new MongoClient();
		MongoDatabase mongo = client.getDatabase("admin");
		Runtime.getRuntime().addShutdownHook(new Thread(()->client.close()));
		return mongo;
	}
	
	@Bean
	public UserRepository userRepository(@Autowired MongoDatabase mongo) {
		return new UserRepository(mongo.getCollection("users"));
	}
	

	@Bean
	public VoteRepository voteRepository(@Autowired MongoDatabase mongo) {
		return new VoteRepository(mongo.getCollection("votes"));
	}
	
	@Bean(name="impl")
	public LinkRepository linkRepositoryImpl(@Autowired MongoDatabase mongo) {
		return new LinkRepositoryImpl(mongo.getCollection("links"));
	}
	
	@Bean
	@Primary
	public LinkRepository linkRepositoryPublisher(@Autowired @Qualifier("impl") LinkRepository repo, @Autowired AppEventListener<LinkCreated> listener) {
		return new LinkRepositoryPub(repo, listener);
	}
	
	@Bean
	public AppEventListener<LinkCreated> appLinkCreatedListener() {
		return new AppEventListener<LinkCreated>();
	}
	
	@Bean
	public SchemaProvider schemaProvider(@Autowired AppEventListener<LinkCreated> listener) {
		return new SchemaProvider(listener);
	}
	
	@Bean
	public PublishWsAdapter publishWsAdapter(@Autowired SchemaProvider provider) {
		return new PublishWsAdapter(provider);
	}
}
