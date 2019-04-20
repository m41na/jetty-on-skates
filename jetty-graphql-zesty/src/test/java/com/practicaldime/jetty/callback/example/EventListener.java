package com.practicaldime.jetty.callback.example;

public interface EventListener<T> {

	void onEvent(Event<T> event);
}
