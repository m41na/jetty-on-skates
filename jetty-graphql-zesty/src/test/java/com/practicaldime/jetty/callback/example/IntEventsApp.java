package com.practicaldime.jetty.callback.example;

public class IntEventsApp {

	public static void main(String[] args) {
		EventListener<Integer> listener = new EventListener<Integer>() {
			
			@Override
			public void onEvent(Event<Integer> event) {
				System.out.println(event.data());
			}
		};
		
		//start events
		IntEvents app = new IntEvents(listener);
		app.start();
	}
}
