package com.practicaldime.jesty.app;

import java.util.function.BiFunction;

public class Sample {

	public String echo(String value) {
		return value;
	}

	public String echo(String value, int modifier) {
		switch(modifier) {
			case 0:{
				return value.toUpperCase();
			}
			case 1:{
				return new StringBuilder(value).reverse().toString();
			}
			default:
				return value;
		}
	}
	
	public String echo(String value, int modifier, BiFunction<String, Integer, String> action) {
		return action.apply(value, modifier);
	}
	
	public static void main(String...args) {
		System.out.println(new Sample().echo("Test from java", 1));
	}
}
