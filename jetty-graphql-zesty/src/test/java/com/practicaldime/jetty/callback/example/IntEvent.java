package com.practicaldime.jetty.callback.example;

public class IntEvent implements Event<Integer> {

	private final Integer value;
	
	public IntEvent(Integer value) {
		super();
		this.value = value;
	}

	@Override
	public Integer data() {
		return this.value;
	}
}
