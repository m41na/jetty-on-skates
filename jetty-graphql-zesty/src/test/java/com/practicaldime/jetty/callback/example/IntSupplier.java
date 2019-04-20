package com.practicaldime.jetty.callback.example;

import java.util.function.Supplier;

public class IntSupplier implements Supplier<Integer> {

	private Integer value = 0;
	
	@Override
	public Integer get() {
		return value++;
	}
}
