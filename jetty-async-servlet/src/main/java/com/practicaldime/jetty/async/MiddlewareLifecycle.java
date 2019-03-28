package com.practicaldime.jetty.async;

public interface MiddlewareLifecycle {

	void onRegistered(String name);
	
	void onUnregistered(String name);
}
