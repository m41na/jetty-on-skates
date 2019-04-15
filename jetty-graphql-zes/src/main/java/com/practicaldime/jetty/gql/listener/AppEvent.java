package com.practicaldime.jetty.gql.listener;

public class AppEvent<T> {

	public final String type;
	public final T message;
	
	public AppEvent(String type, T message) {
		super();
		this.type = type;
		this.message = message;
	}
}
