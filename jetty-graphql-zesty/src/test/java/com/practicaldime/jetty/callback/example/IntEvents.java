package com.practicaldime.jetty.callback.example;

public class IntEvents {

	private final EventListener<Integer> listener;
	private IntSupplier supplier = new IntSupplier();
	private boolean stop;

	public IntEvents(EventListener<Integer> listener) {
		super();
		this.listener = listener;
	}

	public void start() {
		while (!stop) {
			try {
				Thread.sleep(2000);
				listener.onEvent(new IntEvent(supplier.get()));
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
