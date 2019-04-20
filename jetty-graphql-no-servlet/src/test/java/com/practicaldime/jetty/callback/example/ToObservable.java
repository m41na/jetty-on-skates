package com.practicaldime.jetty.callback.example;

import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;

public abstract class ToObservable implements Consumer<IntEvent>{

	protected ObservableEmitter<IntEvent> emitter;

	public void setEmitter(ObservableEmitter<IntEvent> emitter) {
		this.emitter = emitter;
	}
}
